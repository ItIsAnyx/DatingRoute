<template>
  <div class="messages-container" ref="container">
    <div
      v-for="msg in messages"
      :key="msg.id"
      class="message"
      :class="msg.type"
    >
      <div class="message-avatar">
        <span v-if="msg.type === 'ai'">AI</span>
        <span v-else class="user-avatar-small">{{ userInitials }}</span>
      </div>
      <div class="message-content">
        <div class="message-text">{{ msg.text }}</div>
        <div class="message-time">{{ formatTime(msg.timestamp) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'

const props = defineProps({
  messages: Array,
  userInitials: String
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
  background: #f8fafc;
  padding: 0.75rem 1rem;
  border-radius: 12px;
}

.message.user .message-content {
  background: #667eea;
  color: white;
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
  background: #10b981;
  color: white;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.7rem;
}
</style>
