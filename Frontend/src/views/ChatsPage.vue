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
          @send="sendMessage"
          @back="closeChat"
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
import HeaderComponent from '@/components/HeaderComponent.vue'
import ChatsSidebar from '@/components/chats/ChatsSidebar.vue'
import ChatArea from '@/components/chats/ChatArea.vue'
import EmptyChatView from '@/components/chats/EmptyChatView.vue'

const router = useRouter()
const activeChat = ref(null)
const newMessage = ref('')
const isMobile = ref(false)

const chats = ref([
  {
    id: 1,
    title: 'Романтический вечер',
    lastMessage: 'Маршрут построен: ресторан → набережная → смотровая площадка',
    updatedAt: new Date(),
    unreadCount: 0,
    messages: [
      { id: 1, type: 'user', text: 'Спланируй вечер...', timestamp: new Date() },
      { id: 2, type: 'ai', text: 'Вот маршрут...', timestamp: new Date() }
    ]
  }
])

const promptSuggestions = [
  'Создай маршрут для первого свидания',
  'Спланируй романтический пикник в парке',
  'Нужны идеи для активного свидания',
  'Вечер в культурном центре города'
]

const userInitials = computed(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.name ? user.name.charAt(0).toUpperCase() : 'U'
})

const isChatOpen = computed(() => isMobile.value && !!activeChat.value)

const selectChat = (chat) => {
  activeChat.value = chat
  chat.unreadCount = 0
}

const closeChat = () => (activeChat.value = null)

const createNewChat = () => {
  const newChat = {
    id: Date.now(),
    title: 'Новый чат',
    lastMessage: 'Чат только создан...',
    updatedAt: new Date(),
    unreadCount: 0,
    messages: []
  }
  chats.value.unshift(newChat)
  activeChat.value = newChat
}

const sendMessage = async (text) => {
  if (!text.trim()) return
  if (!activeChat.value) createNewChat()

  const msg = { id: Date.now(), type: 'user', text, timestamp: new Date() }
  activeChat.value.messages.push(msg)
  activeChat.value.lastMessage = text
  activeChat.value.updatedAt = new Date()

  await nextTick()
  // Имитируем ответ ИИ
  setTimeout(() => {
    const aiMsg = {
      id: Date.now() + 1,
      type: 'ai',
      text: 'Понял, создаю маршрут...',
      timestamp: new Date()
    }
    activeChat.value.messages.push(aiMsg)
  }, 1000)
}

const handleSuggestion = (text) => {
  createNewChat()
  sendMessage(text)
}

const checkMobile = () => (isMobile.value = window.innerWidth <= 768)

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
})
</script>

<style scoped>
.chats-page {
  min-height: 100vh;
  background: #f8fafc;
}

.chats-main {
  padding-top: 80px;
  min-height: calc(100vh - 80px);
}

.chats-container {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  height: calc(100vh - 80px);
  background: white;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}
</style>
