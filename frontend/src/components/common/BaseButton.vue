<template>
  <button type="button" :disabled="disabled" class="inline-flex items-center justify-center rounded-lg font-medium
           px-4 py-2 text-sm transition
           focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2"
    :class="[variantClasses, disabledClasses]" @click="onClick">

    <!-- Icono izquierdo -->
    <span v-if="$slots.iconLeft" class="flex items-center text-inherit">
      <slot name="iconLeft" />
    </span>


    <!-- Texto -->
    <slot>{{ label }}</slot>

    <!-- Icono derecho -->
    <span v-if="$slots.iconRight" class="flex items-center text-inherit">
      <slot name="iconRight" />
    </span>

  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  label: { type: String, default: 'Button' },
  disabled: { type: Boolean, default: false },
  variant: { type: String, default: 'accept' }, // accept | danger | ghost | outline
})

const emit = defineEmits(['click'])

const variantClasses = computed(() => {
  switch (props.variant) {
    case 'accept':
      return 'bg-[#34C759] text-white hover:bg-[#2FAF4F] focus-visible:ring-[#34C759]'
    case 'secondary':
      return 'bg-[#B8B8B8] text-white hover:bg-[#A6A6A6] active:bg-[#8F8F8F] focus-visible:ring-[#B8B8B8]'
    case 'danger':
      return 'bg-[#DC2626] text-white hover:bg-[#B91C1C] focus-visible:ring-[#DC2626]'
    case 'ghost':
      return `
        bg-transparent
        text-slate-900
        p-0
        hover:underline
        focus-visible:ring-0
      `
    case 'outline':
      return `
        bg-transparent
        border
        border-[#D0D5DD]
        text-[#545F71]
        hover:bg-[#4880FF]/10
        focus-visible:ring-[#4880FF]
      `
    default:
      return 'bg-slate-900 text-white hover:bg-slate-800 focus-visible:ring-slate-400'
  }
})


const disabledClasses = computed(() =>
  props.disabled ? 'opacity-50 cursor-not-allowed pointer-events-none' : 'cursor-pointer'
)

function onClick(e) {
  if (props.disabled) return
  emit('click', e)
}
</script>
