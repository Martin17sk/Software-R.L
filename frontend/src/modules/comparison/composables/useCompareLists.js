import { ref } from 'vue'
import { compararListas } from '../services/comparisonApi'

export function useCompareLists() {
  const loading = ref(false)
  const error = ref(null)

  const fileA = ref(null)
  const fileB = ref(null)
  const resultReport = ref(null)

  function setFileA(f) {fileA.value = f}
  function setFileB(f) {fileB.value = f}

  async function compare(sistemaId = 1) {
    error.value = null
    if (!fileA.value || !fileB.value) {
      throw new Error('Debes cargar ambos archivos para comparar')
    }


    loading.value = true
    try {
      const data = await compararListas({
        listaA: fileA.value,
        listaB: fileB.value,
        sistemaId,
      })
      resultReport.value = data
      return data
    } finally {
      loading.value = false
    }
  }

  return {
    fileA, fileB, resultReport, 
    loading, error,
    setFileA, setFileB,
    compare,
  }
}
