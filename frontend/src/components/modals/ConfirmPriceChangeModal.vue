<template>
  <BaseModal v-model:open="openProxy" title="Confirmar cambio de precio" maxWidth="sm">
    <div class="space-y-4">
      <p class="text-sm text-slate-600">
        Vas a registrar el siguiente cambios:
      </p>

      <!-- MODO MULTIPLE -->
      <div v-if="multiple" class="space-y-3">
        <p class="text-sm text-slate-600">
          Se registrarán <span class="font-semibold text-slate-900">{{ validCount }}</span>
          cambios en distintas listas.
        </p>

        <div class="max-h-[240px] overflow-auto border rounded-md bg-white">
          <table class="w-full text-sm">
            <thead class="sticky top-0 bg-white border-b">
              <tr class="text-left text-slate-600">
                <th class="p-2">Lista</th>
                <th class="p-2 text-right">Anterior</th>
                <th class="p-2 text-right">Nuevo</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(c, idx) in validChanges" :key="c.key ?? idx" class="border-b las:border-b-0">
                <td class="p-2">
                  <span class="font-medium text-slate-900">
                    {{ c.listName || `[lista]` }}
                  </span>
                </td>
                <td class="p-2 text-right text-slate-700">
                  {{ formatMoney(c.prevPrice) }}
                </td>
                <td class="p-2 text-right font-medium text-slate-900">
                  {{ formatMoney(c.nextPrice) }}
                </td>
              </tr>

              <tr v-if="!validChanges.length">
                <td class="p-3 text-slate-500" colspan="3">
                  No hay cambios válidos para confirmar
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- MODO SIMPLE -->
      <ul v-else class="list-disc pl-5 text-sm text-slate-600 space-y-1">
        <li>
          Lista de precio:
          <span class="font-medium text-slate-900">{{ listName || `[nombre]` }}</span>
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
        <BaseButton variant="accept" label="Confirmar" :disabled="multiple && !validChanges.length" @click="confirm" />
      </div>
    </template>
  </BaseModal>
</template>

<script setup>
import { computed } from 'vue'

// Componentes
import BaseModal from '@/components/common/BaseModal.vue'
import BaseButton from '@/components/common/BaseButton.vue'

const props = defineProps({
  open: { type: Boolean, default: false },

  // modo simple
  listName: { type: String, default: '' },
  prevPrice: { type: [Number, String], default: '' },
  nextPrice: { type: [Number, String], default: '' },

  // modo multiple
  multiple: { type: Boolean, default: false },
  changes: {
    type: Array,
    default: () => [], // [{ key?, listName, prevPrice, nextPrice }]
  },
  multipleNote: { type: String, default: '' }
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

function formatMoney(v) {
  const n = toNumber(v)
  return n == null ? '-' : `$${n.toLocaleString('es-CL')}`
}

const formattedPrev = computed(() => formatMoney(props.prevPrice))
const formattedNext = computed(() => formatMoney(props.nextPrice))

const validChanges = computed(() => 
  (props.changes ?? [])
    .filter(c => c && c.listName && toNumber(c.nextPrice) > 0)
)

const validCount = computed(() => validChanges.value.length)

function confirm() {
  emit('confirm')
  openProxy.value = false
}
</script>
