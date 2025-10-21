<template>
  <div class="auth-page">
    <header class="header">
        <div class="logo" @click="goToHome()">DatingRoute</div>
    </header>

    <main class="main-content">
      <div class="container">
        <div class="auth-card">
          <div class="mode-switcher">
            <button 
              class="mode-btn" 
              :class="{ active: isLoginMode }"
              @click="setLoginMode"
            >
              Вход
            </button>
            <button 
              class="mode-btn" 
              :class="{ active: !isLoginMode }"
              @click="setRegisterMode"
            >
              Регистрация
            </button>
          </div>

          <div class="card-header">
            <h1 class="title">
              {{ isLoginMode ? 'Войдите в свой аккаунт' : 'Создайте свой аккаунт' }}
            </h1>
            <p class="subtitle">
              {{ isLoginMode 
                ? 'Пожалуйста, введите свои данные для входа.' 
                : 'Пожалуйста, введите свои данные для создания аккаунта.' 
              }}
            </p>
          </div>

          <form class="auth-form" @submit.prevent="handleSubmit">
            <div class="form-group" v-if="!isLoginMode">
              <label for="fullName" class="form-label">Полное имя</label>
              <input 
                type="text" 
                id="fullName"
                class="form-input"
                placeholder="Введите ваше полное имя"
                v-model="formData.fullName"
                required
              >
            </div>

            <div class="form-group">
              <label for="email" class="form-label">Электронная почта</label>
              <input 
                type="email" 
                id="email"
                class="form-input"
                placeholder="Введите вашу электронную почту"
                v-model="formData.email"
                required
              >
            </div>

            <div class="form-group">
              <label for="password" class="form-label">
                {{ isLoginMode ? 'Пароль' : 'Создайте пароль' }}
              </label>
              <input 
                type="password" 
                id="password"
                class="form-input"
                placeholder="Введите ваш пароль"
                v-model="formData.password"
                required
              >
            </div>

            <div class="form-group" v-if="!isLoginMode">
              <label for="confirmPassword" class="form-label">Подтвердите пароль</label>
              <input 
                type="password" 
                id="confirmPassword"
                class="form-input"
                placeholder="Повторите пароль"
                v-model="formData.confirmPassword"
                required
              >
            </div>

            <div class="forgot-password" v-if="isLoginMode">
              <a href="#" class="forgot-link">Забыли пароль?</a>
            </div>

            <div class="divider"></div>

            <button type="submit" class="btn-submit" :disabled="loading">
              <span v-if="!loading">
                {{ isLoginMode ? 'Войти' : 'Продолжить' }}
              </span>
              <span v-else class="loading">
                {{ isLoginMode ? 'Вход...' : 'Создание аккаунта...' }}
              </span>
            </button>

            <p class="auth-switch">
              {{ isLoginMode ? "Нет аккаунта?" : 'Уже есть аккаунт?' }}
              <a 
                href="#" 
                class="switch-link" 
                @click.prevent="isLoginMode ? setRegisterMode() : setLoginMode()"
              >
                {{ isLoginMode ? 'Зарегистрироваться' : 'Войти' }}
              </a>
            </p>
          </form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const isLoginMode = ref(true)
const loading = ref(false)

const goToHome = () => {
  router.push({
    path: '/'
  })
}

