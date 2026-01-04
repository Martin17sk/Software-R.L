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

const nombreUsuario = ref("")
const contrasenaUsuario = ref("")
const confirmarContrasenaUsuario = ref("")
const activo = ref(true)

watch(() => props.open, (v) => {
    if (!v) return
    nombreUsuario.value = ''
    contrasenaUsuario.value = ''
    confirmarContrasenaUsuario.value = ''
    activo.value = true
})

const nombreTrimmed = computed(() => nombreUsuario.value.trim())

const error = computed(() => {
    if (!props.open) return ''

    if (!nombreTrimmed.value) return 'El nombre de usuario es obligatorio'
    if (nombreTrimmed.value.length > 100) return 'El nombre de usuario supera el máximo permitido de caracteres (100)'

    if (!contrasenaUsuario.value) return 'La contraseña es obligatoria'
    if (contrasenaUsuario.value.length < 6) return 'La contraseña debe tener al menos 6 caracteres'
    if (contrasenaUsuario.value.length > 100) return 'La contraseña debe tener máximo 100 caracteres'

    if (contrasenaUsuario.value !== confirmarContrasenaUsuario.value) return 'Las contraseñas no coinciden'
    
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
        nombre: nombreTrimmed.value,
        contrasena: contrasenaUsuario.value,
        activo: activo.value
    })
}
</script>

<template>
    <BaseModal :open="open" title="Agregar usuario" maxWidth="md" @update:open="v => emit('update:open', v)">
        <div class="flex flex-col gap-4">
            <p class="text-sm text-slate-600 leading-relaxed">
                Ingresa las credenciales del usuario a crear.
            </p>

            <div class="flex flex-col gap-2">
                <BaseInputText label="Nombre de usuario" v-model="nombreUsuario" :disabled="submitting"/>
                <BaseInputText label="Contraseña" v-model="contrasenaUsuario" :disabled="submitting" type="password"/>
                <BaseInputText label="Confirmar contraseña" v-model="confirmarContrasenaUsuario" :disabled="submitting" type="password"/>
                
                <p v-if="error" class="text-xs text-red-600">{{ error }}</p>
                <p v-else-if="serverError" class="text-sm text-red-600">{{ serverError }}</p>
            </div>
        </div>

        <template #footer>
            <div class="flex justify-end gap-3">
                <BaseButton label="Cancelar" variant="outline" class="w-[120px]" :disabled="submitting" @click="close" />
                <BaseButton label="Crear usuario" variant="accept" class="w-[150px]" :disabled="!!error || submitting"
                    @click="submit" />
            </div>
        </template>
    </BaseModal>
</template>
