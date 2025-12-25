<template>
  <div class="input-area">
    <div class="input-container">
      <textarea
        v-model="text"
        placeholder="Опишите ваш идеальный маршрут..."
        class="message-input"
        @keydown.enter.exact.prevent="handleDiscussion"
        rows="1"
        :disabled="disabled"
      ></textarea>

      <button
        class="send-btn discussion-btn"
        @click="handleDiscussion"
        :disabled="!text.trim() || disabled"
      >
        <span class="send-icon">Обсуждение</span>
      </button>
      <button
        class="send-btn points-btn"
        @click="handlePoints"
        :disabled="disabled"
      >
        <span class="send-icon">Точки</span>
      </button>
      <button
        class="send-btn route-btn"
        @click="handleGenerateRoute"
        :disabled="!hasRoute || disabled"
      >
        <span class="send-icon">Сгенерировать маршрут</span>
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

const emit = defineEmits(['send', 'generate-points', 'generate-route'])

const props = defineProps({
  disabled: Boolean,
  hasRoute: Boolean
})

const handleDiscussion = () => {
  if (!text.value.trim() || props.disabled) return
  emit('send', text.value)
  text.value = ''
}

const handlePoints = () => {
  if (props.disabled) return
  emit('generate-points')
}

const handleGenerateRoute = () => {
  if (!props.hasRoute || props.disabled) return
  emit('generate-route')
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
  padding: 0.5rem 1rem;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: 0.2s;
}

.send-btn:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.discussion-btn {
  background: #00ADB5;
}

.discussion-btn:hover:not(:disabled) {
  background: #004d51;
}

.points-btn {
  background: #6366f1;
}

.points-btn:hover:not(:disabled) {
  background: #4f46e5;
}

.route-btn {
  background: #10b981;
}

.route-btn:hover:not(:disabled) {
  background: #059669;
}

.send-icon {
  font-weight: bold;
}

.input-hints {
  text-align: center;
}

.hint {
  font-size: 0.875rem;
  color: #808080;
}
</style>