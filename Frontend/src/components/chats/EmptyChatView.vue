<template>
  <div class="no-chat-selected">
    <div class="welcome-message">
      <div class="welcome-icon">💬</div>
      <h3>Добро пожаловать в чат-планировщик!</h3>
      <p>Опишите ваши пожелания для идеального свидания, и ИИ создаст маршрут</p>

      <div class="prompt-suggestions">
        <div
          v-for="s in promptSuggestions"
          :key="s"
          class="prompt-suggestion"
          @click="$emit('select-suggestion', s)"
        >
          {{ s }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  promptSuggestions: Array
})
</script>

<style scoped>
.no-chat-selected {
    margin: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 2rem;
}

.welcome-message {
  text-align: center;
  max-width: 500px;
}

.welcome-icon {
  font-size: 4rem;
  margin-bottom: 1.5rem;
}

.prompt-suggestions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.75rem;
}

.prompt-suggestion {
  padding: 1rem;
  background: #f8fafc;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: left;
}

.prompt-suggestion:hover {
  background: #667eea;
  color: white;
  border-color: #667eea;
  transform: translateY(-2px);
}
</style>
