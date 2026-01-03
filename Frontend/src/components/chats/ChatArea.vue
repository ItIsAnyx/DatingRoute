<template>
  <section class="chat-area">
    <div class="chat-container">
      <div class="chat-header">
          <button class="action-btn delete-btn" @click="handleDeleteChat" title="Удалить чат">Удалить чат</button>
      </div>

      <MessageList :messages="chat.messages" :user-initials="userInitials" :loading="loading" />

      <MessageInput 
        @send="$emit('send', $event)" 
        @generate-points="$emit('generate-points')"
        @generate-route="$emit('generate-route')"
        :disabled="loading" 
        :has-route="hasRoute"
      />
    </div>
  </section>
</template>

<script setup>
import MessageList from './MessageList.vue'
import MessageInput from './MessageInput.vue'

const props = defineProps({
  chat: Object,
  userInitials: String,
  loading: Boolean,
  hasRoute: Boolean,
  isMobile: Boolean
})

const emit = defineEmits(['clear-chat', 'export-chat', 'delete-chat', 'send', 'generate-points', 'generate-route', 'back'])


const handleDeleteChat = () => {
  if (props.chat) {
    emit('delete-chat', props.chat.id)
  }
}
</script>

<style scoped>
.chat-area {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #191919;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden; 
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #808080;
  background: #191919;
  flex-shrink: 0;
}

.back-btn {
  margin-right: 1rem;
}

.chat-title-main {
  font-weight: 600;
  color: white;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.chat-actions {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

.action-btn {
display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #00ADB5;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.action-btn:active {
  background: #b50000;
}


</style>