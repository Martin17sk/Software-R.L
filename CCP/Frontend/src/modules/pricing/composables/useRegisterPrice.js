import { ref, computed } from 'vue'
import { useHttpClient } from '@composables/useHttpClient'

export function useRegisterPrice() {
  const { post, loading, error } = useHttpClient()

  const articleCode = ref('')
  const articleName = ref('')
  const currentPrice = ref(null)
  const priceLists = ref([])
  const selectedPriceList = ref(null)
  const newPrice = ref(null)
  const note = ref('')

  const isNoteTooLong = computed(() => note.value.length > 255)
  const isPriceValid = computed(
    () => typeof newPrice.value === 'number' && newPrice.value > 0,
  )

  async function fetchArticle() {
    if (!articleCode.value) return
    const data = await post('/pricing/article', { codigo: articleCode.value })
    articleName.value = data.nombre
    currentPrice.value = data.precioActual
    priceLists.value = data.listasPrecio
  }

  async function registerPrice() {
    if (!isPriceValid.value) {
      throw new Error('Precio inválido')
    }
    if (isNoteTooLong.value) {
      throw new Error('La nota excede los 255 caracteres')
    }

    const payload = {
      codigoArticulo: articleCode.value,
      idListaPrecio: selectedPriceList.value,
      nuevoPrecio: newPrice.value,
      nota: note.value || null,
    }

    // El backend se encarga de guardar historial con precio anterior/nuevo, fecha-hora, usuario, etc.
    await post('/pricing/register', payload)
  }

  return {
    articleCode,
    articleName,
    currentPrice,
    priceLists,
    selectedPriceList,
    newPrice,
    note,
    loading,
    error,
    isNoteTooLong,
    isPriceValid,
    fetchArticle,
    registerPrice,
  }
}
