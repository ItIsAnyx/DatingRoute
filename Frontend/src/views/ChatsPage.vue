<template>
  <div class="chats-page">
    <HeaderComponent />

    <main class="chats-main">
      <div class="chats-container">
        <ChatsSidebar
          v-if="!isChatOpen"
          :chats="chats"
          :active-chat="activeChat"
          @select="selectChat"
          @create="createNewChat"
          @delete="deleteChat" 
        />

        <ChatArea
          v-if="activeChat"
          :chat="activeChat"
          :user-initials="userInitials"
          :loading="loading"
          @send="sendMessage"
          @back="closeChat"
          @clear-chat="clearChat"
          @export-chat="exportChat"
          @delete-chat="deleteChat" 
        />

        <EmptyChatView
          v-else
          :prompt-suggestions="promptSuggestions"
          @select-suggestion="handleSuggestion"
        />
      </div>
    </main>
  </div>
</template>

<!-- ... (скрипт и стили уже обновлены выше) ... -->

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import HeaderComponent from '@/components/HeaderComponent.vue'
import ChatsSidebar from '@/components/chats/ChatsSidebar.vue'
import ChatArea from '@/components/chats/ChatArea.vue'
import EmptyChatView from '@/components/chats/EmptyChatView.vue'
// Импортируем наши новые функции
import { connect, subscribeToChat, sendMessage as wsSendMessage, disconnect, isConnected } from '@/services/websocketService.js'

const router = useRouter()
const activeChat = ref(null)
const isMobile = ref(false)
const loading = ref(false)
const chats = ref([])
let stompClientConnection = null // Храним соединение

const promptSuggestions = [
  'Создай маршрут для первого свидания',
  'Спланируй поход по барам с друзьями',
  'Нужны идеи для активного отдыха',
  'Вечер в культурном центре города'
]

const userInitials = computed(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.name ? user.name.charAt(0).toUpperCase() : 'U'
})

const isChatOpen = computed(() => isMobile.value && !!activeChat.value)

const getAuthToken = () => localStorage.getItem('access_token')
const getAuthHeaders = () => {
  const token = getAuthToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
}

// --- Функции для работы с API ---

// 1. Загрузка списка чатов (только id и title)
const loadChats = async () => {
  try {
    const response = await axios.get('/api/chats', { headers: getAuthHeaders() })
    // Мапим только id и title, история сообщений загружается отдельно
    chats.value = response.data.map(chat => ({
      id: chat.id,
      title: chat.title,
      lastMessage: '', // Будет обновлено при загрузке истории
      updatedAt: new Date(), // Будет обновлено
      unreadCount: 0,
      messages: [] // История пока пуста
    }))
  } catch (error) {
    console.error('Ошибка при загрузке чатов:', error)
    if (error.response?.status === 401) {
      router.push('/auth')
    }
  }
}

// 2. Загрузка истории сообщений для конкретного чата
const loadChatHistory = async (chatId) => {
  try {
    const response = await axios.get(`/api/chats/${chatId}/messages`, { headers: getAuthHeaders() })
    const chat = chats.value.find(c => c.id === chatId)
    if (chat) {
      chat.messages = response.data.messages.map(msg => ({
        id: msg.id,
        type: msg.message_type === 'USER_MESSAGE' ? 'user' : 'ai',
        text: msg.content,
        timestamp: new Date(msg.send_date)
      }))
      // Обновляем lastMessage после загрузки истории
      if (chat.messages.length > 0) {
        const lastMsg = chat.messages[chat.messages.length - 1];
        chat.lastMessage = lastMsg.text;
        chat.updatedAt = lastMsg.timestamp;
      }
    }
  } catch (error) {
    console.error('Ошибка при загрузке истории чата:', error)
    if (error.response?.status === 404) {
      alert('Чат не найден')
    }
  }
}

// 3. Удаление чата
const deleteChat = async (chatId) => {
  if (!confirm('Вы уверены, что хотите удалить этот чат?')) return;

  try {
    await axios.delete(`/api/chats/${chatId}`, { headers: getAuthHeaders() })
    chats.value = chats.value.filter(chat => chat.id !== chatId)
    if (activeChat.value?.id === chatId) {
      activeChat.value = null
    }
  } catch (error) {
    console.error('Ошибка при удалении чата:', error)
    alert('Не удалось удалить чат')
  }
}


// --- Функции для управления состоянием UI ---

