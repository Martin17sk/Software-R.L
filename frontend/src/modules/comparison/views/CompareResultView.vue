<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import BaseTable from '@/components/common/BaseTable.vue'

import IconCheck from '@/icons/check.svg'
import IconXMark from '@/icons/x-mark.svg'
import IconSearch from '@/icons/magnifying-glass.svg'
import BaseInputText from '@/components/common/BaseInputText.vue';
import BaseDropdown from '@/components/common/BaseDropdown.vue';
import { computed, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import FooterBar from '@/components/layout/FooterBar.vue';
import ArrowLeftIcon from '@/icons/arrow-left.svg'

const router = useRouter()

const report = ref(null)

onMounted(() => {
    const raw = sessionStorage.getItem('comparisonReport')
    report.value = raw ? JSON.parse(raw) : null
})

const columns = [
    { key: 'codigo', label: 'Código Artículo' },
    { key: 'nombre', label: 'Nombre Artículo' },
    { key: 'precio1', label: 'Precio Lista 1', align: 'right' },
    { key: 'precio2', label: 'Precio Lista 2', align: 'right' },
    { key: 'estado', label: 'Estado', align: 'center' },
]

const rows = computed(() => {
    const items = report.value?.discrepancias ?? []
    return items.map(it => ({
        codigo: it.codigoArticulo,
        nombre: it.nombreArticulo,
        precio1: it.precioA ?? '',
        precio2: it.precioB ?? '',
        estado: it.tipoDiscrepancia, // PRECIO_DISTINTO | NO_EN_B | STRUCT_ERROR
    }))
})

const okCount = computed(() => report.value?.totalSinDiscrepancia ?? 0)
const badCount = computed(() => report.value?.totalConDiscrepancia ?? 0)

const query = ref('');
const sortKey = ref('nombre:asc');

const sortOptions = [
    { value: 'nombre:asc', label: 'Nombre A-Z' },
    { value: 'nombre:desc', label: 'Nombre Z-A' },
    { value: 'codigo:asc', label: 'Código A-Z' },
    { value: 'codigo:desc', label: 'Código Z-A' }
]

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
            <div class="flex flex-col gap-[20px] w-full max-w-[900px] px-6">
                <!-- Toolbar -->
                <div class="flex flex-row gap-[20px] justify-center items-end shrink-0">
                    <div class="flex flex-row gap-[10px]">
                        <div
                            class="flex flex-row bg-[#69F88D] text-white text-[22px] font-semibold rounded-[6px] gap-[10px] justify-center items-center px-[16px] py-[5px]">
                            <span>{{ okCount }}</span>
                            <IconCheck class="h-6 w-6" />
                        </div>
                        <div
                            class="flex flex-row bg-[#FF7A7A] text-white text-[22px] font-semibold rounded-[6px] gap-[10px] justify-center items-center px-[16px] py-[5px]">
                            <span>{{ badCount }}</span>
                            <IconXMark class="h-6 w-6" />
                        </div>
                    </div>
                    <BaseInputText label="Buscar" placeholder="Buscar artículo por código o nombre" v-model="query"
                        class="w-[300px]">
                        <template #iconRight>
                            <IconSearch class="h-4 w-4" />
                        </template>
                    </BaseInputText>
                    <BaseDropdown label="Ordenar por:" :options="sortOptions" v-model="sortKey" />
                </div>

                <!-- Divider -->
                <div class="w-full h-[1px] border-1 border-[#000] border-dotted shrink-0"></div>

                <!-- Tabla de resultados -->
                <div class="flex-1 min-h-0 w-full">
                    <BaseTable :columns="columns" :rows="displayedRows" :max-height="500">
                        <template #cell-estado="{ value }">
                            <span v-if="value === 'PRECIO_DISTINTO'">⚠️</span>
                            <span v-else-if="value === 'NO_EN_B'">❌</span>
                            <span v-else-if="value === 'STRUCT_ERROR'">🧩</span>
                            <span v-else>—</span>
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
