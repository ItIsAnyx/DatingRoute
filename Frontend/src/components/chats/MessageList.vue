<template>
  <div class="messages-container" ref="container">
    <div
      v-for="msg in messages"
      :key="msg.id"
      class="message"
      :class="[msg.type, { 'error-message': msg.isError }]"
    >
      <div class="message-content">
        <div class="message-text">{{ msg.text }}</div>
      </div>
    </div>
    
    <div v-if="loading" class="message ai">
      <div class="message-avatar">
        <span>AI</span>
      </div>
      <div class="message-content loading-content">
        <div class="typing-indicator">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'

const props = defineProps({
  messages: Array,
  userInitials: String,
  loading: Boolean
})

const container = ref(null)

const formatTime = (date) =>
  new Date(date).toLocaleTimeString('ru-RU', {
    hour: '2-digit',
    minute: '2-digit'
  })

watch(
  () => props.messages.length,
  async () => {
    await nextTick()
    if (container.value) {
      container.value.scrollTop = container.value.scrollHeight
    }
  }
)
</script>

<style scoped>
.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.message {
  display: flex;
  gap: 0.75rem;
  max-width: 80%;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message.ai {
  align-self: flex-start;
}

.message-content {
  background: #808080;
  padding: 0.75rem 1rem;
  border-radius: 12px;
}

.message.user .message-content {
  background: #00ADB5;
  color: white;
}

.message.error-message .message-content {
  background: #252525;
  color: rgb(255, 112, 112);
}

.message-time {
  font-size: 0.75rem;
  color: #9ca3af;
  margin-top: 0.25rem;
}

.message.user .message-time {
  color: rgba(255, 255, 255, 0.8);
}

.user-avatar-small {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: #252525;
  color: rgb(145, 145, 145);
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.7rem;
}

.loading-content {
  display: flex;
  align-items: center;
  padding: 1rem;
}

.typing-indicator {
  display: flex;
  gap: 4px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #00ADB5;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}
</style>