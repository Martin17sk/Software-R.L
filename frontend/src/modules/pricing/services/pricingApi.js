import api from '@/services/axios'

export async function fetchPriceLists() {
    const {data} = await api.get("/listas-precio")
    return data
}