const formData = ref({
  fullName: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const setLoginMode = () => {
  isLoginMode.value = true
  resetForm()
}

const setRegisterMode = () => {
  isLoginMode.value = false
  resetForm()
}

const resetForm = () => {
  formData.value = {
    fullName: '',
    email: '',
    password: '',
    confirmPassword: ''
  }
}

const handleSubmit = async () => {
  loading.value = true
  
  await new Promise(resolve => setTimeout(resolve, 1500))
  
  if (isLoginMode.value) {
    console.log('Login data:', {
      email: formData.value.email,
      password: formData.value.password
    })
  } else {
    console.log('Registration data:', formData.value)
  }
  
  loading.value = false
}

</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header {
  display: flex;
  justify-content: center;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #e5e7eb;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 1rem 2rem;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
}

.header .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #667eea;
  cursor: pointer;
}

.nav {
  display: flex;
  align-items: center;
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

.btn-login,
.btn-register {
  padding: 0.5rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
}

.btn-login {
  border: 1px solid #667eea;
  background: transparent;
  color: #667eea;
}

.btn-login:hover,
.btn-login.active {
  background: #667eea;
  color: white;
}

.btn-register {
  border: none;
  background: #667eea;
  color: white;
}

.btn-register:hover,
.btn-register.active {
  background: #5a67d8;
}

.main-content {
  padding-top: 80px;
  min-height: calc(100vh - 80px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.auth-card {
  background: white;
  border-radius: 16px;
  padding: 2.5rem;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  max-width: 440px;
  width: 100%;
  margin: 2rem;
  position: relative;
}

.mode-switcher {
  display: none;
  background: #f7fafc;
  border-radius: 12px;
  padding: 4px;
  margin-bottom: 2rem;
  position: relative;
}

.mode-btn {
  flex: 1;
  padding: 0.75rem 1rem;
  border: none;
  background: transparent;
  color: #718096;
  font-weight: 500;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  z-index: 1;
  position: relative;
}

.mode-btn.active {
  color: #667eea;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  text-align: center;
  margin-bottom: 2rem;
}

.title {
  font-size: 1.75rem;
  font-weight: bold;
  color: #1a202c;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #718096;
  font-size: 0.95rem;
  line-height: 1.5;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-weight: 500;
  color: #2d3748;
  font-size: 0.9rem;
}

.form-input {
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s;
  background: white;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input::placeholder {
  color: #a0aec0;
}

.forgot-password {
  text-align: right;
  margin-top: -0.5rem;
}

.forgot-link {
  color: #667eea;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.3s;
}

.forgot-link:hover {
  color: #5a67d8;
  text-decoration: underline;
}

.divider {
  height: 1px;
  background: #e2e8f0;
  margin: 0.5rem 0 1rem;
}

.btn-submit {
  background: #667eea;
  color: white;
  border: none;
  padding: 1rem 2rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 0.5rem;
}

.btn-submit:hover:not(:disabled) {
  background: #5a67d8;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.loading {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.loading::after {
  content: '';
  width: 1rem;
  height: 1rem;
  border: 2px solid transparent;
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.auth-switch {
  text-align: center;
  color: #718096;
  margin-top: 1.5rem;
  font-size: 0.95rem;
}

.switch-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  margin-left: 0.5rem;
  transition: color 0.3s;
}

.switch-link:hover {
  color: #5a67d8;
  text-decoration: underline;
}

@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }

  .nav {
    display: none;
  }

  .auth-card {
    padding: 2rem;
    margin: 1rem;
  }

  .title {
    font-size: 1.5rem;
  }

  .main-content {
    padding-top: 70px;
  }

  .mode-switcher {
    display: flex;
  }

  .auth-buttons {
    display: none;
  }
}

@media (max-width: 480px) {
  .auth-card {
    padding: 1.5rem;
  }

  .title {
    font-size: 1.375rem;
  }

  .form-input {
    padding: 0.875rem 1rem;
  }

  .btn-submit {
    padding: 0.875rem 1.5rem;
  }

  .mode-btn {
    padding: 0.625rem 0.75rem;
    font-size: 0.9rem;
  }
}

@media (max-width: 768px) {
  .header .container {
    position: relative;
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

  .nav.active {
    right: 0;
  }

  .nav-link {
    padding: 0.75rem 0;
    font-size: 1.1rem;
    border-bottom: 1px solid #f1f1f1;
    width: 100%;
  }

  .auth-buttons {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    width: 100%;
    margin-top: 1rem;
  }

  .btn-login,
  .btn-register {
    width: 100%;
    text-align: center;
  }
}

@media (hover: hover) {
  .nav-link,
  .btn-login,
  .btn-register,
  .form-input,
  .btn-submit,
  .switch-link,
  .forgot-link,
  .mode-btn {
    transition: all 0.3s ease;
  }
}

@media (prefers-reduced-motion: reduce) {
  .nav,
  .btn-login,
  .btn-register,
  .form-input,
  .btn-submit,
  .switch-link,
  .forgot-link,
  .mode-btn {
    transition: none;
  }
  
  .loading::after {
    animation: none;
  }
}
</style>