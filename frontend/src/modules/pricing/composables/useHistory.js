import { ref } from "vue";
import { fetchHistorialPage, deleteHistorial } from "../services/pricingApi";

function parseSortKey(sortKey) {
    const [fieldRaw, dirRaw] = String(sortKey || 'fechaCambio:desc').split(':')
    const dir = (dirRaw || 'desc').toUpperCase() === 'ASC' ? 'ASC' : 'DESC'

    const fieldMap = {
        codigo: 'codigo',
        nombre: 'nombre',
        lista: 'lista',
        sistema: 'sistema',
        responsable: 'usuario',
        fechaCambio: 'fechaHora',
        fechaHora: 'fechaHora',
        usuario: 'usuario'
    }

    return {
        sortBy: fieldMap[fieldRaw] || 'fechaHora',
        sortDir: dir
    }
}

function mapDtoToRow(dto) {
    return {
    id: dto.id,
    codigo: dto.articuloCodigo,
    nombre: dto.articuloNombre ?? '',       // <-- requiere campo en DTO o quedará vacío
    lista: dto.listaPrecioNombre ?? '',
    sistema: dto.sistemaNombre ?? '',       // <-- requiere campo en DTO o quedará vacío
    precioAnterior: dto.precioAnterior,
    precioActual: dto.precioNuevo,
    fechaCambio: dto.fechaHora,             // ISO string / LocalDateTime serializado
    nota: dto.observacion ?? '',
    responsable: dto.usuarioNombre ?? ''
  }
}

export function useHistory() {
    const rows = ref([])
    const loading = ref(false)
    const error = ref('')

    const page = ref(0)
    const size = ref(20)
    const totalPages = ref(0)
    const totalElements = ref(0)

    async function load({ search = '', sortKey = 'fechaCambio:desc', page: p = page.value } = {}) {
        loading.value = true
        error.value = ''
        try {
            const { sortBy, sortDir } = parseSortKey(sortKey)
            const data = await fetchHistorialPage({
                search,
                sortBy,
                sortDir,
                page: p,
                size: size.value
            })

            rows.value = (data?.content ?? []).map(mapDtoToRow)
            page.value = data?.number ?? 0
            totalPages.value = data?.totalPages ?? 0
            totalElements.value = data?.totalElements ?? 0
        } catch (e) {
            error.value = 
                e?.response?.data?.message || (typeof e?.response?.data === 'string' ? e.response.data : '') || 'No se pudo cargar el historial'
        } finally {
            loading.value = false
        }
    }

    async function remove(id) {
        loading.value = true
        error.value = ''
        try {
            await deleteHistorial(id)
            await load({ page: page.value })
        } catch (e) {
            error.value =
                e?.response?.data?.message ||
                (typeof e?.response?.data === 'string' ? e.response.data : '') ||
                'No se pudo eliminar el registro'
            throw e
        } finally {
            loading.value = false
        }
    }

    return {
        rows, loading, error,
        page, size, totalPages, totalElements,
        load, remove
    }
}