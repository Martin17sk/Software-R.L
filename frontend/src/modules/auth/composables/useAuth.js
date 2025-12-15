import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useHttpClient } from '@composables/useHttpClient'
import { authApi } from '../services/authApi'

export function useAuth() {
  const username = ref('')
  const password = ref('')
  const errorMessage = ref(null)
  const loading = ref(false)
  const router = useRouter()
  const { get, setAuthToken, clearAuthToken } = useHttpClient()

  async function login() {
    errorMessage.value = null
    loading.value = true

    try {
      const token = 'Basic ' + btoa(`${username.value}:${password.value}`)
      setAuthToken(token)

      const api = authApi();

      const me = await api.me();

      await router.push('/pricing/register')
    } catch (err) {
      clearAuthToken()
      errorMessage.value = err?.message || 'Error al inicar sesión'
    } finally {
      loading.value = false
    }
  }

  return { username, password, errorMessage, loading, login }
}
