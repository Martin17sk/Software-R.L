import api from '@/services/axios'

export async function fetchSystems() {
    const {data} = await api.get("/sistemas")
    return data
}

export async function fetchListasPrecio() {
    const {data} = await api.get('/listas-precio')
    return data
}

export async function fetchArticulosNombres() {
    const {data} = await api.get('/articulos/nombres')
    return data
}