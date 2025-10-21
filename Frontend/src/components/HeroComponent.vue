<template>
  <section class="hero-section">
    <div class="hero-container">
      <div class="hero-content">
        <h1 class="hero-title">
          Планировать Отдых Легко
        </h1>
        <p class="hero-subtitle">
          ИИ-планировщик маршрутов, преобразующий ваши пожелания по локациям в оптимизированные маршруты.
        </p>
        <div class="hero-features">
          <p class="feature">Просто назовите места - ИИ создаст идеальный маршрут с умным таймингом.</p>
          <p class="feature">От простых встреч до особых свиданий - мы оптимизируем социальную навигацию.</p>
          <p class="feature">Ваши желания + наш ИИ = идеально спланированный отдых.</p>
        </div>
        <button class="cta-button">
          Приступим →
        </button>
      </div>
      <div class="hero-image-container">
        <img :src="homeImage" alt="Планировщик маршрутов" class="hero-image">
      </div>
    </div>
  </section>
</template>

<script setup>
import homeImage from '@/assets/home.png'
</script>

<style scoped>
.hero-section {
  margin: 0 auto;
  max-width: 1200px;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0 40px; 
}

.hero-container {
  width: 100%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 60px;
  padding: 0 40px;
}

.hero-content {
  flex: 1;
  min-width: 0; 
  color: white;
}

.hero-image-container {
  flex: 0 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.hero-image {
  max-width: 700px;
  width: 100%;
  height: auto;
  display: block;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 1.25rem;
  margin-bottom: 2rem;
  opacity: 0.9;
  line-height: 1.6;
}

.hero-features {
  margin-bottom: 3rem;
}

.feature {
  font-size: 1.1rem;
  margin-bottom: 1rem;
  opacity: 0.8;
  line-height: 1.5;
}

.cta-button {
  background: #ff6b6b;
  color: white;
  border: none;
  padding: 1rem 2.5rem;
  font-size: 1.1rem;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 600;
}

.cta-button:hover {
  background: #ff5252;
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(255, 107, 107, 0.3);
}

@media (max-width: 1200px) {
  .hero-container {
    gap: 40px;
    padding: 0 30px;
  }
  
  .hero-title {
    font-size: 3rem;
  }
}

@media (max-width: 1024px) {
  .hero-container {
    flex-direction: column;
    text-align: center;
    gap: 40px;
  }
  
  .hero-content {
    order: 1;
  }
  
  .hero-image-container {
    order: 2;
  }
  
  .hero-image {
    max-width: 400px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 60px 0 30px;
  }
  
  .hero-container {
    padding: 0 20px;
    gap: 30px;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-subtitle {
    font-size: 1.1rem;
  }
  
  .hero-image {
    max-width: 300px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-container {
    padding: 0 15px;
  }
  
  .cta-button {
    padding: 0.875rem 2rem;
    font-size: 1rem;
  }
}
</style>