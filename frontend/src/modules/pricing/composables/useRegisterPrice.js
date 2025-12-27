import { ref, computed } from 'vue'
import { fetchArticuloBasic, fetchArticuloPrecioActual, registrarCambioPrecio } from '../services/pricingApi'

export function useRegisterPrice() {
  const articleCode = ref('')
  const articleName = ref('')
  const currentPrice = ref(null)
  const selectedPriceList = ref(null)
  const newPrice = ref(null)
  const note = ref('')

  const articleError = ref('')
  const priceError = ref('')
  const saving = ref(false)

  const isPriceValid = computed(() => typeof newPrice.value === 'number' && newPrice.value > 0)
  const isNoteTooLong = computed(() => (note.value?.length ?? 0) > 255)

  async function fetchArticle() {
    const codigo = String(articleCode.value || '').trim()
    if (!codigo) return

    articleError.value = ''
    try {
      const a = await fetchArticuloBasic(codigo)
      articleName.value = a.nombre
    } catch (e) {
      articleName.value = ''
      currentPrice.value = null
      const msg = e?.response?.data?.message || e?.response?.data || ''
      articleError.value = msg || 'No existe un artículo con ese código'
      throw e
    }
  }

  async function fetchCurrentPrice() {
    const codigo = String(articleCode.value || '').trim()
    const listaId = selectedPriceList.value

    if (!codigo || !listaId) return

    priceError.value = ''
    try {
      const res = await fetchArticuloPrecioActual(codigo, listaId)
      articleName.value = res.nombre
      currentPrice.value = res.precioActual
    } catch (e) {
      currentPrice.value = null
      const msg = e?.response?.data?.message || e?.response?.data || ''
      priceError.value = msg || 'No se pudo cargar el precio actual'
    }
  }

  async function registerPrice() {
    const codigo = String(articleCode.value || '').trim()
    const listaId = selectedPriceList.value

    if (!codigo) throw new Error('Ingresa el código del artículo')
    if (!listaId) throw new Error('Selecciona una lista de precios')
    if (!isPriceValid.value) throw new Error('Ingresa un precio nuevo válido')
    if (isNoteTooLong.value) throw new Error('La nota excede los 255 caracteres')

      saving.value = true
      try {
        return await registrarCambioPrecio({
          articuloCodigo: codigo,
          listaPrecioId: Number(listaId),
          precioNuevo: Number(newPrice.value),
          observacion: note.value?.trim() ? note.value.trim() : null,
        })
      } finally {
        saving.value = false
      }
  }

  return {
    articleCode,
    articleName,
    currentPrice,
    selectedPriceList,
    newPrice,
    note,
    articleError,
    priceError,
    fetchArticle,
    fetchCurrentPrice,
    registerPrice,
  }
}
