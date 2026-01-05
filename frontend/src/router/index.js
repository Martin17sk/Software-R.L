import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/auth.store.js";

const routes = [
  // ───────────
  // Públicas
  // ───────────
  {
    path: "/login",
    name: "login",
    component: () => import("@/modules/auth/views/LoginView.vue"),
    meta: { public: true, layout: "blank" },
  },

  // ───────────
  // Privadas SIN header
  // ───────────
  {
    path: "/configuracion",
    name: "configuration",
    component: () => import("@/modules/config/views/ConfigurationView.vue"),
    meta: { requiresAuth: true, layout: "blank" },
  },

  // ───────────
  // Privadas CON header
  // ───────────
  {
    path: "/",
    redirect: { name: "register-price" },
  },
  {
    path: "/registro-precio",
    name: "register-price",
    component: () => import("@/modules/pricing/views/RegisterPriceView.vue"),
    meta: { requiresAuth: true, layout: "app" },
  },
  {
    path: "/comparar-listas",
    name: "compare-lists",
    component: () => import("@/modules/comparison/views/CompareListsView.vue"),
    meta: { requiresAuth: true, layout: "app" },
  },
  {
    path: "/resultado-comparacion",
    name: "compare-result",
    component: () => import("@/modules/comparison/views/CompareResultView.vue"),
    meta: { requiresAuth: true, layout: "app" },
  },
  {
    path: "/historial",
    name: "history",
    component: () => import("@/modules/pricing/views/HistoryView.vue"),
    meta: { requiresAuth: true, layout: "app" },
  },

  // ───────────
  // Fallback
  // ───────────
  {
    path: "/:pathMatch(.*)*",
    redirect: { name: "register-price" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to) => {
  const auth = useAuthStore();

  // Esperar init()
  if (auth.loading) {
    await new Promise((resolve) => {
      const stop = auth.$subscribe(() => {
        if (!auth.loading) {
          stop();
          resolve();
        }
      });
    });
  }

  // Ruta pública
  if (to.meta.public) {
    if (auth.isAuthenticated && to.name === "login") {
      return { name: "register-price" };
    }
    return true;
  }

  // Ruta privada
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: "login" };
  }

  return true;
});

export default router;
