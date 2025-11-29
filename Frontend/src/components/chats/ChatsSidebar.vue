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

        <div class="chat-info">
          <div class="chat-title">{{ chat.title }}</div>
          <div class="chat-preview">{{ chat.lastMessage }}</div>
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

</script>

<style scoped>
.chats-sidebar {
  width: 400px;
  border-right: 1px solid #404040;
  display: flex;
  flex-direction: column;
  background: #191919;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid #404040;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sidebar-header h2{
  color: white;
}

.new-chat-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #00ADB5;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
}

.new-chat-btn:hover {
  background: #004d51;
}

.chats-list {
  flex: 1;
  overflow-y: auto;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #404040;
  cursor: pointer;
  transition: background 0.3s;
}

.chat-item:hover {
  background: #252525;
}

.chat-item.active {
  background: #252525;
  border-right: 5px solid #00ADB5;
}


.chat-title {
  font-weight: 600;
  color: white;
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
