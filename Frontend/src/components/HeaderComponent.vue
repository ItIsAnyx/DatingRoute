<template>
  <header class="header">
    <div class="container">
      <div class="logo" @click="goToHome">DatingRoute</div>

      <input type="checkbox" id="menu-toggle" class="menu-toggle">
      <label for="menu-toggle" class="menu-button">
        <span></span>
        <span></span>
        <span></span>
      </label>

      <nav class="nav">
        <div class="nav-links">
          <RouterLink 
            v-if="isAuthenticated"
            to="/chats"
            class="nav-link"
            :class="{ active: isActive('/chats') }"
          >
            Чаты
          </RouterLink>

          <RouterLink 
            to="/planner"
            class="nav-link"
            :class="{ active: isActive('/planner') }"
          >
            Планировщик
          </RouterLink>
          <a href="#" class="nav-link">Возможности</a>
          <a href="#" class="nav-link">О нас</a>
        </div>

        <div v-if="isAuthenticated" class="user-menu">
          <div class="user-avatar">{{ userInitials }}</div>
          <button class="btn-logout" @click="logout">Выйти</button>
        </div>

        <div v-else class="auth-buttons">
          <button class="btn-login" @click="goToAuth('login')">Вход</button>
          <button class="btn-register" @click="goToAuth('register')">Регистрация</button>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const isAuthenticated = computed(() => !!localStorage.getItem('access_token'))

const userInitials = computed(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  return user.name ? user.name.charAt(0).toUpperCase() : 'U'
})

const goToHome = () => router.push('/')
const goToAuth = (mode) => router.push({ path: '/auth', query: { mode } })
const isActive = (path) => route.path.startsWith(path)

const logout = () => {
  localStorage.removeItem('access_token')
  localStorage.removeItem('refresh_token')
  localStorage.removeItem('user')
  router.push('/auth?mode=login')
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

.nav-link:hover,
.nav-link.active {
  color: #667eea;
}

.auth-buttons {
  display: flex;
  gap: 1rem;
}

.btn-login {
  padding: 0.5rem 1.25rem;
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
  padding: 0.5rem 1.25rem;
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

.user-menu {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: #10b981;
  color: white;
  border-radius: 50%;
  font-weight: 600;
  font-size: 0.8rem;
}

.btn-logout {
  padding: 0.5rem 1rem;
  border: 1px solid #e5e7eb;
  background: transparent;
  color: #6b7280;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-logout:hover {
  background: #ef4444;
  color: white;
  border-color: #ef4444;
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

@media (max-width: 768px) {
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

  .auth-buttons,
  .user-menu {
    flex-direction: column;
    width: 100%;
    gap: 1rem;
  }

  .btn-login,
  .btn-register,
  .btn-logout {
    width: 100%;
    text-align: center;
    padding: 0.75rem 1.5rem;
  }

  .menu-toggle:checked ~ .nav {
    right: 0;
  }

  .menu-toggle:checked ~ .menu-button span:nth-child(1) {
    transform: rotate(45deg) translate(6px, 6px);
  }

  .menu-toggle:checked ~ .menu-button span:nth-child(2) {
    opacity: 0;
  }

  .menu-toggle:checked ~ .menu-button span:nth-child(3) {
    transform: rotate(-45deg) translate(6px, -6px);
  }
}
</style>
