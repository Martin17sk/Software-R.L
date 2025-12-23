<template>
  <div class="w-full">
    <div v-if="title" class="text-base font-semibold text-slate-900">
      {{ title }}
    </div>
    <div v-if="subtitle" class="mt-1 text-sm text-slate-500">
      {{ subtitle }}
    </div>

    <div class="mt-4 rounded-xl border-2 border-dashed px-6 py-8 text-center transition
             flex flex-col items-center justify-center gap-1" :class="dropzoneClasses" @dragenter.prevent="onDragEnter"
      @dragover.prevent="onDragOver" @dragleave.prevent="onDragLeave" @drop.prevent="onDrop" @click="openPicker"
      role="button" tabindex="0" @keydown.enter.prevent="openPicker" @keydown.space.prevent="openPicker">
      <div class="text-sm text-slate-900">
        <span>Drop file or</span>
        <button type="button" class="ml-1 font-semibold underline underline-offset-2" @click.stop="openPicker">
          Browse
        </button>
      </div>

      <div class="text-xs text-slate-500">
        {{ formatHint }}
      </div>

      <div v-if="fileName" class="mt-3 text-xs text-slate-700">
        Seleccionado: <span class="font-medium">{{ fileName }}</span>
      </div>

      <input ref="inputRef" type="file" class="hidden" :accept="accept" @change="onPick" />
    </div>

    <p v-if="error" class="mt-2 text-xs text-red-600">
      {{ error }}
    </p>
  </div>
</template>

<script setup>
import { preview } from 'vite'
import { computed, ref } from 'vue'

const props = defineProps({
  title: { type: String, default: 'Titulo' },
  subtitle: { type: String, default: 'Subtitulo' },

  // v-model:file
  file: { type: Object, default: null }, // File | null

  // por defecto solo .xls (puedes ampliar a .xlsx)
  accept: { type: String, default: '.xls,.xlsx' },

  // texto bajo “Drop file or Browse”
  formatHint: { type: String, default: 'Formato: xls, xlsx' },

  disabled: { type: Boolean, default: false },
})

const emit = defineEmits(['update:file', 'change'])

const inputRef = ref(null)
const isDragging = ref(false)
const error = ref('')

const fileName = computed(() => (props.file ? props.file.name : ''))

const dropzoneClasses = computed(() => {
  if (props.disabled) return 'bg-slate-100 border-slate-200 text-slate-400 cursor-not-allowed'
  if (error.value) return 'bg-red-50 border-red-300 cursor-pointer'
  if (isDragging.value) return 'bg-purple-50 border-purple-500 cursor-pointer'
  return 'bg-purple-50/40 border-purple-400 cursor-pointer'
})

function openPicker() {
  if (props.disabled) return
  error.value = ''
  inputRef.value?.click()
}

function validateFile(file) {
  error.value = ''

  const acceptList = props.accept
    .split(',')
    .map(s => s.trim().toLowerCase())
    .filter(Boolean)

  // valida por extensión (simple y suficiente para UI)
  const name = file.name.toLowerCase()
  const ok = acceptList.some(ext => name.endsWith(ext))
  if (!ok) {
    error.value = `Formato inválido. Se requiere: ${props.accept}`
    return false
  }
  return true
}

function setFile(file) {
  if (!validateFile(file)) return
  emit('update:file', file)
  emit('change', file)
}

function onPick(e) {
  const file = e.target.files?.[0]
  if (!file) return
  setFile(file)
  // permitir seleccionar el mismo archivo otra vez
  e.target.value = ''
}

function onDragEnter() {
  if (props.disabled) return
  isDragging.value = true
}

function onDragOver() {
  if (props.disabled) return
  isDragging.value = true
}

function onDragLeave() {
  isDragging.value = false
}

function onDrop(e) {
  if (props.disabled) return
  isDragging.value = false

  const file = e.dataTransfer?.files?.[0]
  if (!file) return
  setFile(file)
}
</script>
