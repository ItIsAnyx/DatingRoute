<template>
  <section class="chat-area">
    <div class="chat-container">
      <div class="chat-header">
        <button class="action-btn" @click="$emit('back')">← Назад</button>

        <div class="chat-header-info">
          <span class="chat-title-main">{{ chat.title }}</span>
        </div>

        <div class="chat-actions">
          <button class="action-btn" @click="clearChat">Очистить чат</button>
          <button class="action-btn" @click="exportChat">Экспорт чата</button>
          <button class="action-btn" @click="handleDeleteChat">Удалить чат</button>
        </div>
      </div>

      <MessageList :messages="chat.messages" :user-initials="userInitials" :loading="loading" />

      <MessageInput @send="$emit('send', $event)" :disabled="loading" />
    </div>
  </section>
</template>

<script setup>
import MessageList from './MessageList.vue'
import MessageInput from './MessageInput.vue'

const props = defineProps({
  chat: Object,
  userInitials: String,
  loading: Boolean
})

const emit = defineEmits(['clear-chat', 'export-chat', 'delete-chat', 'send'])

const clearChat = () => {
  emit('clear-chat')
}

const exportChat = () => {
  emit('export-chat')
}

const handleDeleteChat = () => {
  if (props.chat) {
    emit('delete-chat', props.chat.id)
  }
}
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
  border-bottom: 1px solid #808080;
}



.chat-title-main {
  font-weight: 600;
  color: white;
}

.chat-actions {
  margin-left: auto;
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  background: none;
  border: none;
  background: #252525;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  margin-right: 1rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  transition: 0.2s;
}

.action-btn:hover {
  background: #00ADB5;
  color: white;
}
</style>