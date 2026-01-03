<template>
  <aside class="chats-sidebar" :class="{ 'is-mobile': isMobile, 'is-open': sidebarOpen }">
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
        </div>
      </div>
    </div>

    <div v-if="contextMenuVisible" class="context-menu" :style="menuStyle" @click="hideContextMenu">
      <button @click.stop="handleDeleteChat">Удалить чат</button>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'

const props = defineProps({
  chats: Array,
  activeChat: Object,
  isMobile: Boolean,
  sidebarOpen: Boolean
})

const emit = defineEmits(['select', 'create', 'delete'])

const contextMenuVisible = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)
const selectedChatForDelete = ref(null)

const menuStyle = computed(() => {
  const rightEdge = window.innerWidth - 200; 
  const left = contextMenuX.value > rightEdge ? rightEdge : contextMenuX.value;
  return {
    top: `${contextMenuY.value}px`,
    left: `${left}px`
  }
})

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
.chats-sidebar {
  width: 400px;
  background: #191919;
  border-right: 1px solid #404040;
  display: flex;
  flex-direction: column;
  flex-shrink: 0; 
  position: relative;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid #404040;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.sidebar-header h2 {
  color: white;
  margin: 0;
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
  transition: background-color 0.2s;
  white-space: nowrap;
}

.new-chat-btn:active {
  background: #004d51;
}

.chats-list {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem 0;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  cursor: pointer;
  transition: background-color 0.2s;
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
  min-width: 0; 
}

.chat-title {
  font-weight: 600;
  color: white;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-preview {
  color: #6b7280;
  font-size: 0.875rem;
  margin-top: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

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


@media (max-width: 768px) {
  .chats-sidebar.is-mobile {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 85%;
    max-width: 320px;
    z-index: 20;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
    box-shadow: 2px 0 10px rgba(0,0,0,0.5);
  }

  .chats-sidebar.is-mobile.is-open {
    transform: translateX(0);
  }
  
  .sidebar-header {
    padding: 1rem;
  }
  
  .chat-item {
    padding: 0.75rem 1rem;
  }
  
  .chat-title {
    font-size: 0.9rem;
  }
  
  .chat-preview {
    font-size: 0.8rem;
  }
}
</style>