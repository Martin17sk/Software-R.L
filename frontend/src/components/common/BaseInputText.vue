<template>
  <div class="flex flex-col gap-1">
    <!-- Label -->
    <label v-if="label" :for="id" class="text-[14px] font-regular text-[#545F71]">
      {{ label }}
    </label>

    <!-- Wrapper -->
    <div class="relative w-full">

      <!-- Icono izquierdo -->
      <span v-if="$slots.iconLeft"
        class="absolute inset-y-0 left-3 flex items-center text-slate-400 pointer-events-none">
        <slot name="iconLeft" />
      </span>

      <!-- Input -->
      <input :id="id" :type="type" :value="modelValue" :disabled="disabled" :placeholder="placeholder" @input="onInput"
        class="w-full rounded-lg border border-slate-300 py-2 text-sm
               text-slate-900 placeholder:text-slate-400
               transition
               focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        :class="[disabledClasses, inputPaddingClasses]" />

      <!-- Icono derecho -->
      <span v-if="$slots.iconRight"
        class="absolute inset-y-0 right-3 flex items-center text-slate-400 pointer-events-none">
        <slot name="iconRight" />
      </span>

    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  label: { type: String, default: '' },
  placeholder: { type: String, default: '' },
  type: { type: String, default: 'text' },
  disabled: { type: Boolean, default: false },
  id: {
    type: String,
    default: () => `input-${Math.random().toString(36).slice(2)}`,
  },
})

const emit = defineEmits(['update:modelValue'])

const disabledClasses = computed(() =>
  props.disabled
    ? 'bg-slate-100 text-slate-500 cursor-not-allowed'
    : 'bg-white'
)

const inputPaddingClasses = computed(() => {
  if (props.disabled) return 'px-3'
  // padding dinámico según slots
  return [
    props.$slots?.iconLeft ? 'pl-10' : 'pl-3',
    props.$slots?.iconRight ? 'pr-10' : 'pr-3'
  ]
})

function onInput(event) {
  emit('update:modelValue', event.target.value)
}
</script>
