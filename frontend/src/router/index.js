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
        meta: { requiresAuth: true }
      },
      {
        path: 'pricing/history',
        name: 'pricing-history',
        component: HistoryView,
        meta: { requiresAuth: true }
      },
      {
        path: 'comparison',
        name: 'comparison',
        component: CompareListsView,
        meta: { requiresAuth: true }
      },
      {
        path: 'config/parameters',
        name: 'config-parameters',
        component: ParametersView,
        meta: { requiresAuth: true }
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('authToken')

  if (to.meta.requiresAuth && !token) {
    next("/login")
  } else {
    next()
  }
});

export default router