const selectChat = async (chat) => {
  activeChat.value = chat
  chat.unreadCount = 0
  
  // Если соединение еще не установлено, устанавливаем его
  if (!isConnected()) {
    try {
      stompClientConnection = await connect();
      console.log('WebSocket connection established');
    } catch (error) {
      console.error('Failed to establish WebSocket connection:', error);
      // Здесь можно показать уведомление пользователю
      return;
    }
  }

  // Загружаем историю, если она еще не загружена
  if (chat.messages.length === 0) {
    await loadChatHistory(chat.id)
  }

  // Подписываемся на сообщения для нового чата
  if (stompClientConnection) {
    // Отписываемся от предыдущих подписок, если они были
    // (это может потребовать хранения ссылок на подписки)
    
    subscribeToChat(chat.id, (message) => {
      // Это callback, который вызовется при получении нового сообщения
      if (message.chat_id === activeChat.value.id) {
        const aiMsg = {
          id: message.id,
          type: 'ai',
          text: message.content,
          timestamp: new Date(message.send_date)
        }
        activeChat.value.messages.push(aiMsg)
        activeChat.value.lastMessage = aiMsg.text
        activeChat.value.updatedAt = aiMsg.timestamp
      }
    })
  }
}

const closeChat = () => (activeChat.value = null)

const createNewChat = () => {
  const tempId = Date.now()
  const newChat = {
    id: tempId,
    title: 'Новый чат',
    lastMessage: '',
    updatedAt: new Date(),
    unreadCount: 0,
    messages: []
  }
  chats.value.unshift(newChat)
  activeChat.value = newChat
  return newChat
}

const clearChat = () => {
  if (activeChat.value) {
    activeChat.value.messages = []
    activeChat.value.lastMessage = 'Чат очищен'
    activeChat.value.updatedAt = new Date()
  }
}

const exportChat = () => {
  if (activeChat.value) {
    const chatData = { title: activeChat.value.title, messages: activeChat.value.messages }
    const dataStr = JSON.stringify(chatData, null, 2)
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
    const exportFileDefaultName = `chat_${activeChat.value.title}_${Date.now()}.json`
    const linkElement = document.createElement('a')
    linkElement.setAttribute('href', dataUri)
    linkElement.setAttribute('download', exportFileDefaultName)
    linkElement.click()
  }
}

// 4. Отправка сообщения (самая важная часть)
const sendMessage = async (text) => {
  if (!text.trim() || loading.value || !activeChat.value) return

  const userMsg = { id: Date.now(), type: 'user', text, timestamp: new Date() }
  activeChat.value.messages.push(userMsg)
  activeChat.value.lastMessage = text
  activeChat.value.updatedAt = new Date()

  // Если чат временный, сначала создаем его через HTTP
  if (activeChat.value.id > 1000000) {
    loading.value = true
    try {
      const response = await axios.post('/api/chats', { message: text }, { headers: getAuthHeaders() })
      const responseData = response.data;
      activeChat.value.id = responseData.id;
      activeChat.value.title = responseData.title || 'Новый маршрут';
      
      const aiMsg = {
        id: responseData.message.id,
        type: 'ai',
        text: responseData.message.content,
        timestamp: new Date(responseData.message.send_date)
      }
      activeChat.value.messages.push(aiMsg);
      activeChat.value.lastMessage = aiMsg.text;
      activeChat.value.updatedAt = aiMsg.timestamp;
    } catch (error) {
      console.error('Error creating chat:', error);
      // Добавляем сообщение об ошибке в чат
      const errorMsg = { 
        id: Date.now(), 
        type: 'ai', 
        text: 'Извините, произошла ошибка. Попробуйте еще раз.', 
        timestamp: new Date(),
        isError: true
      }
      activeChat.value.messages.push(errorMsg);
    } finally {
      loading.value = false
    }
  } else {
    // Если чат уже существует, отправляем сообщение через WebSocket
    try {
      wsSendMessage(activeChat.value.id, text)
    } catch (error) {
      console.error('Error sending message via WebSocket:', error);
      // Добавляем сообщение об ошибке в чат
      const errorMsg = { 
        id: Date.now(), 
        type: 'ai', 
        text: 'Не удалось отправить сообщение. Проверьте подключение к интернету.', 
        timestamp: new Date(),
        isError: true
      }
      activeChat.value.messages.push(errorMsg);
    }
  }
}

const handleSuggestion = (text) => {
  createNewChat()
  sendMessage(text)
}

const checkMobile = () => (isMobile.value = window.innerWidth <= 768)

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
  loadChats()
  
  // Устанавливаем WebSocket соединение при загрузке страницы
  connect().then(client => {
    stompClientConnection = client
  }).catch(error => {
    console.error('Failed to establish WebSocket connection on mount:', error)
  })
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
  disconnect()
})
</script>

<style scoped>
.chats-page {
  min-height: 100vh;
  background: #191919;
}

.chats-main {
  padding-top: 69px;
  min-height: calc(100vh - 69px);
}

.chats-container {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  height: calc(100vh - 80px);
  background: #191919;
}
</style>