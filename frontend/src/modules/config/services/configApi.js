import api from '@/services/axios'

export async function fetchSystems() {
    const {data} = await api.get("/sistemas")
    return data
}