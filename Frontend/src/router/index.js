// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AuthView from '../views/AuthView.vue'
import Chats from '../views/ChatsPage.vue'
import MapView from '../views/MapView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/auth',
    name: 'auth',
    component: AuthView
  },
  {
    path: '/chats',
    name: 'chats',
    component: Chats
  },
  {
    path: '/map/:routeId',
    name: 'map',
    component: MapView,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router