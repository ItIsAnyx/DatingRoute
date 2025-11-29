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

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import HeaderComponent from '@/components/HeaderComponent.vue'
import ChatsSidebar from '@/components/chats/ChatsSidebar.vue'
import ChatArea from '@/components/chats/ChatArea.vue'
import EmptyChatView from '@/components/chats/EmptyChatView.vue'

const router = useRouter()
const activeChat = ref(null)
const newMessage = ref('')
const isMobile = ref(false)
const loading = ref(false)


const chats = ref([])

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

const getAuthToken = () => {
  return localStorage.getItem('access_token')
}

const getAuthHeaders = () => {
  const token = getAuthToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
}


const loadChats = async () => {
  try {
    const response = await axios.get('/api/chats', {
      headers: getAuthHeaders()
    })
    
    chats.value = response.data.map(chat => ({
      id: chat.id,
      title: chat.title,
      lastMessage: chat.message,
      updatedAt: new Date(chat.createdAt),
      unreadCount: 0,
      messages: [{
        id: 1,
        type: 'user',
        text: chat.message,
        timestamp: new Date(chat.createdAt)
      }]
    }))
  } catch (error) {
    console.error('Ошибка при загрузке чатов:', error)
    if (error.response && error.response.status === 401) {
      router.push('/auth')
    }
  }
}

const selectChat = (chat) => {
  activeChat.value = chat
  chat.unreadCount = 0
}

const closeChat = () => (activeChat.value = null)

const createNewChat = async () => {
  const tempId = Date.now()
  const newChat = {
    id: tempId,
    title: 'Новый чат',
    lastMessage: 'Чат только создан...',
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
    const chatData = {
      title: activeChat.value.title,
      messages: activeChat.value.messages,
      createdAt: activeChat.value.updatedAt
    }
    
    const dataStr = JSON.stringify(chatData, null, 2)
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
    
    const exportFileDefaultName = `chat_${activeChat.value.title}_${Date.now()}.json`
    
    const linkElement = document.createElement('a')
    linkElement.setAttribute('href', dataUri)
    linkElement.setAttribute('download', exportFileDefaultName)
    linkElement.click()
  }
}

const sendMessage = async (text) => {
  if (!text.trim() || loading.value) return

  if (!activeChat.value) {
    await createNewChat()
  }

  const userMsg = { 
    id: Date.now(), 
    type: 'user', 
    text, 
    timestamp: new Date() 
  }
  activeChat.value.messages.push(userMsg)
  activeChat.value.lastMessage = text
  activeChat.value.updatedAt = new Date()

  loading.value = true
  
  try {
    const response = await axios.post('/api/chats', 
      { message: text },
      { headers: getAuthHeaders() }
    )

    if (response.status === 201) {
      const responseData = response.data

      if (activeChat.value.messages.length === 1) {
        activeChat.value.title = responseData.title || 'Новый маршрут'
      }

      const aiMsg = {
        id: Date.now() + 1,
        type: 'ai',
        text: responseData.message || 'Маршрут создан успешно',
        timestamp: new Date()
      }
      activeChat.value.messages.push(aiMsg)
      activeChat.value.lastMessage = aiMsg.text
      activeChat.value.updatedAt = new Date()

      if (typeof activeChat.value.id === 'number' && activeChat.value.id > 1000000) {
        activeChat.value.id = responseData.id
      }
    }
  } catch (error) {
    console.error('Ошибка при отправке сообщения:', error)

    let errorMessage = 'Произошла ошибка при обработке вашего запроса'
    
    if (error.response) {
      if (error.response.status === 400) {
        errorMessage = error.response.data.message || 'Неверный формат запроса'
      } else if (error.response.status === 401) {
        errorMessage = 'Ошибка авторизации. Пожалуйста, войдите снова.'
        router.push('/auth')
      }
    }
    
    const errorMsg = {
      id: Date.now() + 1,
      type: 'ai',
      text: errorMessage,
      timestamp: new Date(),
      isError: true
    }
    activeChat.value.messages.push(errorMsg)
    activeChat.value.lastMessage = errorMessage
    activeChat.value.updatedAt = new Date()
  } finally {
    loading.value = false
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