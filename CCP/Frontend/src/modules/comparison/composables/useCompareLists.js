import { ref } from 'vue'
import { useHttpClient } from '@composables/useHttpClient'

export function useCompareLists() {
  const { post, loading, error } = useHttpClient()
  const fileA = ref(null)
  const fileB = ref(null)
  const resultReport = ref(null)

  function setFileA(f) {
    fileA.value = f
  }

  function setFileB(f) {
    fileB.value = f
  }

  async function compare() {
    if (!fileA.value || !fileB.value) {
      throw new Error('Debes cargar ambos archivos para comparar')
    }

    const formData = new FormData()
    formData.append('archivoA', fileA.value)
    formData.append('archivoB', fileB.value)

    const data = await post('/comparison/compare', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    resultReport.value = data // estructura de discrepancias
  }

  return {
    fileA,
    fileB,
    resultReport,
    loading,
    error,
    setFileA,
    setFileB,
    compare,
  }
}
