<template>
    <div class="relative flex flex-col gap-1" ref="root">
        <label v-if="label" class="form-label">
            {{ label }}
        </label>

        <!-- Trigger -->
        <button type="button" class="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm
             text-slate-900 transition
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500
             flex items-center justify-between gap-3"
            :class="disabled ? 'bg-slate-100 text-slate-500 cursor-not-allowed' : 'cursor-pointer'" :disabled="disabled"
            @click="toggle" @keydown.esc.prevent="close">
            <span class="truncate" :class="!selectedLabel ? 'text-slate-400' : ''">
                {{ selectedLabel || placeholder }}
            </span>

            <svg class="h-4 w-4 shrink-0" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                <path fill-rule="evenodd"
                    d="M5.23 7.21a.75.75 0 0 1 1.06.02L10 10.94l3.71-3.71a.75.75 0 1 1 1.06 1.06l-4.24 4.24a.75.75 0 0 1-1.06 0L5.21 8.29a.75.75 0 0 1 .02-1.08z"
                    clip-rule="evenodd" />
            </svg>
        </button>

        <!-- Panel -->
        <div v-if="open"
            class="absolute left-0 right-0 top-full mt-1 z-50 mt-1 rounded-lg border border-slate-200 bg-white shadow-md overflow-hidden"
            role="listbox">
            <div v-if="searchable" class="p-2 border-b border-slate-200">
                <input v-model="query" type="text" :placeholder="searchPlaceholder" class="w-full rounded-md border border-slate-300 px-3 py-2 text-sm
             text-slate-900 placeholder:text-slate-400
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" @keydown.esc.prevent="close" />
            </div>

            <ul class="py-1 overflow-auto" :style="{ maxHeight: maxMenuHeightPx }">
                <li v-for="opt in filteredOptions" :key="opt.value" role="option" class="px-3 py-2 text-sm select-none"
                    :class="[String(opt.value) === String(modelValue) ? 'bg-slate-100 font-medium' : '', opt.disabled ? 'text-slate-400 cursor-not-allowed' : 'cursor-pointer hover:bg-slate-100']"
                    @click="!opt.disabled && select(opt.value)">
                    {{ opt.label }}

                </li>

                <li v-if="!filteredOptions.length" class="px-3 py-2 text-sm text-slate-400">
                    Sin resultados
                </li>
            </ul>
        </div>

    </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

const props = defineProps({
    label: { type: String, default: '' },          // título arriba
    placeholder: { type: String, default: 'Selecciona una opción' },
    disabled: { type: Boolean, default: false },

    // v-model
    modelValue: { type: [String, Number], default: '' },

    // opciones: [{ value, label }]
    options: { type: Array, default: () => [] },

    // máximo ítems visibles (requisito: 10)
    maxVisibleItems: { type: Number, default: 10 },

    // altura por item (px). 36 suele calzar bien con py-2 text-sm
    itemHeight: { type: Number, default: 36 },

    searchable: { type: Boolean, default: false },
    searchPlaceholder: { type: String, default: 'Buscar...' },

})

const emit = defineEmits(['update:modelValue', 'change'])

const open = ref(false)
const root = ref(null)

const selectedLabel = computed(() => {
    const found = props.options.find(o => String(o.value) === String(props.modelValue))
    return found?.label ?? ''
})

const maxMenuHeightPx = computed(() => {
    return `${props.maxVisibleItems * props.itemHeight}px`
})

const query = ref('')

const filteredOptions = computed(() => {
    const q = query.value.trim().toLowerCase()
    if (!q) return props.options
    return props.options.filter(o => String(o.label).toLowerCase().includes(q))
})

function toggle() {
    if (props.disabled) return
    open.value = !open.value
}

function close() {
    open.value = false
    query.value = ''
}

function select(value) {
    emit('update:modelValue', value)
    emit('change', value)
    close()
}

function onClickOutside(e) {
    if (!root.value) return
    if (!root.value.contains(e.target)) close()
}

onMounted(() => document.addEventListener('mousedown', onClickOutside))
onBeforeUnmount(() => document.removeEventListener('mousedown', onClickOutside))
</script>
