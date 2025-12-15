import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@auth/views/LoginView.vue'
import RegisterPriceView from '@pricing/views/RegisterPriceView.vue'
import HistoryView from '@pricing/views/HistoryView.vue'
import CompareListsView from '@comparison/views/CompareListsView.vue'
import ParametersView from '@config/views/ParametersView.vue'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { public: true },
  },
  {
    path: '/',
    component: () => import('@components/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'home',
        redirect: '/pricing/register',
      },
      {
        path: 'pricing/register',
        name: 'pricing-register',
        component: RegisterPriceView,
      },
      {
        path: 'pricing/history',
        name: 'pricing-history',
        component: HistoryView,
      },
      {
        path: 'comparison',
        name: 'comparison',
        component: CompareListsView,
      },
      {
        path: 'config/parameters',
        name: 'config-parameters',
        component: ParametersView,
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const isPublic = to.meta.public
  const isLoggedIn = !!localStorage.getItem('authToken') // o usa Pinia

  if (!isPublic && !isLoggedIn) {
    return next({ name: 'login' })
  }

  next()
})

export default router
