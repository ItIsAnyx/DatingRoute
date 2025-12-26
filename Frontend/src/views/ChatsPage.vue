<template>
  <div class="chats-page">
    <HeaderComponent />

    <main class="chats-main">
      <div class="chats-container">
        <ChatsSidebar
          v-if="isMobile"
          :chats="chats"
          :active-chat="activeChat"
          :is-mobile="isMobile"
          :sidebar-open="sidebarOpen"
          @select="selectChat"
          @create="createNewChat"
          @delete="deleteChat" 
        />
        
        <ChatsSidebar
          v-else
          :chats="chats"
          :active-chat="activeChat"
          :is-mobile="isMobile"
          :sidebar-open="true"
          @select="selectChat"
          @create="createNewChat"
          @delete="deleteChat" 
        />

        <div class="chat-content-wrapper">
          <button 
            v-if="isMobile" 
            class="sidebar-toggle-btn" 
            @click="toggleSidebar"
          >
            ☰ Чаты
          </button>

          <div class="chat-content">
            <ChatArea
              v-if="activeChat"
              :chat="activeChat"
              :user-initials="userInitials"
              :loading="loading"
              :has-route="hasRoute"
              :is-mobile="isMobile"
              @send="sendMessage"
              @generate-points="generatePoints"
              @generate-route="generateRoute"
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
        </div>
      </div>
    </main>
  </div>
</template>


<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

import HeaderComponent from '@/components/HeaderComponent.vue';
import ChatsSidebar from '@/components/chats/ChatsSidebar.vue';
import ChatArea from '@/components/chats/ChatArea.vue';
import EmptyChatView from '@/components/chats/EmptyChatView.vue';

import {
  connect,
  subscribeToChat,
  sendMessage as wsSendMessage,
  disconnect,
  isConnected
} from '@/services/websocketService.js';

const router = useRouter();
const activeChat = ref(null);
const isMobile = ref(false);
const sidebarOpen = ref(false);
const loading = ref(false);
const chats = ref([]);
const currentRouteId = ref(null);
let stompClientConnection = null;

const promptSuggestions = [
  'Создай маршрут для первого свидания',
  'Спланируй поход по барам с друзьями',
  'Нужны идеи для активного отдыха',
  'Вечер в культурном центре города'
];

const userInitials = computed(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  return user.name ? user.name.charAt(0).toUpperCase() : 'U';
});

const hasRoute = computed(() => !!currentRouteId.value);

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value;
};

const getAuthToken = () => localStorage.getItem('access_token');
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

const loadChats = async () => {
  try {
    const response = await axios.get('/api/chats', { headers: getAuthHeaders() });
    chats.value = response.data.map(chat => ({
      id: chat.id,
      title: chat.title,
      lastMessage: '',
      updatedAt: new Date(),
      unreadCount: 0,
      messages: []
    }));
  } catch (error) {
    console.error('Ошибка при загрузке чатов:', error);
    if (error.response?.status === 401) router.push('/auth');
  }
};

const loadChatHistory = async (chatId) => {
  try {
    const response = await axios.get(`/api/chats/${chatId}/messages`, { headers: getAuthHeaders() });
    const chat = chats.value.find(c => c.id === chatId);
    if (chat) {
      chat.messages = response.data.messages.map(msg => ({
        id: msg.id,
        type: msg.message_type === 'USER_MESSAGE' ? 'user' : 'ai',
        text: msg.content,
        timestamp: new Date(msg.send_date.replace(' ', 'T') + 'Z')
      }));
      if (chat.messages.length > 0) {
        const lastMsg = chat.messages[chat.messages.length - 1];
        chat.lastMessage = lastMsg.text;
        chat.updatedAt = lastMsg.timestamp;
      }
    }
  } catch (error) {
    console.error('Ошибка при загрузке истории чата:', error);
    if (error.response?.status === 404) console.error('Чат не найден');
  }
};

const checkRouteExists = async (chatId) => {
  try {
    const response = await axios.get(`/api/routes?chatId=${chatId}`, { headers: getAuthHeaders() });
    if (response.data.exists) {
      currentRouteId.value = response.data.route_id;
      await loadRoutePoints();
      return true;
    }
    return false;
  } catch (error) {
    console.error('Ошибка при проверке маршрута:', error);
    return false;
  }
};

