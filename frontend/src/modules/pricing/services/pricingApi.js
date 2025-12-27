import api from '@/services/axios'

export async function fetchPriceLists() {
    const {data} = await api.get("/listas-precio")
    return data
}

export async function fetchHistorialPage({
    search = '',
    sortBy = 'fechaHora',
    sortDir = 'DESC',
    page = 0,
    size = 20
} = {}) {
    const { data } = await api.get('/historial', {
        params: { search, sortBy, sortDir, page, size }
    })
    return data
}

export async function deleteHistorial(id) {
    await api.delete(`/historial/${id}`)
}

export async function registrarCambioPrecio(payload) {
    const { data } = await api.post('/precios/cambio', payload)
    return data
}

export async function registrarCambioPrecioMultiple(payload) {
    const { data } = await api.post('/precios/cambio/multiple', payload)
    return data
}

export async function fetchArticuloBasic(codigo) {
    const { data } = await api.get(`/articulos/${encodeURIComponent(codigo)}/basic`)
    return data
}

export async function fetchArticuloPrecioActual(codigo, listaPrecioId) {
    const { data } = await api.get(`/articulos/${encodeURIComponent(codigo)}/precio-actual`, {
        params: { listaPrecioId },
    })
    return data
}