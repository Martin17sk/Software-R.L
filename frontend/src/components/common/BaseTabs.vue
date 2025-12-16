<template>
  <div class="w-full rounded-xl bg-slate-100 p-1">
    <div class="flex gap-1">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        type="button"
        class="flex-1 rounded-lg px-4 py-3 text-sm font-semibold transition
               focus:outline-none focus-visible:ring-2 focus-visible:ring-offset-2"
        :class="tabClasses(tab.value)"
        @click="select(tab.value)"
      >
        <span class="truncate">{{ tab.label }}</span>
      </button>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: [String, Number], required: true },
  tabs: {
    type: Array,
    required: true,
    // [{ value: 'pricing', label: 'Cambio de precio de producto' }, ...]
  },
  disabled: { type: Boolean, default: false },
})

const emit = defineEmits(['update:modelValue', 'change'])

function select(value) {
  if (props.disabled) return
  emit('update:modelValue', value)
  emit('change', value)
}

function tabClasses(value) {
  const isActive = value === props.modelValue

  if (props.disabled) {
    return isActive
      ? 'bg-white text-slate-500 shadow-sm opacity-70 cursor-not-allowed'
      : 'bg-transparent text-slate-400 opacity-70 cursor-not-allowed'
  }

  return isActive
    ? 'bg-white text-slate-700 shadow-sm focus-visible:ring-slate-300'
    : 'bg-transparent text-slate-500 hover:text-slate-700 hover:bg-white/50 focus-visible:ring-slate-200'
}
</script>
