import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AuthView from '../views/AuthView.vue'
import Chats from '../views/ChatsPage.vue'
import MapView from '../views/MapView.vue'
import FeaturesView from '../views/FeaturesView.vue'
import AboutView from '../views/AboutView.vue'

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
  },
   {
    path: '/features',
    name: 'features',
    component: FeaturesView
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router