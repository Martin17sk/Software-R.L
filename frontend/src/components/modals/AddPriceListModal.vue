<script setup>
import { computed, ref, watch } from 'vue'
import BaseModal from '@/components/common/BaseModal.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
    open: { type: Boolean, default: false },
})

const emit = defineEmits(['update:open', 'confirm'])

const name = ref('')

watch(() => props.open, (v) => {
    if (v) name.value = ''
})

const trimmed = computed(() => name.value.trim())
const error = computed(() => {
    if (!props.open) return ''
    if (!trimmed.value) return 'El nombre es obligatorio.'
    if (trimmed.value.length > 255) return 'Máximo 255 caracteres.'
    return ''
})

function close() {
    emit('update:open', false)
}

function confirm() {
    if (error.value) return
    emit('confirm', trimmed.value)
    close()
}
</script>

<template>
    <BaseModal :open="open" title="Agregar lista de precios" maxWidth="md" @update:open="v => emit('update:open', v)">
        <div class="flex flex-col gap-4">
            <p class="text-sm text-slate-600 leading-relaxed">
                Ingresa el nombre de la nueva lista de precios. Este nombre se utilizará para identificarla en el
                sistema.
            </p>

            <div class="flex flex-col gap-1">
                <BaseInputText label="Nombre de la lista" v-model="name" placeholder="" />
                <p v-if="error" class="text-xs text-red-600">{{ error }}</p>
            </div>
        </div>

        <template #footer>
            <div class="flex justify-end gap-3">
                <BaseButton label="Cancelar" variant="outline" class="w-[120px]" @click="close" />
                <BaseButton label="Agregar lista" variant="accept" class="w-[150px]" :disabled="!!error"
                    @click="confirm" />
            </div>
        </template>
    </BaseModal>
</template>
