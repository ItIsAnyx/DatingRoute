<template>
  <section class="chat-area">
    <div class="chat-container">
      <div class="chat-header">
        <button class="back-btn" @click="$emit('back')">← Назад</button>

        <div class="chat-header-info">
          <span class="chat-avatar-small">AI</span>
          <span class="chat-title-main">{{ chat.title }}</span>
        </div>

        <div class="chat-actions">
          <button class="action-btn" >Очистить чат</button>
          <button class="action-btn" >Экспорт чата</button>
        </div>
      </div>

      <MessageList :messages="chat.messages" :user-initials="userInitials" />

      <MessageInput @send="$emit('send', $event)" />
    </div>
  </section>
</template>

<script setup>
import MessageList from './MessageList.vue'
import MessageInput from './MessageInput.vue'

defineProps({
  chat: Object,
  userInitials: String
})
</script>

<style scoped>
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #e5e7eb;
}

.back-btn {
  background: none;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  cursor: pointer;
  margin-right: 1rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
}

.chat-avatar-small {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.7rem;
  margin-right: 0.75rem;
}

.chat-title-main {
  font-weight: 600;
  color: #1f2937;
}

.chat-actions {
  margin-left: auto;
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  background: none;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  cursor: pointer;
  margin-right: 1rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
}

.action-btn:hover {
  background: #f3f4f6;
}
</style>
