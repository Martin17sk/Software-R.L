import axios from 'axios';

const base = import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, '')
if (!base) {
    throw new Error('Falta VITE_API_BASE_URL en el .env (ej: http://localhost:8080)')
}

const api = axios.create({
    baseURL: `${base}/api`,
    withCredentials: true,
});

export default api;