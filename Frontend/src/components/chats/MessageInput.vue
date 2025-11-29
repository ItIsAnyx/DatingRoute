<template>
  <div class="input-area">
    <div class="input-container">
      <textarea
        v-model="text"
        placeholder="Опишите ваш идеальный маршрут..."
        class="message-input"
        @keydown.enter.exact.prevent="handleSend"
        rows="1"
        :disabled="disabled"
      ></textarea>

      <button
        class="send-btn"
        @click="handleSend"
        :disabled="!text.trim() || disabled"
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

const props = defineProps({
  disabled: Boolean
})

const handleSend = () => {
  if (!text.value.trim() || props.disabled) return
  emit('send', text.value)
  text.value = ''
}
</script>

<style scoped>
.input-area {
  padding: 1.5rem;
  border-top: 1px solid #808080;
}

.input-container {
  display: flex;
  align-items: flex-end;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.message-input {
  flex: 1;
  border: none;
  border-radius: 12px;
  padding: 0.75rem 1rem;
  resize: none;
  font-size: 1rem;
  background: #252525;
  color: white;
}

.message-input:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.1);
}

.message-input:disabled {
  background-color: #f9fafb;
  color: #6b7280;
  cursor: not-allowed;
}

.send-btn {
  width: 40px;
  height: 40px;
  background: #00ADB5;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.send-btn:disabled {
  background: #356e72;
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
  color: #808080;
}
</style>