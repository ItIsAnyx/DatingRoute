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

      <div class="button-group">
        <button
          class="send-btn discussion-btn"
          @click="handleDiscussion"
          :disabled="!text.trim() || disabled"
        >
          Обсуждение
        </button>
        <button
          class="send-btn points-btn"
          @click="handlePoints"
          :disabled="disabled"
        >
          Точки
        </button>
        <button
          class="send-btn route-btn"
          @click="handleGenerateRoute"
          :disabled="!hasRoute || disabled"
        >
          Маршрут
        </button>
      </div>
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
  padding: 1rem;
  border-top: 1px solid #808080;
  background: #191919;
  flex-shrink: 0;
}

.input-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.message-input {
  width: 100%;
  border: none;
  border-radius: 12px;
  padding: 0.75rem;
  resize: none;
  font-size: 1rem;
  background: #252525;
  color: white;
  font-family: inherit;
}

.message-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(0, 173, 181, 0.5);
}

.message-input:disabled {
  background-color: #333;
  color: #6b7280;
  cursor: not-allowed;
}

.button-group {
  display: flex;
  gap: 0.5rem;
}

.send-btn {
  flex: 1;
  padding: 0.75rem;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.send-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.discussion-btn { background: #00ADB5; }
.discussion-btn:hover:not(:disabled) { background: #004d51; }

.points-btn { background: #6366f1; }
.points-btn:hover:not(:disabled) { background: #4f46e5; }

.route-btn { background: #10b981; }
.route-btn:hover:not(:disabled) { background: #059669; }

@media (max-width: 768px) {
  .input-area { padding: 0.75rem; }
  .message-input { font-size: 0.9rem; }
  .send-btn { font-size: 0.9rem; padding: 0.6rem; }
}

@media (max-width: 480px) {
  .input-area { padding: 0.5rem; }
  .message-input { font-size: 0.85rem; padding: 0.6rem; }
  .send-btn { font-size: 0.8rem; padding: 0.5rem; }
}

@media (max-width: 320px) {
  .button-group {
    flex-direction: column;
  }
}
</style>