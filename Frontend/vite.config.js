import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  define: {
    // Определяем `global` для браузера, чтобы библиотеки не ломались
    global: 'globalThis',
  },
  preview: {
    port: 8080
  }, 
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': process.env.BACKEND_API_URL || 'http://localhost:8081'
    }
  }
})