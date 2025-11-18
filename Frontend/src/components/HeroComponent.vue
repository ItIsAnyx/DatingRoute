<template>
  <section class="hero-section">
    <div class="hero-container">
      <div class="hero-content">
        <h1>
          DatingRoute
        </h1>
        <h1>
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
          Приступим  <b>→</b>
        </button>
      </div>
      <div class="hero-image-container">
        <img :src="homeImage" alt="Планировщик маршрутов" class="hero-image">
      </div>
    </div>
  </section>
</template>

<script setup>
import homeImage from '@/assets/home.svg'
</script>

<style scoped>
.hero-section {
  margin: 0 auto;
  max-width: 1200px;
  min-height: 100vh;
  background: black;
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

.hero-content > h1{
  color: white;
  font-size: 3rem;
  line-height: 3.5rem;
}

.hero-content > h1:first-child{
  color: #00ADB5;
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
  color: rgba(255, 255, 255, 0.5);
  font-size: 1rem;
  margin: 1rem 0 0.5rem 0;
  opacity: 0.9;
  line-height: 1.6;
}

.hero-features {
  margin-bottom: 3rem;
}

.feature {
   color: rgba(255, 255, 255, 0.5);
  font-size: 1rem;
  margin-bottom: 0.5rem;
  opacity: 0.8;
  line-height: 1.5;
}

.cta-button {
  background: white;
  border: none;
  padding: 1rem 2.5rem;
  font-size: 1.1rem;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s;
  color: rgb(0, 0, 0);
}

.cta-button b{
  margin-left: 10px;
}

.cta-button:hover {
  background: #00ADB5;
  color: white;
}

@media (max-width: 1200px) {
  .hero-container {
    gap: 40px;
    padding: 0 100px;
  }

  
  .hero-content > h1{
    font-size: 2rem;
    line-height: 2.5rem;
  }

  .hero-content p{
    font-size: 0.8rem;
  }

  .cta-button {
    font-size: 1rem;
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
  
  .hero-container {
    padding: 0 20px;
    gap: 30px;
  }
  
  
  .hero-image {
    max-width: 300px;
  }
}

@media (max-width: 480px) {

  
  .hero-container {
    padding: 0 15px;
  }
  

}
</style>