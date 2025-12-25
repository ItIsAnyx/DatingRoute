<template>
  <div class="map-page">
    <HeaderComponent />
    
    <main class="map-main">
      <div class="map-container">
        <div class="map-header">
          <button class="back-btn" @click="$router.go(-1)">
            ← Назад
          </button>
          <h2>Интерактивный планировщик маршрутов</h2>
          <div class="route-actions">
            <button 
              class="action-btn" 
              @click="refreshRoute"
              :disabled="loading"
            >
              Обновить маршрут
            </button>
            <!-- Кнопка смены режима временно убрана, т.к. не поддерживается бэкендом -->
            <button 
              class="action-btn" 
              @click="exportRoute"
              :disabled="loading"
            >
              Экспортировать
            </button>
          </div>
        </div>
        
        <div class="map-content">
          <div id="map" class="map-wrapper"></div>
          
          <!-- Индикатор загрузки -->
          <div v-if="loading" class="map-loader">
            <div class="loader-circle"></div>
            <p>Загрузка маршрута...</p>
          </div>
          
          <!-- Блок с ошибкой -->
          <div v-if="error" class="map-error">
            <p>{{ error }}</p>
            <button @click="refreshRoute" class="retry-btn">Попробовать снова</button>
          </div>
          
          <!-- Информационная панель с точками маршрута -->
          <div v-if="routeData && routeData.points" class="route-info">
            <h3>Точки маршрута</h3>
            <div class="route-points">
              <div 
                v-for="(point, index) in routeData.points" 
                :key="index" 
                class="route-point"
              >
                <span class="point-number">{{ index + 1 }}</span>
                <span class="point-name">{{ point.name || `Точка ${index + 1}` }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import HeaderComponent from '@/components/HeaderComponent.vue'

// Получаем ID маршрута из URL
const route = useRoute()
const routeId = route.params.routeId

// Состояние компонента
const loading = ref(true)
const error = ref(null)
const routeData = ref(null)

// Глобальные переменные для Яндекс.Карт
let map = null
let multiRoute = null
let ymapsLoaded = false

// --- Функции для работы с аутентификацией ---
const getAuthToken = () => localStorage.getItem('access_token');
const getAuthHeaders = () => {
  const token = getAuthToken();
  return token ? { 
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  } : { 'Content-Type': 'application/json' };
};

// --- Функции для работы с Яндекс.Картами ---

// Асинхронная загрузка API Яндекс.Карт
const loadYandexMaps = () => {
  return new Promise((resolve, reject) => {
    if (window.ymaps) {
      ymapsLoaded = true
      resolve(window.ymaps)
      return
    }
    
    fetch('/api/config/maps-key', { headers: getAuthHeaders() })
      .then(response => {
        if (!response.ok) {
          throw new Error(`Ошибка загрузки ключа API: ${response.statusText}`);
        }
        return response.json();
      })
      .then(({ key }) => {
        const script = document.createElement('script')
        script.src = `https://api-maps.yandex.ru/2.1/?apikey=${key}&lang=ru_RU`
        script.onload = () => {
          window.ymaps.ready(() => {
            ymapsLoaded = true
            resolve(window.ymaps)
          })
        }
        script.onerror = () => reject(new Error('Не удалось загрузить скрипт Яндекс.Карт'))
        document.head.appendChild(script)
      })
      .catch(err => reject(new Error('Ошибка при запросе ключа API: ' + err.message)))
  })
}

// Инициализация карты после загрузки API
const initMap = () => {
  if (!ymapsLoaded || !document.getElementById('map')) return
  
  map = new window.ymaps.Map('map', {
    center: [55.751574, 37.573856], // Москва по умолчанию
    zoom: 11,
    controls: ['zoomControl', 'fullscreenControl']
  })
  
  loadRoute()
}

// Загрузка данных маршрута с бэкенда
const loadRoute = async () => {
  if (!routeId) {
    error.value = 'ID маршрута не указан в URL.'
    loading.value = false
    return
  }
  
  loading.value = true
  error.value = null
  
  try {
    // --- ГЛАВНОЕ ИЗМЕНЕНИЕ ЗДЕСЬ ---
    // Явно указываем метод POST, как требует спецификация API
    const response = await fetch(`/api/routes/${routeId}/build`, { 
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({}) // Тело запроса может быть пустым
    });
    // --- КОНЕЦ ИЗМЕНЕНИЯ ---

    if (!response.ok) {
      let errorText = `Ошибка ${response.status}`;
      try {
        const errJson = await response.json();
        errorText += `: ${errJson.message || errJson.error || 'Неизвестная ошибка сервера'}`;
      } catch {
        errorText += `: ${response.statusText}`;
      }
      throw new Error(errorText);
    }
    
    const data = await response.json()
    routeData.value = data
    buildRoute(data)
    
  } catch (err) {
    console.error('Ошибка при загрузке маршрута:', err)
    error.value = 'Не удалось загрузить маршрут: ' + err.message
  } finally {
    loading.value = false
  }
}

// Построение маршрута на карте
const buildRoute = (data) => {
  if (!map || !data || !data.points || data.points.length === 0) {
    console.warn('Нет данных для построения маршрута')
    error.value = 'Нет данных для построения маршрута.'
    return
  }
  
  if (multiRoute) {
    map.geoObjects.remove(multiRoute)
  }
  
  const referencePoints = data.points.map(p => {
    if (p.coords && p.coords.length >= 2) {
      return p.coords.join(',')
    }
    return p.name || p.request || ''
  })
  
  multiRoute = new window.ymaps.multiRouter.MultiRoute(
    {
      referencePoints: referencePoints,
      params: {
        routingMode: "pedestrian", 
        avoidTrafficJam: true
      }
    },
    {
      boundsAutoApply: true,
      routeStrokeColor: "#00ADB5",
      routeActiveStrokeColor: "#004d51",
      routeStrokeWidth: 4,
      routeActiveStrokeWidth: 6,
      pinIconFillColor: "#00ADB5",
    }
  );

  map.geoObjects.add(multiRoute)
}

// --- Функции-обработчики для кнопок ---

const refreshRoute = () => {
  loadRoute()
}

// Экспорт маршрута
const exportRoute = () => {
  if (!routeId) return
  
  const element = document.createElement('a')
  element.setAttribute('href', `/api/routes/${routeId}/export`)
  element.setAttribute('download', `route_${routeId}.json`)
  element.style.display = 'none'
  document.body.appendChild(element)
  element.click()
  document.body.removeChild(element)
}

// --- Жизненный цикл компонента ---

onMounted(async () => {
  try {
    await loadYandexMaps()
    initMap()
  } catch (err) {
    console.error('Критическая ошибка инициализации карты:', err)
    error.value = 'Не удалось инициализировать карту: ' + err.message
    loading.value = false
  }
})

onUnmounted(() => {
  if (map) {
    map.destroy()
  }
})
</script>

<style scoped>
/* Стили остаются без изменений */
.map-page {
  min-height: 100vh;
  background: #191919;
}

.map-main {
  padding-top: 69px;
  min-height: calc(100vh - 69px);
}

.map-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 69px);
  background: #191919;
}

