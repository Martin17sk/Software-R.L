import axios from 'axios'
import { ref } from 'vue'

//const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
const http = axios.create({
  baseURL: 'http://localhost:5173',
  withCredentials: true,
})

export function useHttpClient() {
  function setAuthToken(token) {
    localStorage.setItem('authToken', token)
    http.defaults.headers.common.Authorization = token
  }

  function clearAuthToken() {
    localStorage.removeItem('authToken')
    delete http.defaults.headers.common.Authorization
  }

  const saved = localStorage.getItem('authToken')
  if (saved) {
    http.defaults.headers.common.Authorization = saved
  }

  return {
    get: (url, config) => http.get(url, config),
    post: (url, data, config) => http.post(url, data, config),
    setAuthToken,
    clearAuthToken,
  }
}