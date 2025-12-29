<script setup>
// ----- IMPORTS -----

// Utilidades
import { computed, ref, watch, onMounted } from 'vue'

// Servicios
import { fetchArticulos } from '@/modules/config/services/configApi'

// Componentes
import BaseModal from '@/components/common/BaseModal.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseDropdown from '../common/BaseDropdown.vue'

// ----- CONSTS -----

// Props
const props = defineProps({
    open: { type: Boolean, default: false },

    // Vienen del padre
    serverError: { type: String, default: '' },
    submitting: { type: Boolean, default: false },
})

// Emit
const emit = defineEmits(['update:open', 'confirm'])

// Refs
const articulos = ref([])
const codigoArticulo = ref("")
const nombreArticulo = ref("")
const umArticulo = ref("")

// Computeds
const articuloOptions = computed(() =>
    articulos.value.map(a => ({ value: a.codigo, label: `${a.codigo} — ${a.nombre}` }))
)

const nombreTrimmed = computed(() => nombreArticulo.value.trim())
const umTrimmed = computed(() => umArticulo.value.trim())

const error = computed(() => {
    if (!props.open) return ''
    if (!codigoArticulo.value) return 'Debes seleccionar un artículo'
    if (!nombreTrimmed.value) return 'El nombre es obligatorio.'
    if (!umTrimmed.value) return 'La unidad de medida es obligatoria.'
    if (nombreTrimmed.value.length > 150) return 'El nombre supera el máximo permitido de caracteres (150)'
    if (umTrimmed.value.length > 40) return 'La unidad de medida supera el máximo permitido de caracteres (40)'
    return ''
})

// ----- FUNCIONES -----
function close() {
    if (props.submitting) return
    emit('update:open', false)
}

function submit () {
    if (props.submitting) return
    if (error.value) return

    emit('confirm', {
        codigo: codigoArticulo.value,
        nombre: nombreTrimmed.value,
        unidadMedida: umTrimmed.value
    })
}

// ----- WATCHES -----
watch(
    [codigoArticulo, articulos],
    ([codigo, lista]) => {
        if (!codigo) {
            nombreArticulo.value = ''
            umArticulo.value = ''
            return
        }

        const articulo = (lista ?? []).find(a => String(a.codigo) === String(codigo))

        if (!articulo) {
            nombreArticulo.value = ''
            umArticulo.value = ''
            return
        }

        nombreArticulo.value = articulo.nombre ?? ''
        umArticulo.value = articulo.unidadMedida ?? ''
    },
    { immediate: true }
)

watch(() => props.open, (v) => {
    if (v) {
        codigoArticulo.value = ""
        nombreArticulo.value = ""
        umArticulo.value = ""
    }
})

// ----- MOUNTED -----
onMounted(async () => {
    articulos.value = await fetchArticulos()
})
</script>

<template>
    <BaseModal :open="open" title="Editar artículo existente" maxWidth="md" @update:open="v => emit('update:open', v)">
        <div class="flex flex-col gap-4">
            <p class="text-sm text-slate-600 leading-relaxed">
                Selecciona el artículo a editar. Los cambios quedaran guardados en el sistema
            </p>

            <div class="flex flex-col gap-2">
                <BaseDropdown label="Artículo" placeholder="Seleccionar" v-model="codigoArticulo" :options="articuloOptions"
                    :searchable="true" :disabled="submitting"/>
                <BaseInputText label="Nombre" v-model="nombreArticulo" :disabled="submitting"/>
                <BaseInputText label="UM" v-model="umArticulo" :disabled="submitting"/>

                <p v-if="error" class="text-xs text-red-600">{{ error }}</p>
                <p v-else-if="serverError" class="text-sm text-red-600">{{ serverError }}</p>
            </div>
        </div>

        <template #footer>
            <div class="flex justify-end gap-3">
                <BaseButton label="Cancelar" variant="outline" class="w-[120px]" :disabled="submitting" @click="close" />
                <BaseButton label="Guardar cambios" variant="accept" class="w-[150px]" :disabled="!!error || submitting"
                    @click="submit" />
            </div>
        </template>
    </BaseModal>
</template>
