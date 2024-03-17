import { defineStore } from 'pinia'
import apiClient from '@/apiClient'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: {
      login: null,
      id: null,
      role: null
    }
  }),
  getters: {},
  actions: {
    setUser(userInfo) {
      this.userInfo = userInfo
    },
    clearUser() {
      this.userInfo = { login: null, id: null, role: null }
    },
    async fetchUserInfo() {
      try {
        const response = await apiClient.get('/api/user')
        this.setUser(response.data)
      } catch (error) {
        console.error('Failed to fetch user info:', error)
        this.clearUser()
      }
    }
  }
})
