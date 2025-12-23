<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import BaseTable from '@/components/common/BaseTable.vue'

import Check from '@/icons/Check.png'
import Cross from '@/icons/X.png'
import Search from '@/icons/Search.png'
import BaseInputText from '@/components/common/BaseInputText.vue';
import BaseDropdown from '@/components/common/BaseDropdown.vue';
import { computed } from 'vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import router from '@/router';
import FooterBar from '@/components/layout/FooterBar.vue';
import ArrowLeftIcon from '@/icons/arrow-left.svg'



const columns = [
    { key: 'codigo', label: 'Código Artículo' },
    { key: 'nombre', label: 'Nombre Artículo' },
    { key: 'precio1', label: 'Precio Lista 1', align: 'right' },
    { key: 'precio2', label: 'Precio Lista 2', align: 'right' },
    { key: 'estado', label: 'Estado', align: 'center' },
]

const rows = ref([
    {
        codigo: 'VV2538',
        nombre: 'Chorizo parrilleros 250g',
        precio1: 5000,
        precio2: 6000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
    {
        codigo: 'VV2497',
        nombre: 'Longaniza 250g',
        precio1: 3000,
        precio2: 2000,
        estado: 'warning',
    },
])

const query = ref('');

const sortKey = ref('nombre:asc');

const sortOptions = [
    { value: 'nombre:asc', label: 'Nombre A-Z' },
    { value: 'nombre:desc', label: 'Nombre Z-A' },
    { value: 'codigo:asc', label: 'Código A-Z' },
    { value: 'codigo:desc', label: 'Código Z-A' }
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

function goToCompare() {
    router.push({ name: 'compare-lists' })
}
</script>

<template>
    <section class="bg-[#F4F4F4] h-full w-full flex flex-col">
        <main class="flex-1 min-h-0 w-full flex flex-col items-center overflow-hidden">
            <div class="flex flex-col gap-[50px] w-full max-w-[900px] px-6">
                <!-- Toolbar -->
                <div class="flex flex-row gap-[20px] justify-center items-end shrink-0">
                    <div class="flex flex-row gap-[10px]">
                        <div
                            class="flex flex-row bg-[#69F88D] text-white text-[22px] font-semibold rounded-[6px] gap-[10px] justify-center items-center px-[16px] py-[5px]">
                            <span>123</span>
                            <img :src="Check" alt="">
                        </div>
                        <div
                            class="flex flex-row bg-[#FF7A7A] text-white text-[22px] font-semibold rounded-[6px] gap-[10px] justify-center items-center px-[16px] py-[5px]">
                            <span>2</span>
                            <img :src="Cross" alt="">
                        </div>
                    </div>
                    <BaseInputText label="Buscar" placeholder="Buscar artículo por código o nombre" :icon-right="Search"
                        v-model="query" class="w-[300px]" />
                    <BaseDropdown label="Ordenar por:" :options="sortOptions" v-model="sortKey" />
                </div>

                <!-- Divider -->
                <div class="w-full h-[1px] border-1 border-[#000] border-dotted shrink-0"></div>

                <!-- Tabla de resultados -->
                <div class="flex-1 min-h-0 w-full">
                    <BaseTable :columns="columns" :rows="displayedRows" :max-height="500">
                        <template #cell-estado="{ value }">
                            <span v-if="value === 'warning'" class="inline-flex justify-center text-red-500">⚠️</span>
                        </template>
                    </BaseTable>
                </div>
            </div>
        </main>

        <FooterBar>
            <template #center>
                <BaseButton label="Volver" variant="secondary" class="w-[200px] gap-[10px]" @click="goToCompare">
                    <template #iconLeft>
                        <ArrowLeftIcon class="h-6 w-6" />
                    </template>
                </BaseButton>
            </template>
        </FooterBar>
    </section>
</template>
