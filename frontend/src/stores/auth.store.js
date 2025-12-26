import { defineStore } from "pinia";
import api from "@/services/axios";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null, // objeto usuario devuelto por el backend
    loading: false,
  }),

  getters: {
    isAuthenticated: (state) => !!state.user
  },

  actions: {
    // Se llama al inicar la app (App.vue)
    async init() {
      this.loading = true;
      try {
        const res = await api.get("/me");
        this.user = res.data
      } catch (err) {
        // No hay sesion valida
        this.user = null
        console.log(err)
      } finally {
        this.loading = false
      }
    },

    // Login real (sesion)
    async login(username, password) {
      this.loading = true
      try {
        const res = await api.post("/login", {
          username,
          password,
        });

        //El backend crea la sesion y devuelve el usuario
        this.user = res.data
        return res.data
      } catch (err) {
        this.user = null;
        throw err
      } finally {
        this.loading = false
      }
    },

    // Logout real
    async logout(){
      try {
        await api.post("/logout")
      } finally {
        this.user = null
      }
    }
  }
})