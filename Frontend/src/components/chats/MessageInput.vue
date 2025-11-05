<template>
  <div class="input-area">
    <div class="input-container">
      <textarea
        v-model="text"
        placeholder="Опишите ваш идеальный маршрут свидания..."
        class="message-input"
        @keydown.enter.exact.prevent="handleSend"
        rows="1"
      ></textarea>

      <button
        class="send-btn"
        @click="handleSend"
        :disabled="!text.trim()"
      >
        <span class="send-icon">↑</span>
      </button>
    </div>
    <div class="input-hints">
      <span class="hint">Например: "Романтический вечер в центре города..."</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const text = ref('')

const emit = defineEmits(['send'])

const handleSend = () => {
  if (!text.value.trim()) return
  emit('send', text.value)
  text.value = ''
}
</script>

<style scoped>
.input-area {
  padding: 1.5rem;
  border-top: 1px solid #e5e7eb;
}

.input-container {
  display: flex;
  align-items: flex-end;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.message-input {
  flex: 1;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 0.75rem 1rem;
  resize: none;
  font-size: 1rem;
}

.message-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.send-btn {
  width: 40px;
  height: 40px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.send-btn:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}

.send-icon {
  font-weight: bold;
  transform: rotate(45deg);
}

.input-hints {
  text-align: center;
}

.hint {
  font-size: 0.875rem;
  color: #6b7280;
}
</style>
