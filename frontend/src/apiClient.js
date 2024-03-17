import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL
})

apiClient.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()

    if (authStore.isLoggedIn()) {
      config.headers.Authorization = `Bearer ${authStore.getToken()}`
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)
apiClient.interceptors.request.use((config) => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(config), 250) // Delaying all requests by 2 seconds
  })
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()
      router.push('login')
    }
    return Promise.reject(error)
  }
)

export default apiClient