const loadRoutePoints = async () => {
  if (!currentRouteId.value) return;
  
  try {
    const response = await axios.get(`/api/routes/${currentRouteId.value}/places`, { headers: getAuthHeaders() });
    const points = response.data.points;
    
    if (points && points.length > 0 && activeChat.value) {
      const pointsMessage = {
        id: Date.now(),
        type: 'system',
        text: `Точки маршрута:\n${points.map(point => `• ${point}`).join('\n')}`,
        timestamp: new Date(),
        isPoints: true
      };
      activeChat.value.messages.push(pointsMessage);
    }
  } catch (error) {
    console.error('Ошибка при загрузке точек маршрута:', error);
  }
};

const generatePoints = async () => {
  if (!activeChat.value || loading.value) return;
  
  loading.value = true;
  
  try {
    let response;
    
    if (currentRouteId.value) {
      response = await axios.put(`/api/routes/${currentRouteId.value}/places`, {}, { headers: getAuthHeaders() });
    } else {
      response = await axios.post(`/api/routes?chatId=${activeChat.value.id}`, {}, { headers: getAuthHeaders() });
      currentRouteId.value = response.data.route_id;
    }
    
    const points = response.data.points;
    
    if (points && points.length > 0) {
      const pointsMessage = {
        id: Date.now(),
        type: 'ai',
        text: `Предлагаемые точки для вашего маршрута:\n${points.map(point => `• ${point}`).join('\n')}`,
        timestamp: new Date(),
        isPoints: true
      };
      activeChat.value.messages.push(pointsMessage);
    }
  } catch (error) {
    console.error('Ошибка при генерации точек:', error);
    if (activeChat.value) {
      activeChat.value.messages.push({
        id: Date.now(),
        type: 'ai',
        text: 'Извините, не удалось сгенерировать точки маршрута. Попробуйте еще раз.',
        timestamp: new Date(),
        isError: true
      });
    }
  } finally {
    loading.value = false;
  }
};

const generateRoute = async () => {
  if (!currentRouteId.value || !activeChat.value || loading.value) return;
  
  loading.value = true;
  
  try {  
    const routeMessage = {
      id: Date.now(),
      type: 'ai',
      text: `Ваш маршрут готов! Вы можете посмотреть его на карте.`,
      timestamp: new Date(),
      isRoute: true,
      routeId: currentRouteId.value
    };
    activeChat.value.messages.push(routeMessage);
  } catch (error) {
    console.error('Ошибка при генерации маршрута:', error);
    if (activeChat.value) {
      activeChat.value.messages.push({
        id: Date.now(),
        type: 'ai',
        text: 'Извините, не удалось сгенерировать маршрут. Попробуйте еще раз.',
        timestamp: new Date(),
        isError: true
      });
    }
  } finally {
    loading.value = false;
  }
};

const deleteChat = async (chatId) => {
  if (!confirm('Вы уверены, что хотите удалить этот чат?')) return;
  try {
    await axios.delete(`/api/chats/${chatId}`, { headers: getAuthHeaders() });
    chats.value = chats.value.filter(chat => chat.id !== chatId);
    if (activeChat.value?.id === chatId) {
      activeChat.value = null;
      currentRouteId.value = null;
    }
  } catch (error) {
    console.error('Ошибка при удалении чата:', error);
  }
};

const selectChat = async (chat) => {
  activeChat.value = chat;
  chat.unreadCount = 0;
  currentRouteId.value = null;

  if (chat.messages.length === 0) await loadChatHistory(chat.id);
  
  await checkRouteExists(chat.id);

  if (stompClientConnection) {
    subscribeToChat(chat.id, (message) => {
      if (message.chat_id === activeChat.value.id) {
        const aiMsg = {
          id: message.id,
          type: 'ai',
          text: message.content,
          timestamp: new Date(message.send_date.replace(' ', 'T') + 'Z')
        };
        activeChat.value.messages.push(aiMsg);
        activeChat.value.lastMessage = aiMsg.text;
        activeChat.value.updatedAt = aiMsg.timestamp;
        loading.value = false;
      }
    });
  }

  if (isMobile.value) {
    sidebarOpen.value = false;
  }
};

