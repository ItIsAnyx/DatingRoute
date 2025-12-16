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
        @contextmenu.prevent="showContextMenu($event, chat)"
      >
        <div class="chat-info">
          <div class="chat-title">{{ chat.title }}</div>
          <div class="chat-preview">{{ chat.lastMessage || 'Пустой чат' }}</div>
        </div>
      </div>
    </div>

    <!-- Контекстное меню для удаления -->
    <div v-if="contextMenuVisible" class="context-menu" :style="{ top: contextMenuY + 'px', left: contextMenuX + 'px' }" @click="hideContextMenu">
      <button @click.stop="handleDeleteChat">Удалить чат</button>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  chats: Array,
  activeChat: Object
})

const emit = defineEmits(['select', 'create', 'delete'])

const contextMenuVisible = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)
const selectedChatForDelete = ref(null)

const showContextMenu = (event, chat) => {
  contextMenuVisible.value = true
  contextMenuX.value = event.clientX
  contextMenuY.value = event.clientY
  selectedChatForDelete.value = chat
}

const hideContextMenu = () => {
  contextMenuVisible.value = false
  selectedChatForDelete.value = null
}

const handleDeleteChat = () => {
  if (selectedChatForDelete.value) {
    emit('delete', selectedChatForDelete.value.id)
  }
  hideContextMenu()
}

const handleClickOutside = (event) => {
  if (!event.target.closest('.context-menu')) {
    hideContextMenu()
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
/* ... (ваши существующие стили) ... */
.chats-sidebar {
  width: 400px;
  border-right: 1px solid #404040;
  display: flex;
  flex-direction: column;
  background: #191919;
  position: relative; /* Для позиционирования контекстного меню */
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

.chat-info {
  flex: 1;
}

.chat-title {
  font-weight: 600;
  color: white;
}

.chat-preview {
  color: #6b7280;
  font-size: 0.875rem;
  margin-top: 4px;
}

/* Стили для контекстного меню */
.context-menu {
  position: fixed;
  background: #252525;
  border: 1px solid #404040;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.context-menu button {
  width: 100%;
  padding: 0.5rem 1rem;
  border: none;
  background: none;
  color: white;
  text-align: left;
  cursor: pointer;
  border-radius: 6px;
}

.context-menu button:hover {
  background: #00ADB5;
}
</style>