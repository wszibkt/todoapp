<script setup>
import { RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useUserStore } from '@/stores/user'

const authStore = useAuthStore()
const userStore = useUserStore()
</script>

<template>
  <template v-if="!authStore.isLoggedIn()">
    <div id="login-app">
      <div class="landing-content">
        <img alt="logo" class="logo" src="@/assets/logo.svg" />
        <br />
        <RouterView />
      </div>
    </div>
  </template>
  <template v-else>
    <div id="app-wrapper">
      <div id="app-content">
        <header>
          <img alt="logo" class="logo" src="@/assets/logo.svg" />
          <div class="wrapper">
            <p v-if="userStore.userInfo.login">
              Welcome back, <b>{{ userStore.userInfo.login }}</b>
            </p>
            <nav>
              <RouterLink to="/">Todo list</RouterLink>
              <RouterLink to="/categories">Categories</RouterLink>
              <a href="#" @click="logout">Logout</a>
            </nav>
          </div>
        </header>

        <RouterView />
      </div>
    </div>
  </template>
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
  border-left: 1px solid var(--color-border);
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>

<script>
export default {
  data() {
    return {}
  },
  methods: {
    logout() {
      const authStore = useAuthStore()
      authStore.logout()
    }
  }
}
</script>
