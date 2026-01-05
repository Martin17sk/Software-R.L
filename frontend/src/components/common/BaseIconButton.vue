<template>
    <button type="button" class="
      inline-flex items-center justify-center
      rounded-lg p-2
      transition
      focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 cursor-pointer
    " :class="computedClasses" :title="title" :aria-label="title" @click="$emit('click')">
        <slot />
    </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    variant: {
        type: String,
        default: 'default', // default | ghost | danger
    },
    title: {
        type: String,
        default: '',
    },
})

defineEmits(['click'])

const computedClasses = computed(() => {
    switch (props.variant) {
        case 'ghost':
            return 'text-slate-500 hover:text-slate-700 hover:bg-slate-200'
        case 'danger':
            return 'text-red-500 hover:text-red-700 hover:bg-red-50'
        default:
            return 'text-slate-600 hover:text-slate-800 hover:bg-slate-200'
    }
})
</script>
