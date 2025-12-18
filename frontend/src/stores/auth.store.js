import { defineStore } from "pinia";
import { login as apiLogin, logout as apiLogout, me as apiMe } from "@/services/auth.service";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,       // { usuarioId, username } según tu backend
    loading: false,
  }),

  getters: {
    isAuthenticated: (state) => !!state.user,
  },

  actions: {
    async init() {
      // Se llama al arrancar la app para recuperar sesión si existe
      this.loading = true;
      try {
        this.user = await apiMe();
      } catch {
        this.user = null;
      } finally {
        this.loading = false;
      }
    },

    async login(username, password) {
      this.loading = true;
      try {
        this.user = await apiLogin(username, password);
        return this.user;
      } finally {
        this.loading = false;
      }
    },

    async logout() {
      this.loading = true;
      try {
        await apiLogout();
      } finally {
        this.user = null;
        this.loading = false;
      }
    },
  },
});
