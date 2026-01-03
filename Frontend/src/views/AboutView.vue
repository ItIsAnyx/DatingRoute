<template>
  <HeaderComponent />
  <main class="about-page">
    <div class="container">
      <header class="page-header">
        <h1>О нас</h1>
        <p>Мы верим, что планирование отдыха должно приносить радость, а не стресс.</p>
      </header>

      <section class="about-content">
        <div class="about-text">
          <h2>Наша миссия</h2>
          <p>
            DatingRoute родился из простой идеи: помочь людям проводить больше времени вместе, а не тратить его на утомительную организацию. Мы знаем, как сложно согласовать планы, выбрать места и составить удобный маршрут, особенно когда хочется сделать всё идеально.
          </p>
          <p>
            Наша команда разработчиков и дизайнеров объединила усилия, чтобы создать умного помощника, который берет на себя всю рутину. Используя передовые технологии искусственного интеллекта, мы превращаем ваши желания в готовый план действий.
          </p>
        </div>
        <div class="about-image">
          <img src="https://placehold.co/600x400/191919/00ADB5?text=Типа+Фоточка+челы+улыбаются" alt="Команда DatingRoute">
        </div>
      </section>
      
      <section class="cta-section">
        <h2>Готовы начать?</h2>
        <p>Присоединяйтесь к тысячам пользователей, которые уже упростили себе жизнь с помощью DatingRoute.</p>
        <RouterLink to="/auth" class="cta-button">Попробовать бесплатно</RouterLink>
      </section>
    </div>
  </main>
</template>

<script setup>
  import HeaderComponent from '@/components/HeaderComponent.vue'
</script>

<style scoped>
.about-page {
  padding-top: 80px; 
  min-height: 100vh;
  background-color: #191919;
  color: white;
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

.about-content {
  display: flex;
  align-items: center;
  gap: 4rem;
  margin-bottom: 4rem;
}

.about-text {
  flex: 1;
}

.about-text h2 {
  font-size: 2rem;
  color: #ffffff;
  margin-bottom: 1.5rem;
}

.about-text p {
  font-size: 1rem;
  color: #a0aec0;
  line-height: 1.7;
  margin-bottom: 1.5rem;
}

.about-image {
  flex: 1;
}

.about-image img {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.cta-section {
  text-align: center;
  padding: 3rem 2rem;
  background: #252525;
  border-radius: 12px;
}

.cta-section h2 {
  font-size: 2rem;
  color: #ffffff;
  margin-bottom: 1rem;
}

.cta-section p {
  font-size: 1.1rem;
  color: #a0aec0;
  margin-bottom: 2rem;
}

.cta-button {
  display: inline-block;
  background: #00ADB5;
  color: white;
  padding: 1rem 2.5rem;
  border-radius: 50px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.1rem;
  transition: background-color 0.3s ease;
}

.cta-button:hover {
  background: #004d51;
}

@media (max-width: 1024px) {
  .about-content {
    flex-direction: column;
    gap: 2rem;
    text-align: center;
  }
  .about-image {
    order: -1; 
  }
}

@media (max-width: 768px) {
  .page-header h1, .cta-section h2 {
    font-size: 2rem;
  }
  .about-text h2 {
    font-size: 1.75rem;
  }
  .about-content {
    margin-bottom: 3rem;
  }
  .cta-section {
    padding: 2rem 1.5rem;
  }
}
</style>