<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import BaseTable from '@/components/common/BaseTable.vue'
import BaseInputText from '@/components/common/BaseInputText.vue';
import BaseDropdown from '@/components/common/BaseDropdown.vue';

import ArrowLeftIcon from '@/icons/arrow-left.svg'
import IconSearch from '@/icons/magnifying-glass.svg'

import { onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import FooterBar from '@/components/layout/FooterBar.vue';

import { useHistory } from '../composables/useHistory';

const columns = [
  { key: 'codigo', label: 'Código Artículo' },
  { key: 'nombre', label: 'Nombre Artículo' },
  { key: 'lista', label: 'Lista de precio' },
  { key: 'sistema', label: 'Sistema' },
  { key: 'precioAnterior', label: 'Precio Anterior' },
  { key: 'precioActual', label: 'Precio Actual' },
  { key: 'fechaCambio', label: 'Fecha de Cambio' },
  { key: 'nota', label: 'Nota' },
  { key: 'responsable', label: 'Responsable' },
]

const { rows, load } = useHistory()

const router = useRouter()

const query = ref('');

const sortKey = ref('fechaCambio:desc');

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

let debounceId = null

function goToRegister() {
  router.push({name: 'register-price'})
}

function reload() {
  clearTimeout(debounceId)
  debounceId = setTimeout(() => {
    load({ search: query.value, sortKey: sortKey.value, page: 0 })
  }, 300)
}

watch(query, reload)
watch(sortKey, () => load({ search: query.value, sortKey: sortKey.value, page: 0 }))

onMounted(() => {
  load({ search: query.value, sortKey: sortKey.value, page: 0 })
})

</script>

<template>
  <section class="bg-[#F4F4F4] h-full w-full flex flex-col">
    <main class="flex-1 min-h-0 w-full flex flex-col items-center overflow-hidden">
      <div class="flex flex-col gap-[50px] w-full max-w-[1200px] px-6 py-6 min-h-0">
        <!-- Toolbar -->
        <div class="flex flex-row gap-[20px] justify-center items-end shrink-0">
          <BaseInputText label="Buscar" placeholder="Buscar artículo por código o nombre"
            v-model="query" class="w-[300px]">
            <template #iconRight>
              <IconSearch class="h-4 w-4"/>
            </template>
          </BaseInputText>
          <BaseDropdown label="Ordenar por:" :options="sortOptions" v-model="sortKey" />
        </div>

        <!-- Divider -->
        <div class="w-full border-t border-black border-dotted shrink-0"></div>

        <!-- Tabla de resultados -->
        <div class="history-table">
          <BaseTable :columns="columns" :rows="rows" :max-height="500" />
        </div>
      </div>
    </main>

    <FooterBar>
      <template #center>
        <BaseButton label="Volver" variant="secondary" class="w-[200px] h-[40px] gap-[10px]" @click="goToRegister">
          <template #iconLeft>
            <ArrowLeftIcon class="h-5 w-5 text-white" />
          </template>
        </BaseButton>
      </template>
    </FooterBar>
  </section>
</template>
