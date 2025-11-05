<template>
  <aside class="chats-sidebar">
    <div class="sidebar-header">
      <h2>Чаты</h2>
      <button class="new-chat-btn" @click="$emit('create')">
        <span class="plus-icon">+</span>
        Новый чат
      </button>
    </div>

    <div class="chats-list">
      <div
        v-for="chat in chats"
        :key="chat.id"
        class="chat-item"
        :class="{ active: activeChat?.id === chat.id }"
        @click="$emit('select', chat)"
      >
        <div class="chat-avatar"><span class="ai-avatar">AI</span></div>

        <div class="chat-info">
          <div class="chat-title">{{ chat.title }}</div>
          <div class="chat-preview">{{ chat.lastMessage }}</div>
          <div class="chat-time">{{ formatTime(chat.updatedAt) }}</div>
        </div>

        <div class="chat-status" v-if="chat.unreadCount > 0">
          <span class="unread-badge">{{ chat.unreadCount }}</span>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
defineProps({
  chats: Array,
  activeChat: Object
})

const formatTime = (date) => {
  const now = new Date()
  const diff = now - new Date(date)
  if (diff < 1000 * 60) return 'только что'
  if (diff < 1000 * 60 * 60) return `${Math.floor(diff / (1000 * 60))} мин`
  if (diff < 1000 * 60 * 60 * 24) return `${Math.floor(diff / (1000 * 60 * 60))} ч`
  return new Date(date).toLocaleDateString('ru-RU')
}
</script>

<style scoped>
.chats-sidebar {
  width: 400px;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  background: white;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.new-chat-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.new-chat-btn:hover {
  background: #5a67d8;
}

.chats-list {
  flex: 1;
  overflow-y: auto;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #f3f4f6;
  cursor: pointer;
  transition: background 0.3s;
}

.chat-item:hover {
  background: #f9fafb;
}

.chat-item.active {
  background: #eff6ff;
  border-right: 3px solid #667eea;
}

.ai-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 8px;
  font-weight: 600;
  font-size: 0.8rem;
  margin-right: 1rem;
}

.chat-title {
  font-weight: 600;
  color: #1f2937;
}

.chat-preview {
  color: #6b7280;
  font-size: 0.875rem;
}

.chat-time {
  color: #9ca3af;
  font-size: 0.75rem;
}

.unread-badge {
  background: #ef4444;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: 600;
}
</style>
