import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'

export function useAuth() {
  const authStore = useAuthStore()
  const router = useRouter()

  const username = ref("")
  const password = ref("")
  const errorMessage = ref(null)

  const loading = computed(() => authStore.loading)

  async function login() {
    errorMessage.value = null

    try {
      await authStore.login(username.value, password.value)
      router.replace({ name: "home" })
    } catch {
      errorMessage.value = "Credenciales inválidas"
    }
  }

  return {
    username,
    password,
    errorMessage,
    loading,
    login,
  }
}
