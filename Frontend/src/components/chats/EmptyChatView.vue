<template>
  <div class="no-chat-selected">
    <div class="welcome-message">
      <h3>Добро пожаловать в чат-планировщик!</h3>
      <p>Опишите ваши пожелания для идеального отдыха, и ИИ создаст маршрут</p>

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
  color: white;
}
.welcome-message h3, .welcome-message p{
  margin: 15px;
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
  background: #252525;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
  text-align: left;
}

.prompt-suggestion:hover {
  background: #00ADB5;
}
</style>
