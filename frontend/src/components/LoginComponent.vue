<script setup>
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'
</script>

<template>
  <div class="login-container">
    <h2>Sign in</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="login">Login</label>
        <input type="text" id="login" v-model="login" required />
        <ul v-if="loginError" class="error-list">
          <li v-for="(error, index) in loginError" :key="index" class="error-message">
            {{ error }}
          </li>
        </ul>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" required />
        <ul v-if="passwordError" class="error-list">
          <li v-for="(error, index) in passwordError" :key="index" class="error-message">
            {{ error }}
          </li>
        </ul>
      </div>
      <p v-if="error" class="error-message">{{ error }}</p>
      <button type="submit" :disabled="loading">Login</button>
    </form>
    <p>Don't have an account? <RouterLink to="/register">Register here</RouterLink></p>
  </div>
</template>
<script>
export default {
  name: 'LoginComponent',
  data() {
    return {
      login: '',
      password: '',
      loginError: [],
      passwordError: [],
      loading: false,
      error: null
    }
  },
  methods: {
    async submitForm() {
      this.loading = true
      this.error = null
      this.loginError = null
      this.passwordError = null

      const authStore = useAuthStore()
      try {
        await authStore.login(this.login, this.password)
        router.push({ name: 'home' })
      } catch (error) {
        if (error.response.status == 400) {
          if (error?.response?.data?.login) {
            this.loginError = error.response.data.login
          }
          if (error?.response?.data?.password) {
            this.passwordError = error.response.data.password
          }
        } else {
          this.error = 'Failed to log in. Please check your credentials.'
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}

p {
  margin-top: 1rem;
}
</style>
