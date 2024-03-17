import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { useUserStore } from '@/stores/user'
import { useAuthStore } from '@/stores/auth'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

const userStore = useUserStore()
const authStore = useAuthStore()

if (authStore.isLoggedIn()) {
  userStore.fetchUserInfo()
}

app.mount('#app')
