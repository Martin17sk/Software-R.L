<template>
    <Teleport to="body">
        <div v-if="open" class="fixed inset-0 z-[9999]" @keydown.esc="emitClose">
            <!-- Overlay -->
            <div class="absolute inset-0 bg-black/40" @click="closeOnOverlay ? emitClose() : null" />

            <!-- Dialog -->
            <div class="absolute inset-0 flex items-center justify-center p-4">
                <div class="w-full rounded-2xl bg-white shadow-xl" :class="maxWidthClass" role="dialog"
                    aria-modal="true">
                    <!-- Header -->
                    <div v-if="title" class="px-6 pt-5 pb-3 border-b border-slate-200">
                        <h3 class="text-base font-semibold text-slate-900">
                            {{ title }}
                        </h3>
                    </div>

                    <!-- Body -->
                    <div class="px-6 py-5">
                        <slot />
                    </div>

                    <!-- Footer -->
                    <div v-if="$slots.footer" class="px-6 pb-5">
                        <slot name="footer" />
                    </div>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script setup>
import { computed, watch } from 'vue'

const props = defineProps({
    open: { type: Boolean, default: false },
    title: { type: String, default: '' },
    closeOnOverlay: { type: Boolean, default: true },
    maxWidth: { type: String, default: 'md' }, // sm | md | lg | xl
})

const emit = defineEmits(['update:open', 'close'])

const maxWidthClass = computed(() => {
    switch (props.maxWidth) {
        case 'sm': return 'max-w-sm'
        case 'lg': return 'max-w-2xl'
        case 'xl': return 'max-w-4xl'
        default: return 'max-w-lg'
    }
})

function emitClose() {
    emit('update:open', false)
    emit('close')
}

// opcional pero recomendado: bloquear scroll del body cuando el modal está abierto
watch(
    () => props.open,
    (v) => {
        document.body.style.overflow = v ? 'hidden' : ''
    }
)
</script>
