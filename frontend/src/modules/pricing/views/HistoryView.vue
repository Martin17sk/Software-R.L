<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import BaseTable from '@/components/common/BaseTable.vue'
import BaseInputText from '@/components/common/BaseInputText.vue';
import BaseDropdown from '@/components/common/BaseDropdown.vue';

import WhiteArrowLeftIcon from '@/icons/WhiteArrowLeft.png'
import TrashIcon from '@/icons/Trash.png'

import { computed, onMounted, onBeforeUnmount, ref } from 'vue';

const columns = [
  { key: 'codigo', label: 'Código Artículo' },
  { key: 'nombre', label: 'Nombre Artículo' },
  { key: 'lista', label: 'Lista de precio' },
  { key: 'sistema', label: 'Sistema' },
  { key: 'precioAnterior', label: 'Precio Actual' },
  { key: 'precioNuevo', label: 'Precio Nuevo' },
  { key: 'fechaCambio', label: 'Fecha de Cambio' },
  { key: 'nota', label: 'Nota' },
  { key: 'responsable', label: 'Responsable' },
]

const rows = ref([
  {
    codigo: 'VV2538',
    nombre: 'Chorizo parrilleros 250g',
    lista: '(1) Cob. Osorno a Chiloe',
    sistema: 'SAP',
    precioAnterior: 5000,
    precioActual: 6000,
    fechaCambio: '2025-12-11 12:45',
    nota: 'Nota de prueba larga, pero muy muy larga larguisima, pero de verdad muy larga',
    responsable: 'Nelson',
  },
  {
    codigo: 'VV2497',
    nombre: 'Longaniza 250g',
    lista: '(2) Cob. Stgo. a Talca',
    sistema: 'POS_Web',
    precioAnterior: 3000,
    precioActual: 2000,
    fechaCambio: '2025-12-11 13:41',
    nota: 'Nota de prueba corta',
    responsable: 'Nelson',
  },
])

const query = ref('');

const sortKey = ref('nombre:asc');

const sortOptions = [
  { value: 'nombre:asc', label: 'Nombre A-Z' },
  { value: 'nombre:desc', label: 'Nombre Z-A' },
  { value: 'codigo:asc', label: 'Código A-Z' },
  { value: 'codigo:desc', label: 'Código Z-A' },
  { value: 'lista:asc', label: 'Lista A-Z' },
  { value: 'lista:desc', label: 'Lista Z-A' },
  { value: 'sistema:asc', label: 'Sistema A-Z' },
  { value: 'sistema:desc', label: 'Sistema Z-A' },
  { value: 'responsable:asc', label: 'Responsable A-Z' },
  { value: 'responsable:desc', label: 'Responsable Z-A' },
  { value: 'fechaCambio:asc', label: 'Fecha de Cambio Ascendente' },
  { value: 'fechaCambio:desc', label: 'Fecha de Cambio Descendente' },
]

//Filtro
const filteredRows = computed(() => {
  const base = rows.value ?? []
  const q = query.value.trim().toLowerCase()
  if (!q) return base;

  return base.filter(r => {
    const codigo = String(r.codigo ?? '').toLowerCase()
    const nombre = String(r.nombre ?? '').toLowerCase()
    return codigo.includes(q) || nombre.includes(q)
  })
})

//Ordenar
const displayedRows = computed(() => {
  const base = filteredRows.value ?? []
  const [field, dir] = String(sortKey.value).split(':')
  const sign = dir === 'desc' ? -1 : 1

  return [...base].sort((a, b) => {
    const aValue = a?.[field]
    const bValue = b?.[field]

    //Numeros
    if (typeof aValue === 'number' && typeof bValue === 'number') {
      return (aValue - bValue) * sign
    }

    //Strings
    return String(aValue ?? '').localeCompare(String(bValue ?? ''), 'es', { sensitivity: 'base' }) * sign
  })
})

const now = ref(Date.now());
let timerId = null;

onMounted(() => {
  timerId = setInterval(() => {
    now.value = Date.now();
  }, 1000);
});

onBeforeUnmount(() => clearInterval(timerId));

const WINDOW_MS = 24 * 60 * 60 * 1000; // 24 horas

function remainingMs(row) {
  const created = new Date(row.fechaCambio).getTime();
  if (Number.isNaN(created)) return 0;
  return Math.max(0, created + WINDOW_MS - now.value);
}

function formatHHMMSS(ms) {
  const totalSeconds = Math.floor(ms / 1000);
  const hours = String(Math.floor(totalSeconds / 3600)).padStart(2, '0');
  const minutes = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, '0');
  const seconds = String(totalSeconds % 60).padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
}

function canDelete(row) {
  return remainingMs(row) > 0;
}

/* async function onDelete(row) {
  await http.delete(`/pricing/history/${row.codigo}`);

  //Simular espera
  await new Promise(resolve => setTimeout(resolve, 500));

  //Eliminar
  rows.value = rows.value.filter(r => r !== row);
}*/

</script>

<template>
  <section class="bg-[#F4F4F4] min-h-screen w-full flex flex-col justify-center items-center place-content-between">
    <header class="h-[180px] w-full flex flex-row py-[50px] justify-start px-[60px] shrink-0">
      <BaseButton label="Volver" :icon-left="WhiteArrowLeftIcon" variant="secondary"
        class="gap-[12px] w-[150px] h-[65px]" />
    </header>

    <main class="flex-1 w-full flex flex-col justify-start items-center">
      <div class="flex flex-col gap-[50px] w-full max-w-[1200px] px-6">
        <!-- Toolbar -->
        <div class="flex flex-row gap-[20px] justify-center items-end shrink-0">
          <BaseInputText label="Buscar" placeholder="Buscar artículo por código o nombre" :icon-right="Search"
            v-model="query" class="w-[300px]" />
          <BaseDropdown label="Ordenar por:" :options="sortOptions" v-model="sortKey" />
        </div>

        <!-- Divider -->
        <div class="w-full h-[1px] border-1 border-[#000] border-dotted shrink-0"></div>

        <!-- Tabla de resultados -->
        <div class="flex-1 min-h-0 w-full">
          <BaseTable :columns="columns" :rows="displayedRows" :max-height="420">
            <template #cell-tiempoRestante="{ row }">
              <div class="flex items-center justify-center gap-3">
                <span class="tabular-nums" :class="canDelete(row) ? '' : 'text-slate-400'">
                  {{ formatHHMMSS(remainingMs(row)) }}
                </span>

                <button v-if="canDelete(row)" type="button" class="p-1 rounded hover:bg-red-50" title="Eliminar" @click="onDelete(row)">
                  <img :src="TrashIcon" alt="Eliminar" class="w-4 h-4" />
                </button>
              </div>
            </template>
          </BaseTable>
        </div>
      </div>
    </main>

    <footer
      class="shrink-0 flex flex-col border-t border-t-black gap-[30px] w-full h-[180px] justify-center items-center">
    </footer>
  </section>
</template>
