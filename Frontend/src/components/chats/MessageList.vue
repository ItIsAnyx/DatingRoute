<template>
  <div class="messages-container" ref="container">
    <div
      v-for="msg in messages"
      :key="msg.id"
      class="message"
      :class="[msg.type, { 'error-message': msg.isError, 'points-message': msg.isPoints, 'route-message': msg.isRoute }]"
    >
      <div class="message-avatar" v-if="msg.type === 'ai' || msg.type === 'system'">
        <span v-if="msg.type === 'ai'">AI</span>
        <span v-else-if="msg.type === 'system'">📍</span>
      </div>
      
      <div class="message-content">
        <div class="message-text">{{ msg.text }}</div>
        <div v-if="msg.isRoute && msg.routeId" class="map-link">
          <router-link :to="`/map/${msg.routeId}`" class="map-button">Открыть на карте</router-link>
        </div>
        <div class="message-time">{{ formatTime(msg.timestamp) }}</div>
      </div>
          
      <div class="message-avatar" v-if="msg.type === 'user'">
        <span>{{ userInitials }}</span>
      </div>
    </div>
    
    <div v-if="loading" class="message ai">
      <div class="message-avatar"><span>AI</span></div>
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
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
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

.message.ai, .message.system {
  align-self: flex-start;
}

.message-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: #252525;
  color: rgb(145, 145, 145);
  border-radius: 50%;
  font-weight: 600;
  font-size: 0.8rem;
  flex-shrink: 0;
}

.message.user .message-avatar {
  background: #00ADB5;
  color: white;
}

.message-content {
  background: #303030;
  padding: 0.75rem 1rem;
  border-radius: 18px;
  position: relative;
}

.message.user .message-content {
  background: #00ADB5;
  color: white;
}

.message.system .message-content {
  background: #6366f1;
  color: white;
}

.message.error-message .message-content {
  background: #252525;
  color: rgb(255, 112, 112);
}

.message.points-message .message-content {
  background: #6366f1;
  color: white;
}

.message.route-message .message-content {
  background: #10b981;
  color: white;
}

.message-text {
  white-space: pre-line;
  word-wrap: break-word;
}

.message-time {
  font-size: 0.7rem;
  color: #9ca3af;
  margin-top: 0.25rem;
}

.message.user .message-time {
  color: rgba(255, 255, 255, 0.8);
}

.message.system .message-time,
.message.points-message .message-time,
.message.route-message .message-time {
  color: rgba(255, 255, 255, 0.8);
}

.map-link {
  margin-top: 0.5rem;
}

.map-button {
  display: inline-block;
  padding: 0.4rem 0.8rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  text-decoration: none;
  border-radius: 12px;
  transition: background-color 0.2s;
  font-size: 0.85rem;
}

.map-button:hover {
  background: rgba(255, 255, 255, 0.3);
}

.loading-content {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
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

.typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
.typing-indicator span:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-10px); }
}

@media (max-width: 768px) {
  .messages-container { padding: 0.75rem; gap: 0.75rem; }
  .message { max-width: 90%; }
  .message-avatar { width: 32px; height: 32px; font-size: 0.7rem; }
  .message-content { padding: 0.6rem 0.8rem; }
  .message-text { font-size: 0.9rem; }
  .message-time { font-size: 0.65rem; }
}

@media (max-width: 480px) {
  .messages-container { padding: 0.5rem; gap: 0.5rem; }
  .message { max-width: 95%; }
  .message-avatar { width: 28px; height: 28px; font-size: 0.6rem; }
  .message-content { padding: 0.5rem 0.7rem; }
  .message-text { font-size: 0.85rem; }
  .message-time { font-size: 0.6rem; }
}
</style>