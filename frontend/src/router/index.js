import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        title: 'Home'
      }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: {
        title: 'Sign In'
      }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
      meta: {
        title: 'Sign Up'
      }
    },
    {
      path: '/categories',
      name: 'categories',
      component: () => import('../views/CategoriesView.vue'),
      meta: {
        title: 'Categories'
      }
    }
  ]
})

router.beforeEach((to, prev) => {
  document.title = to.meta?.title ?? 'TODO App'

  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const auth = useAuthStore()

  if (!authRequired && auth.isLoggedIn()) {
    return prev
  } else if (authRequired && !auth.isLoggedIn()) {
    auth.returnUrl = to.fullPath
    return '/login'
  }
})

export default router
