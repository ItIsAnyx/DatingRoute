<template>
      <HeaderComponent />
  <main class="features-page">
    <div class="container">
      <header class="page-header">
        <h1>Возможности</h1>
        <p>Узнайте, как DatingRoute делает планирование отдыха простым и увлекательным.</p>
      </header>

      <section class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">🤖</div>
          <h3>Умный ИИ-планировщик</h3>
          <p>Наш искусственный интеллект анализирует ваши пожелания и создает оптимальный маршрут, экономя ваше время и силы.</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">⏱️</div>
          <h3>Тайминг под контроль</h3>
          <p>Планируйте, сколько времени вы хотите провести в каждом месте. Приложение поможет составить реалистичный график.</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">🗺️</div>
          <h3>Интеграция с Яндекс Картами</h3>
          <p>Все маршруты строятся на точных и подробных картах. Легко прокладывайте путь и получайте информацию о пробках.</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">🔄</div>
          <h3>Альтернативные маршруты</h3>
          <p>Если какая-то точка оказалась недоступной, ИИ мгновенно предложит ближайшую альтернативу, не нарушая ваш план.</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">💬</div>
          <h3>Интерактивный чат</h3>
          <p>Планируйте маршрут в удобном формате чата. Просто опишите свои желания, а ИИ сделает всю остальную работу.</p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">👥</div>
          <h3>Идеально для компаний</h3>
          <p>Неважно, планируете ли вы свидание, прогулку с друзьями или семейный отдых — наше приложение подойдет для любой компании.</p>
        </div>
      </section>
    </div>
  </main>
</template>

<script setup>
import HeaderComponent from '@/components/HeaderComponent.vue'
</script>

<style scoped>
.features-page {
  padding-top: 80px; 
  min-height: 100vh;
  background-color: #191919;
  color: white;
  padding-bottom: 4rem;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.page-header {
  text-align: center;
  margin-bottom: 4rem;
}

.page-header h1 {
  font-size: 2.5rem;
  font-weight: bold;
  color: #00ADB5;
  margin-bottom: 1rem;
}

.page-header p {
  font-size: 1.1rem;
  color: #a0aec0;
  max-width: 600px;
  margin: 0 auto;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: #252525;
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: 1px solid #333;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 20px rgba(0, 173, 181, 0.2);
  border-color: #00ADB5;
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.feature-card h3 {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.75rem;
  color: #ffffff;
}

.feature-card p {
  color: #a0aec0;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 2rem;
  }
  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  .feature-card {
    padding: 1.5rem;
  }
}
</style>