const closeChat = () => {
  activeChat.value = null;
  currentRouteId.value = null;
  if (isMobile.value) {
    sidebarOpen.value = true;
  }
};

const createNewChat = () => {
  const tempId = Date.now();
  const newChat = {
    id: tempId,
    title: 'Новый чат',
    lastMessage: '',
    updatedAt: new Date(),
    unreadCount: 0,
    messages: []
  };
  chats.value.unshift(newChat);
  activeChat.value = newChat;
  currentRouteId.value = null;
  
  if (isMobile.value) {
    sidebarOpen.value = false;
  }
  
  return newChat;
};

const clearChat = () => {
  if (activeChat.value) {
    activeChat.value.messages = [];
    activeChat.value.lastMessage = 'Чат очищен';
    activeChat.value.updatedAt = new Date();
    currentRouteId.value = null;
  }
};

const exportChat = () => {
  if (activeChat.value) {
    const chatData = { 
      title: activeChat.value.title, 
      messages: activeChat.value.messages,
      routeId: currentRouteId.value
    };
    const dataStr = JSON.stringify(chatData, null, 2);
    const dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);
    const exportFileDefaultName = `chat_${activeChat.value.title}_${Date.now()}.json`;
    const linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
  }
};

const sendMessage = async (text) => {
  if (!text.trim() || loading.value || !activeChat.value) return;

  const userMsg = { id: Date.now(), type: 'user', text, timestamp: new Date() };
  activeChat.value.messages.push(userMsg);
  activeChat.value.lastMessage = text;
  activeChat.value.updatedAt = new Date();

  if (activeChat.value.id > 1000000) {
    loading.value = true;
    try {
      const response = await axios.post('/api/chats', { message: text }, { headers: getAuthHeaders() });
      const responseData = response.data;
      activeChat.value.id = responseData.id;
      activeChat.value.title = responseData.title || 'Новый маршрут';

      const aiMsg = {
        id: responseData.message.id,
        type: 'ai',
        text: responseData.message.content,
        timestamp: new Date(responseData.message.send_date.replace(' ', 'T') + 'Z')
      };
      activeChat.value.messages.push(aiMsg);
      activeChat.value.lastMessage = aiMsg.text;
      activeChat.value.updatedAt = aiMsg.timestamp;
      if (stompClientConnection) {
        subscribeToChat(activeChat.value.id, (message) => {
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
            loading.value = false;
          }
        })
      }
    } catch (error) {
      console.error('Error creating chat:', error);
      activeChat.value.messages.push({
        id: Date.now(),
        type: 'ai',
        text: 'Извините, произошла ошибка. Попробуйте еще раз.',
        timestamp: new Date(),
        isError: true
      });
    } finally {
      loading.value = false;
    }
  } else {
    loading.value = true;
    try {
      wsSendMessage(activeChat.value.id, text);
    } catch (error) {
      console.error('Error sending message via WebSocket:', error);
      loading.value = false;
      activeChat.value.messages.push({
        id: Date.now(),
        type: 'ai',
        text: 'Не удалось отправить сообщение. Проверьте подключение к интернету.',
        timestamp: new Date(),
        isError: true
      });
    }
  }
};

const handleSuggestion = (text) => {
  createNewChat();
  sendMessage(text);
};

const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768;
  if (!isMobile.value) {
    sidebarOpen.value = false;
  }
};

onMounted(() => {
  checkMobile();
  window.addEventListener('resize', checkMobile);
  loadChats();

  connect().then(client => {
    stompClientConnection = client;
  }).catch(error => {
    console.error('Failed to establish WebSocket connection on mount:', error);
  });
});

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile);
  disconnect();
});
</script>

<style scoped>
.chats-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #191919;
}

.chats-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding-top: 80px; 
}

.chats-container {
  flex: 1;
  display: flex;
  position: relative;
  overflow: hidden;
}

.chat-content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  background-color: #191919;
}

.sidebar-toggle-btn {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 30;
  background: #00ADB5;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
}

.sidebar-toggle-btn:active {
  background-color: #004d51;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

@media (min-width: 769px) {
  .sidebar-toggle-btn {
    display: none;
  }
}

</style>