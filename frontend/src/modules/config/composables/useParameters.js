import { ref } from "vue";
import { fetchSystems, fetchListasPrecio, fetchArticulosNombres } from "../services/configApi";

export function useParameters() {
    const loading = ref(false)
    const error = ref('')

    const systems = ref([])
    const listasPrecio = ref([])
    const articulos = ref([])

    async function loadAll() {
        loading.value = true
        error.value = ''
        try {
            const [s, lp, arts] = await Promise.all([
                fetchSystems(),
                fetchListasPrecio(),
                fetchArticulosNombres(),
            ])

            systems.value = s ?? []
            listasPrecio.value = lp ?? []
            articulos.value = arts ?? []
        } catch (e) {
            error.value = e?.response?.data?.message || e?.message || 'No se pudo cargar la configuración'
            throw e
        } finally {
            loading.value = false
        }
    }

    return { loading, error, systems, listasPrecio, articulos, loadAll}
}