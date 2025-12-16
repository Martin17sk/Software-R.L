<template>
    <div class="w-full rounded-lg border border-slate-200 overflow-hidden">
        <div class="overflow-y-auto overflow-x-auto" :style="wrapperStyle">
            <table class="w-full border-collapse text-sm">
                <!-- Header -->
                <thead class="bg-slate-100 text-slate-700">
                    <tr>
                        <th 
                            v-for="col in columns" 
                            :key="col.key"
                            class="sticky top-0 z-10 bg-slate-100 px-4 py-3 font-semibold"
                            :class="alignClass(col.align)"
                        >
                            {{ col.label }}
                        </th>
                    </tr>
                </thead>

                <!-- Body -->
                <tbody>
                    <tr 
                        v-for="(row, rowIndex) in rows" 
                        :key="rowIndex" 
                        class="border-t border-slate-200"
                        :class="rowIndex % 2 === 0 ? 'bg-white' : 'bg-slate-50'"
                    >
                        <td 
                            v-for="col in columns" 
                            :key="col.key" 
                            class="px-4 py-3 text-slate-800"
                            :class="alignClass(col.align)"
                        >
                            <!-- Slot por columna -->
                            <slot :name="`cell-${col.key}`" :value="row[col.key]" :row="row">
                                {{ row[col.key] }}
                            </slot>
                        </td>
                    </tr>

                    <!-- Empty -->
                    <tr v-if="!rows.length">
                        <td :colspan="columns.length" class="px-4 py-6 text-center text-slate-400">
                            Sin resultados
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    columns: {
        type: Array,
        default: () => [],
    },
    rows: {
        type: Array,
        default: () => [],
    },
    maxHeight: {
        type: Number,
        default: 420,
    }
})

const wrapperStyle = computed(() => ({
    maxHeight: props.maxHeight ? `${props.maxHeight}px` : undefined
}))

function alignClass(align) {
    if (align === 'right') return 'text-right'
    if (align === 'center') return 'text-center'
    return 'text-left'
}
</script>
