<script setup>
import { computed, ref, watch } from 'vue'
import BaseModal from '@/components/common/BaseModal.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
    open: { type: Boolean, default: false },

    // Vienen del padre
    serverError: { type: String, default: '' },
    submitting: { type: Boolean, default: false },
})

const emit = defineEmits(['update:open', 'confirm'])

const codigoArticulo = ref("")
const nombreArticulo = ref("")
const umArticulo = ref("")

watch(() => props.open, (v) => {
    if (v) {
        codigoArticulo.value = ""
        nombreArticulo.value = ""
        umArticulo.value = ""
    }
})

const codigoTrimmed = computed(() => codigoArticulo.value.trim())
const nombreTrimmed = computed(() => nombreArticulo.value.trim())
const umTrimmed = computed(() => umArticulo.value.trim())

const error = computed(() => {
    if (!props.open) return ''
    if (!codigoTrimmed.value) return 'El código es obligatorio'
    if (!nombreTrimmed.value) return 'El nombre es obligatorio.'
    if (!umTrimmed.value) return 'La Unidad de Medida es obligatoria'
    if (codigoTrimmed.value.length > 10) return 'El código supera el máximo permitido de caracteres (10)'
    if (nombreTrimmed.value.length > 150) return 'El nombre supera el máximo permitido de caracteres (150)'
    if (umTrimmed.value.length > 40) return 'La unidad de medida supera el máximo permitido de caracteres (40)'
    return ''
})

function close() {
    if (props.submitting) return
    emit('update:open', false)
}

function submit () {
    if (props.submitting) return
    if (error.value) return

    emit('confirm', {
        codigo: codigoTrimmed.value,
        nombre: nombreTrimmed.value,
        unidadMedida: umTrimmed.value
    })
}
</script>

<template>
    <BaseModal :open="open" title="Agregar artículo" maxWidth="md" @update:open="v => emit('update:open', v)">
        <div class="flex flex-col gap-4">
            <p class="text-sm text-slate-600 leading-relaxed">
                Ingresa el código, nombre y unidad de medida del artículo. Esta información se usara para identificar al artículo en el sistema
            </p>

            <div class="flex flex-col gap-2">
                <BaseInputText label="Código" v-model="codigoArticulo" :disabled="submitting" class="w-[120px]"/>
                <BaseInputText label="Nombre" v-model="nombreArticulo" :disabled="submitting"/>
                <BaseInputText label="UM" v-model="umArticulo" :disabled="submitting"/>
                <p v-if="error" class="text-xs text-red-600">{{ error }}</p>
                <p v-else-if="serverError" class="text-sm text-red-600">{{ serverError }}</p>
            </div>
        </div>

        <template #footer>
            <div class="flex justify-end gap-3">
                <BaseButton label="Cancelar" variant="outline" class="w-[120px]" :disabled="submitting" @click="close" />
                <BaseButton label="Crear artículo" variant="accept" class="w-[150px]" :disabled="!!error || submitting"
                    @click="submit" />
            </div>
        </template>
    </BaseModal>
</template>
