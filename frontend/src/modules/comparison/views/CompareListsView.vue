<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import BaseDropZone from '@/components/common/BaseDropZone.vue'
import BaseButton from '@/components/common/BaseButton.vue'

import FooterBar from '@/components/layout/FooterBar.vue'
import IconArrowsRightLeft from '@/icons/arrows-right-left.svg'
import { useCompareLists } from '../composables/useCompareLists'

const previewLeft = ref(null)
const previewRight = ref(null)
const previewErrorLeft = ref('')
const previewErrorRight = ref('')

const excelFileLeft = ref(null)
const excelFileRight = ref(null)

const router = useRouter()

const { setFileA, setFileB, compare } = useCompareLists()

// TODO: por ahora fijo. Después lo tomas desde tu selector real de sistema.
const sistemaId = ref(1)

async function readExcelPreview(file) {
  const XLSX = await import('xlsx')

  const data = await file.arrayBuffer()
  const wb = XLSX.read(data, { type: 'array' })

  const sheetName = wb.SheetNames?.[0]
  if (!sheetName) throw new Error('El archivo no contiene hojas')

  const ws = wb.Sheets[sheetName]
  const matrix = XLSX.utils.sheet_to_json(ws, { header: 1, raw: false })

  const headers = (matrix[0] ?? []).map(h => String(h ?? '').trim())
  const body = matrix.slice(1).filter(r => Array.isArray(r) && r.some(c => String(c ?? '').trim() !== ''))

  const rows = body.slice(0, 10).map(r => r.map(c => String(c ?? '').trim()))

  return {
    fileName: file.name,
    sheetName,
    rowCount: body.length,
    headers,
    rows
  }
}

function resetLeft() {
  excelFileLeft.value = null
  previewLeft.value = null
  previewErrorLeft.value = ''
}

function resetRight() {
  excelFileRight.value = null
  previewRight.value = null
  previewErrorRight.value = ''
}

watch(excelFileLeft, async (f) => {
  previewLeft.value = null
  previewErrorLeft.value = ''
  if (!f) return

  try {
    previewLeft.value = await readExcelPreview(f)
  } catch (e) {
    previewErrorLeft.value = e?.message ?? 'No se pudo leer el archivo'
  }
})

watch(excelFileRight, async (f) => {
  previewRight.value = null
  previewErrorRight.value = ''
  if (!f) return

  try {
    previewRight.value = await readExcelPreview(f)
  } catch (e) {
    previewErrorRight.value = e?.message ?? 'No se pudo leer el archivo.'
  }
})

async function runCompare() {
  setFileA(excelFileLeft.value)
  setFileB(excelFileRight.value)

  const report = await compare(sistemaId.value)

  sessionStorage.setItem('comparisonReport', JSON.stringify(report))
  router.push({ name: 'compare-result'})
}

function goToResults() {
  router.push({ name: 'compare-result'})
}
</script>

<template>
  <section class="bg-[#F4F4F4] h-full w-full flex flex-col">
    <main class="h-full w-full flex flex-col justify-start items-center pt-[80px]">
      <div>
        <div class="flex flex-col gap-[20px]">
          <h1>Comparar listas de precios</h1>
          <div class="flex flex-row gap-[50px] items-center">
            <div class="flex flex-col w-[640px] bg-white p-[24px] rounded-[12px] gap-[32px] shadow-md">
              <BaseDropZone title="Subir archivo" subtitle="Por favor, suba el archivo de referencia en formato .xls o .xlsx"
                accept=".xls,.xlsx" v-model:file="excelFileLeft" />

              <div v-if="previewErrorLeft" class="text-sm text-red-600">
                {{ previewErrorLeft }}
              </div>

              <div v-else-if="previewLeft" class="text-sm text-slate-600">
                <div class="flex flex-col gap-1">
                  <div><span class="font-semibold">Archivo:</span> {{ previewLeft.fileName }}</div>
                  <div><span class="font-semibold">Hoja:</span> {{ previewLeft.sheetName }}</div>
                  <div><span class="font-semibold">Filas detectadas:</span> {{ previewLeft.rowCount }}</div>
                </div>

                <div v-if="previewLeft.rows.length" class="mt-3 overflow-auto max-h-[180px] border rounded bg-white">
                  <table class="w-full text-xs">
                    <thead class="sticky top-0 bg-white">
                      <tr>
                        <th v-for="(h, i) in previewLeft.headers" :key="i"
                          class="text-left p-2 border-b whitespace-nowrap">
                          {{ h || `Col ${i + 1}` }}
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(r, ri) in previewLeft.rows" :key="ri">
                        <td v-for="(c, ci) in r" :key="ci" class="p-2 border-b whitespace-nowrap">
                          {{ c }}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <div class="flex flex-row items-center justify-between gap-[12px]">
                <BaseButton variant="outline" label="Restablecer" class="w-full" @click="resetLeft"/>
              </div>
            </div>
            <div class="flex h-full items-center">
              <IconArrowsRightLeft class="w-24 h-24"/>
            </div>
            <div class="flex flex-col w-[640px] bg-white p-[24px] rounded-[12px] gap-[32px] shadow-md">
              <BaseDropZone title="Subir archivo" subtitle="Por favor, suba el archivo a comparar con la referencia en formato .xls o .xlsx"
                accept=".xls,.xlsx" v-model:file="excelFileRight" />

              <div v-if="previewErrorRight" class="text-sm text-red-600">
                {{ previewErrorRight }}
              </div>

              <div v-else-if="previewRight" class="text-sm text-slate-600">
                <div class="flex flex-col gap-1">
                  <div><span class="font-semibold">Archivo:</span> {{ previewRight.fileName }}</div>
                  <div><span class="font-semibold">Hoja:</span> {{ previewRight.sheetName }}</div>
                  <div><span class="font-semibold">Filas detectadas:</span> {{ previewRight.rowCount }}</div>
                </div>

                <div v-if="previewRight.rows.length" class="mt-3 overflow-auto max-h-[180px] border rounded bg-white">
                  <table class="w-full text-xs">
                    <thead class="sticky top-0 bg-white">
                      <tr>
                        <th v-for="(h, i) in previewRight.headers" :key="i"
                          class="text-left p-2 border-b whitespace-nowrap">
                          {{ h || `Col ${i + 1}` }}
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(r, ri) in previewRight.rows" :key="ri">
                        <td v-for="(c, ci) in r" :key="ci" class="p-2 border-b whitespace-nowrap">
                          {{ c }}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>

              <div class="flex flex-row items-center justify-between gap-[12px]">
                <BaseButton variant="outline" label="Restablecer" class="w-full" @click="resetRight"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <FooterBar>
      <template #center>
        <BaseButton label="Comparar" variant="accept" class="w-[200px]" :disabled="!excelFileLeft || !excelFileRight"
          @click="runCompare" />
      </template>
    </FooterBar>
  </section>
</template>
