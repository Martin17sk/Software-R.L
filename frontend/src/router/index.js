import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@auth/views/LoginView.vue'
import RegisterPriceView from '@pricing/views/RegisterPriceView.vue'
import HistoryView from '@pricing/views/HistoryView.vue'
import CompareListsView from '@comparison/views/CompareListsView.vue'
import CompareResultView from '@/modules/comparison/views/CompareResultView.vue'
import ConfigurationView from '@/modules/config/views/ConfigurationView.vue'
import { useAuthStore } from '@/stores/auth.store'

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
    meta: {requiresAuth: true},
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
        path: 'comparison/result',
        name: 'comparison-result',
        component: CompareResultView,
        meta: { requiresAuth: true }
      },
      {
        path: 'configuration',
        name: 'configuration',
        component: ConfigurationView,
        meta: { requiresAuth: true }
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach(async (to) => {
  const auth = useAuthStore();

  if (auth.loading) {
    await new Promise((resolve) => {
      const stop = auth.$subscribe(() => {
        if (!auth.loading) {
          stop()
          resolve()
        }
      })
    })
  }

  if (to.meta.public) return true

  if (to.meta.requiresAuth && !auth.isAuthenticated){
    return {name: "login"}
  }

  return true
});

export default router
