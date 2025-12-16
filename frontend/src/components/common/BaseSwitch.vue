<template>
  <button
    type="button"
    role="switch"
    :aria-checked="modelValue"
    :disabled="disabled"
    class="relative inline-flex h-6 w-11 shrink-0 items-center rounded-full
           transition-colors duration-200 focus:outline-none
           focus-visible:ring-2 focus-visible:ring-offset-2"
    :class="trackClasses"
    @click="toggle"
  >
    <!-- Thumb -->
    <span
      class="inline-block h-5 w-5 transform rounded-full bg-white shadow
             transition-transform duration-200"
      :class="thumbClasses"
    />
  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
})

const emit = defineEmits(['update:modelValue', 'change'])

function toggle() {
  if (props.disabled) return
  emit('update:modelValue', !props.modelValue)
  emit('change', !props.modelValue)
}

const trackClasses = computed(() => {
  if (props.disabled) {
    return props.modelValue
      ? 'bg-slate-300 cursor-not-allowed'
      : 'bg-slate-200 cursor-not-allowed'
  }

  return props.modelValue
    ? 'bg-[#4880FF] cursor-pointer focus-visible:ring-[#4880FF]'
    : 'bg-slate-300 cursor-pointer focus-visible:ring-slate-400'
})

const thumbClasses = computed(() =>
  props.modelValue ? 'translate-x-5' : 'translate-x-1'
)
</script>
