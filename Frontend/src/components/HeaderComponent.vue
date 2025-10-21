<template>
  <header class="header">
    <div class="container">
      <div class="logo">DatingRoute</div>

      <input type="checkbox" id="menu-toggle" class="menu-toggle">
      <label for="menu-toggle" class="menu-button">
        <span></span>
        <span></span>
        <span></span>
      </label>

      <nav class="nav">
        <div class="nav-links">
          <a href="#" class="nav-link">Чаты</a>
          <a href="#" class="nav-link">Возможности</a>
          <a href="#" class="nav-link">О нас</a>
        </div>
        <div class="auth-buttons">
          <button class="btn-login" @click="goToAuth('login')">Вход</button>
          <button class="btn-register" @click="goToAuth('register')">Регистрация</button>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToAuth = (mode) => {
  router.push({
    path: '/auth',
    query: { mode }
  })
}

</script>

<style scoped>
.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #e5e7eb;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}



.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #667eea;
  z-index: 1001;
  cursor: pointer;
}

.nav {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  text-decoration: none;
  color: #4a5568;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-link:hover {
  color: #667eea;
}

.auth-buttons {
  display: flex;
  gap: 1rem;
}

.btn-login {
  padding: 0.5rem 1.5rem;
  border: 1px solid #667eea;
  background: transparent;
  color: #667eea;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-login:hover {
  background: #667eea;
  color: white;
}

.btn-register {
  padding: 0.5rem 1.5rem;
  border: none;
  background: #667eea;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-register:hover {
  background: #5a67d8;
}

.menu-toggle {
  display: none;
}

.menu-button {
  display: none;
  flex-direction: column;
  justify-content: space-between;
  width: 24px;
  height: 18px;
  cursor: pointer;
  z-index: 1001;
}

.menu-button span {
  display: block;
  height: 2px;
  width: 100%;
  background-color: #4a5568;
  transition: all 0.3s ease;
  transform-origin: center;
}

@media (max-width: 968px) {
  .nav {
    gap: 1.5rem;
  }

  .nav-links {
    gap: 1.5rem;
  }

  .auth-buttons {
    gap: 0.75rem;
  }

  .btn-login,
  .btn-register {
    padding: 0.5rem 1.25rem;
    font-size: 0.9rem;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }

  .menu-button {
    display: flex;
  }

  .nav {
    position: fixed;
    top: 0;
    right: -100%;
    width: 80%;
    max-width: 300px;
    height: 100vh;
    background: white;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
    padding: 80px 2rem 2rem;
    gap: 0;
    transition: right 0.3s ease;
    box-shadow: -5px 0 15px rgba(0, 0, 0, 0.1);
  }

  .nav-links {
    flex-direction: column;
    gap: 1.5rem;
    width: 100%;
    margin-bottom: 2rem;
  }

  .nav-link {
    padding: 0.75rem 0;
    font-size: 1.1rem;
    border-bottom: 1px solid #f1f1f1;
    width: 100%;
  }

  .auth-buttons {
    flex-direction: column;
    gap: 1rem;
    width: 100%;
  }

  .btn-login,
  .btn-register {
    width: 100%;
    text-align: center;
    padding: 0.75rem 1.5rem;
  }

  .menu-toggle:checked~.nav {
    right: 0;
  }

  .menu-toggle:checked~.menu-button span:nth-child(1) {
    transform: rotate(45deg) translate(6px, 6px);
  }

  .menu-toggle:checked~.menu-button span:nth-child(2) {
    opacity: 0;
  }

  .menu-toggle:checked~.menu-button span:nth-child(3) {
    transform: rotate(-45deg) translate(6px, -6px);
  }
}

@media (max-width: 480px) {
  .logo {
    font-size: 1.25rem;
  }

  .container {
    padding: 0.75rem 1rem;
  }

  .nav {
    width: 100%;
    max-width: none;
    padding: 70px 1.5rem 1.5rem;
  }
}

@media (hover: hover) {

  .nav-link,
  .btn-login,
  .btn-register {
    transition: all 0.3s ease;
  }
}

@media (prefers-reduced-motion: reduce) {

  .nav,
  .menu-button span,
  .nav-link,
  .btn-login,
  .btn-register {
    transition: none;
  }
}
</style>