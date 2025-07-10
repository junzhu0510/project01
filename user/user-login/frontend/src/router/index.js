import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/forgot-password',
      name: 'forgotPassword',
      component: () => import('../views/ForgotPasswordView.vue')
    },
    {
      path: '/reset-password',
      name: 'resetPassword',
      component: () => import('../views/ResetPasswordView.vue')
    },
    {
      path: '/lottery',
      name: 'lottery',
      component: () => import('../views/LotteryView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/prize-history',
      name: 'prizeHistory',
      component: () => import('../views/PrizeHistoryView.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router