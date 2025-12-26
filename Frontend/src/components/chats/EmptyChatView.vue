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
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 1rem;
  text-align: center;
}

.welcome-message {
  color: white;
  max-width: 500px;
}

.welcome-message h3, .welcome-message p {
  margin: 0.5rem 0 1.5rem 0;
}

.prompt-suggestions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.prompt-suggestion {
  padding: 1rem;
  background: #252525;
  border: 1px solid #404040;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s, border-color 0.2s;
  text-align: left;
}

.prompt-suggestion:hover {
  background: #00ADB5;
  border-color: #00ADB5;
}

@media (max-width: 480px) {
  .welcome-message {
    max-width: 100%;
  }
  .welcome-message h3 {
    font-size: 1.2rem;
  }
  .welcome-message p {
    font-size: 0.9rem;
  }
  .prompt-suggestions {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }
  .prompt-suggestion {
    padding: 0.8rem;
    font-size: 0.9rem;
  }
}
</style>