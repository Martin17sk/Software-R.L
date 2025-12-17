<template>
  <BaseModal v-model:open="openProxy" title="Confirmar cambio de precio" maxWidth="sm">
    <div class="space-y-4">
      <p class="text-sm text-slate-600">
        Vas a registrar el siguiente cambios:
      </p>

      <ul class="list-disc pl-5 text-sm text-slate-600 space-y-1">
        <li>
          Lista de precio:
          <span class="font-medium text-slate-900">{{ listName || '[nombre]' }}</span>
        </li>
        <li>
          Precio anterior:
          <span class="font-medium text-slate-900">{{ formattedPrev }}</span>
        </li>
        <li>
          Precio nuevo:
          <span class="font-medium text-slate-900">{{ formattedNext }}</span>
        </li>
      </ul>

      <p class="text-sm text-slate-600">
        Este registro quedará guardado en el historial y solo se podrá eliminar dentro de
        <span class="font-semibold text-slate-900">24 horas</span>
        desde la confirmación. Pasado ese plazo,
        <span class="font-semibold underline underline-offset-2">no</span>
        podrán modificarse.
      </p>
    </div>

    <template #footer>
      <div class="flex justify-end gap-3">
        <BaseButton variant="outline" label="Cancelar" @click="openProxy = false" />
        <BaseButton variant="accept" label="Confirmar" @click="confirm" />
      </div>
    </template>
  </BaseModal>
</template>

<script setup>
import { computed } from 'vue'
import BaseModal from '@/components/common/BaseModal.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
  open: { type: Boolean, default: false },

  listName: { type: String, default: '' },
  prevPrice: { type: [Number, String], default: '' },
  nextPrice: { type: [Number, String], default: '' },
})

const emit = defineEmits(['update:open', 'confirm'])

const openProxy = computed({
  get: () => props.open,
  set: (v) => emit('update:open', v),
})

function toNumber(v) {
  const n = typeof v === 'number' ? v : Number(String(v).replace(/[^\d.-]/g, ''))
  return Number.isFinite(n) ? n : null
}

const formattedPrev = computed(() => {
  const n = toNumber(props.prevPrice)
  return n == null ? `$[precio]` : n.toLocaleString('es-CL')
})

const formattedNext = computed(() => {
  const n = toNumber(props.nextPrice)
  return n == null ? `$[precio]` : n.toLocaleString('es-CL')
})

function confirm() {
  emit('confirm')
  openProxy.value = false
}
</script>