.map-header {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #808080;
  background: #252525;
}

.back-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  margin-right: 1rem;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: background 0.2s;
}

.back-btn:hover {
  background: #00ADB5;
}

.map-header h2 {
  color: white;
  margin: 0;
  flex-grow: 1;
}

.route-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  background: #00ADB5;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}

.action-btn:hover:not(:disabled) {
  background: #004d51;
}

.action-btn:disabled {
  background: #356e72;
  cursor: not-allowed;
}

.map-content {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.map-wrapper {
  width: 100%;
  height: 100%;
}

.map-loader {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  color: white;
}

.loader-circle {
  border: 5px solid #f3f3f3;
  border-top: 5px solid #00ADB5;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.map-error {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #252525;
  padding: 1.5rem;
  border-radius: 8px;
  text-align: center;
  color: white;
  max-width: 80%;
  z-index: 1000;
}

.retry-btn {
  background: #00ADB5;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 1rem;
}

.route-info {
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  background: rgba(37, 37, 37, 0.9);
  padding: 1rem;
  border-radius: 8px;
  max-width: 300px;
  color: white;
  z-index: 1000;
}

.route-info h3 {
  margin: 0 0 1rem 0;
  color: #00ADB5;
}

.route-points {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.route-point {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.point-number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: #00ADB5;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  font-size: 0.8rem;
  flex-shrink: 0;
}

.point-name {
  flex: 1;
}
</style>