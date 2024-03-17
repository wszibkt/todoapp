<script setup>
import { RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'
</script>

<template>
  <div class="register-container">
    <h2>Sign up</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="login">Login</label>
        <input type="text" name="login" id="login" v-model="login" required />
        <ul v-if="loginError" class="error-list">
          <li v-for="(error, index) in loginError" :key="index" class="error-message">
            {{ error }}
          </li>
        </ul>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" name="password" required />
        <ul v-if="passwordError" class="error-list">
          <li v-for="(error, index) in passwordError" :key="index" class="error-message">
            {{ error }}
          </li>
        </ul>
      </div>
      <div class="form-group">
        <label for="repassword">Re-enter Password</label>
        <input type="password" id="repassword" v-model="repassword" name="repassword" required />
        <ul v-if="repasswordError" class="error-list">
          <li v-for="(error, index) in repasswordError" :key="index" class="error-message">
            {{ error }}
          </li>
        </ul>
      </div>
      <p v-if="genericError" class="error-message">{{ genericError }}</p>
      <button type="submit" :disabled="loading">Register</button>
    </form>
    <p>Already have an account? <RouterLink to="/login">Login here</RouterLink></p>
  </div>
</template>
<script>
export default {
  name: 'LoginComponent',
  data() {
    return {
      login: '',
      password: '',
      repassword: '',
      loading: false,
      loginError: [],
      passwordError: [],
      repasswordError: [],
      genericError: null
    }
  },
  methods: {
    validateInput() {
      this.genericError = null
      this.loginError = []
      this.passwordError = []
      this.repasswordError = []
      let isValid = true

      if (this.login.length < 3) {
        this.loginError.push('Login must be at least 3 characters')
        isValid = false
      }

      if (this.password.length < 4) {
        this.passwordError.push('Password must be at least 4 characters')
        isValid = false
      }

      if (this.password != this.repassword) {
        this.repasswordError.push('Passwords must match')
        isValid = false
      }

      return isValid
    },
    async submitForm() {
      if (this.validateInput()) {
        this.loading = true
        const authStore = useAuthStore()
        try {
          await authStore.register(this.login, this.password)
          router.push({ name: 'home' })
        } catch (error) {
          if (error.response.status == 400) {
            if (typeof error?.response?.data === 'string') {
              this.genericError = error.response.data
            } else {
              if (error?.response?.data?.login) {
                this.loginError = error.response.data.login
              }
              if (error?.response?.data?.password) {
                this.passwordError = error.response.data.password
              }
            }
          } else {
            this.genericError = error.response.data
          }
        } finally {
          this.loading = false
        }
      }
    }
  }
}
</script>

<style></style>
