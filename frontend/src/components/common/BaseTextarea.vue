<template>
  <div class="flex flex-col gap-1">
    <label v-if="label" class="form-label">
      {{ label }}
    </label>

    <textarea
      :value="modelValue"
      :placeholder="placeholder"
      :maxlength="maxLength"
      :rows="rows"
      :disabled="disabled"
      class="w-full resize-none rounded-lg border border-slate-300
             px-3 py-2 text-sm text-slate-900 placeholder:text-slate-400
             transition
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
      :class="disabled ? 'bg-slate-100 text-slate-500 cursor-not-allowed' : 'bg-white'"
      @input="onInput"
    />
    
    <!-- contador -->
    <div
      v-if="showCounter"
      class="text-xs text-slate-400 text-right"
    >
      {{ modelValue.length }} / {{ maxLength }}
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: String, default: '' },
  label: { type: String, default: '' },
  placeholder: { type: String, default: '' },
  maxLength: { type: Number, default: 255 },
  rows: { type: Number, default: 4 },
  disabled: { type: Boolean, default: false },
  showCounter: { type: Boolean, default: true },
})

const emit = defineEmits(['update:modelValue'])

function onInput(e) {
  emit('update:modelValue', e.target.value)
}
</script>
