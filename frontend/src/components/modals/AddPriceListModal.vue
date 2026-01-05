<script setup>
import { computed, ref, watch, onMounted } from 'vue'
import { fetchSystems } from '@/modules/config/services/configApi'
import BaseModal from '@/components/common/BaseModal.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseDropdown from '../common/BaseDropdown.vue'

const props = defineProps({
    open: { type: Boolean, default: false },

    // Vienen del padre
    serverError: { type: String, default: '' },
    submitting: { type: Boolean, default: false },
})

const emit = defineEmits(['update:open', 'confirm'])

const systems = ref([])
const sistemaId = ref("")
const nombreLista = ref("")

const systemOptions = computed(() =>
    systems.value.map(s => ({ value: s.id, label: s.nombre }))
)

watch(() => props.open, (v) => {
    if (v) {
        nombreLista.value = ""
        sistemaId.value = ""
    }
})

const trimmed = computed(() => nombreLista.value.trim())

const error = computed(() => {
    if (!props.open) return ''
    if (!sistemaId.value) return 'Debes seleccionar un sistema'
    if (!trimmed.value) return 'El nombre es obligatorio.'
    if (trimmed.value.length > 100) return 'Máximo 100 caracteres.'
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
        nombre: trimmed.value,
        sistemaId: Number(sistemaId.value)
    })
}

onMounted(async () => {
    systems.value = await fetchSystems()
})

</script>

<template>
    <BaseModal :open="open" title="Agregar lista de precios" maxWidth="md" @update:open="v => emit('update:open', v)">
        <div class="flex flex-col gap-4">
            <p class="text-sm text-slate-600 leading-relaxed">
                Ingresa el nombre de la nueva lista de precios. Este nombre se utilizará para identificarla en el
                sistema.
            </p>

            <div class="flex flex-col gap-2">
                <BaseDropdown label="Sistema" placeholder="Seleccionar" v-model="sistemaId" :options="systemOptions"
                    :searchable="false" :disabled="submitting"/>
                <BaseInputText label="Nombre de la lista" v-model="nombreLista" :disabled="submitting"/>
                <p v-if="error" class="text-xs text-red-600">{{ error }}</p>
                <p v-else-if="serverError" class="text-sm text-red-600">{{ serverError }}</p>
            </div>
        </div>

        <template #footer>
            <div class="flex justify-end gap-3">
                <BaseButton label="Cancelar" variant="outline" class="w-[120px]" :disabled="submitting" @click="close" />
                <BaseButton label="Crear lista" variant="accept" class="w-[150px]" :disabled="!!error || submitting"
                    @click="submit" />
            </div>
        </template>
    </BaseModal>
</template>
