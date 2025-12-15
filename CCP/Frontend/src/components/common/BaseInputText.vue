<template>
  <div class="flex flex-col gap-1">
    <!-- Label -->
    <label
      v-if="label"
      :for="id"
      class="text-[14px] font-regular color-[#545F71]"
    >
      {{ label }}
    </label>

    <!-- Input -->
    <input
      :id="id"
      :type="type"
      :value="modelValue"
      :disabled="disabled"
      :placeholder="placeholder"
      class="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm
             text-slate-900 placeholder:text-slate-400
             transition
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
      :class="disabledClasses"
      @input="onInput"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  /** v-model */
  modelValue: {
    type: String,
    default: '',
  },

  /** Texto del label */
  label: {
    type: String,
    default: '',
  },

  /** Placeholder */
  placeholder: {
    type: String,
    default: '',
  },

  /** Tipo de input */
  type: {
    type: String,
    default: 'text',
  },

  /** Disabled */
  disabled: {
    type: Boolean,
    default: false,
  },

  /** id opcional (para accesibilidad) */
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

function onInput(event) {
  emit('update:modelValue', event.target.value)
}
</script>
