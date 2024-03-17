import { ref } from 'vue'
import { defineStore } from 'pinia'
import apiClient from '@/apiClient'
import { useUserStore } from '@/stores/user'
import { useCategoriesStore } from '@/stores/categories'
import { useTodoStore } from '@/stores/todos'

export const useAuthStore = defineStore('auth', () => {
  const userStore = useUserStore()
  const token = ref(localStorage.getItem('authToken'))

  function isLoggedIn() {
    return token.value !== null
  }

  // Function to handle user registration
  async function register(login, password) {
    const response = await apiClient.post('/api/auth/register', {
      login,
      password
    })

    token.value = response.data.token
    localStorage.setItem('authToken', token.value)
    userStore.fetchUserInfo()
  }

  // Function to handle user login
  async function login(login, password) {
    try {
      const response = await apiClient.post('/api/auth/getToken', {
        login,
        password
      })

      token.value = response.data.token
      localStorage.setItem('authToken', token.value)
      userStore.fetchUserInfo()
    } catch (error) {
      token.value = null
      localStorage.removeItem('authToken')
      throw error
    }
  }

  function logout() {
    const categoriesStore = useCategoriesStore()
    const todoStore = useTodoStore()

    userStore.value = null
    token.value = null
    localStorage.removeItem('authToken')

    categoriesStore.clearState()
    todoStore.clearState()
  }

  function getToken() {
    return token.value
  }

  return { getToken, isLoggedIn, register, login, logout }
})
