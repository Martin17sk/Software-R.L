<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import BaseInputText from '@/components/common/BaseInputText.vue';
import BaseDropdown from '@/components/common/BaseDropdown.vue';
import BaseTextarea from '@/components/common/BaseTextarea.vue';
import BaseSwitch from '@/components/common/BaseSwitch.vue';

import IconLockClosed from '@/icons/lock-closed.svg';

import { computed, onMounted, ref, watch } from 'vue';
import { usePricingStore } from '@/stores/pricing.store';
import ConfirmPriceChangeModal from '@/components/modals/ConfirmPriceChangeModal.vue';
import FooterBar from '@/components/layout/FooterBar.vue';
import { useRegisterPrice } from '../composables/useRegisterPrice';
import { useNotifications } from '@/composables/useNotifications';
import { fetchArticuloPrecioActual, registrarCambioPrecioMultiple } from '../services/pricingApi';

const { notifySuccess, notifyError } = useNotifications()

const {
  articleCode,
  articleName,
  articleError,
  currentPrice,
  selectedPriceList,
  newPrice,
  priceError,
  note,
  fetchArticle,
  fetchCurrentPrice,
  registerPrice,
} = useRegisterPrice()


const pricingStore = usePricingStore()
onMounted(() => pricingStore.loadPriceLists())

const opciones = computed(() => pricingStore.options)

// Modal: datos reales
const precioAnteriorModal = computed(() => currentPrice.value ?? null)
const precioNuevoModal = computed(() => newPrice.value ?? null)
const listaSeleccionada = computed(() => {
  return opciones.value.find(o => String(o.value) === String(selectedPriceList.value))?.label ?? ''
})

// Varias listas
const variasListas = ref(false);
const duplicatedListError = ref('');

const priceRows = ref([
  { id: crypto.randomUUID(), lista: '', precioAnterior: null, precioNuevo: '', nota: '', showNota: false, loadingPrev: false, rowError: '' },
]);

function addRow() {
  priceRows.value.push({
    id: crypto.randomUUID(),
    lista: '',
    precioAnterior: '',
    precioNuevo: '',
    nota: '',
    showNota: false
  });
}

const canAddRow = computed(() => priceRows.value.length < opciones.value.length);

function removeRow(id) {
  if (priceRows.value.length <= 1) return
  priceRows.value = priceRows.value.filter(r => r.id !== id);
}

function toggleNota(row) {
  const willOpen = !row.showNota;
  priceRows.value.forEach(r => (r.showNota = false))
  row.showNota = willOpen;
}

async function setRowLista(row, value) {
  row.rowError = ''

  if (value === '' || value === null || value === undefined) {
    row.lista = '';
    row.precioAnterior = null
    duplicatedListError.value = '';
    return;
  }

  const exists = priceRows.value.some(r => r.id !== row.id && String(r.lista) === String(value));
  if (exists) {
    duplicatedListError.value = 'No puedes repetir la misma lista en "Varias listas".';
    return
  }

  row.lista = value;
  duplicatedListError.value = ''

  await fetchRowCurrentPrice(row)
}

function optionForRow(row) {
  const used = new Set(
    priceRows.value
      .filter(r => r.id !== row.id)
      .map(r => String(r.lista))
      .filter(v => v && v !== 'null' && v !== 'undefined')
  )

  return opciones.value.map(o => ({
    ...o,
    disabled: used.has(String(o.value))
  }));
}

const canRemove = computed(() => priceRows.value.length > 1);

// Modal
const confirmOpen = ref(false)

function openConfirm() {
  const codigo = String(articleCode.value || '').trim()
  if (!codigo) return notifyError('Ingresa el código del artículo')

  if (!variasListas.value) {
    if (!selectedPriceList.value) return notifyError('Selecciona una lista de precios')
    if (typeof newPrice.value !== 'number' || newPrice.value <= 0) return notifyError('Ingresa un precio nuevo válido')
    if ((note.value?.length ?? 0) > 255) return notifyError('La nota excede los 255 caracteres')
    confirmOpen.value = true
    return
  }

  if (duplicatedListError.value) return notifyError(duplicatedListError.value)

  // filas válidas
  const validRows = priceRows.value.filter(r => r.lista && Number(r.precioNuevo) > 0)
  if (!validRows.length) return notifyError('Debes ingresar al menos un cambio válido')

  // filas incompletas (lista sin precio o precio sin lista)
  const hasIncomplete = priceRows.value.some(r => {
    const hasList = !!r.lista
    const hasNew = r.precioNuevo !== '' && r.precioNuevo !== null && r.precioNuevo !== undefined
    const newOk = Number(r.precioNuevo) > 0
    return (hasList && !newOk) || (!hasList && hasNew)
  })
  if (hasIncomplete) return notifyError('Tienes filas incompletas: selecciona lista y un precio nuevo válido')

  // notas por fila
  const noteTooLong = priceRows.value.some(r => (r.nota?.length ?? 0) > 255)
  if (noteTooLong) return notifyError('Alguna nota de fila excede 255 caracteres')

  confirmOpen.value = true
}

function onNewPrice(v) {
  newPrice.value = v === '' ? null : Number(v)
}


async function onConfirm() {
  confirmOpen.value = false

  try {
    if (!variasListas.value) {
      // modo simple
      await registerPrice()
      notifySuccess('Precio registrado correctamente')
    } else {
      const cambios = priceRows.value
        .filter(r => r.lista && Number(r.precioNuevo) > 0)
        .map(r => ({
          listaPrecioId: Number(r.lista),
          precioNuevo: Number(r.precioNuevo),
          observacion: r.nota?.trim() ? r.nota.trim() : null
        }))

      if (!cambios.length) {
        notifyError('Debes ingresar al menos un cambio válido')
        return
      }

      await registrarCambioPrecioMultiple({
        articuloCodigo: articleCode.value,
        cambios,
        observacion: note.value?.trim() ? note.value.trim() : null
      })

      notifySuccess('Cambios registrados correctamente')
    }

    // Limpieza (cierre visual)
    selectedPriceList.value = null
    newPrice.value = null
    note.value = ''

    // modo multiple:
    priceRows.value = [
      { id: crypto.randomUUID(), lista: '', precioAnterior: '', precioNuevo: '', nota: '', showNota: false }
    ]
    duplicatedListError.value = ''
  } catch (e) {
    const msg =
      e?.response?.data?.message ||
      (typeof e?.response?.data === 'string' ? e.response.data : '') ||
      e?.message ||
      'Error registrando el precio'

    notifyError(msg)
    console.error(e)
  }
}

async function onArticleCommit() {
  try {
    await fetchArticle()
    
    if (!variasListas.value) {
      await fetchCurrentPrice()
    } else {
      await Promise.all(priceRows.value.map(r => (r.lista ? fetchRowCurrentPrice(r) : Promise.resolve())))
    }
  } catch {
    //
  }
}

async function fetchRowCurrentPrice(row) {
  row.rowError = ''
  row.precioAnterior = null

  const codigo = String(articleCode.value || '').trim()
  const listaId = row.lista

  if (!codigo || !listaId) return

  row.loadingPrev = true
  try {
    const res = await fetchArticuloPrecioActual(codigo, listaId)
    row.precioAnterior = res?.precioActual ?? null
    if (res?.nombre) articleName.value = res.nombre
  } catch (e) {
    row.precioAnterior = null
    row.rowError = e?.response?.data?.message || 'No se pudo cargar el precio actual'
    console.error(e)
  } finally {
    row.loadingPrev = false
  }
}

const multipleChangesForModal = computed(() => {
  if (!variasListas.value) return []
  return priceRows.value
    .filter(r => r.lista && Number(r.precioNuevo) > 0)
    .map(r => ({
      key: r.id,
      listName: opciones.value.find(o => String(o.value) === String(r.lista))?.label ?? '',
      prevPrice: r.precioAnterior,
      nextPrice: r.precioNuevo,
    }))
})

watch(variasListas, v => {
  if (v) {
    selectedPriceList.value = null
    newPrice.value = null
  } else {
    priceRows.value.forEach(r => (r.showNota = false))
  }
});

watch(articleCode, () => {
  articleName.value = ''
  articleError.value = ''
  currentPrice.value = null

  // Modo multiple
  priceRows.value.forEach(r => {
    r.precioAnterior = null
    r.rowError = ''
  })
})
</script>

<template>
  <section class="bg-[#F4F4F4] h-full w-full flex flex-col">
    <main class="flex-1 min-h-0 overflow-auto flex flex-col items-center">
      <div class="gap-[20px]">
        <h1>Cambio de precio de artículo</h1>
        <form action="" class="gap-[20px] flex flex-col">
          <div class="w-full place-content-between flex flex-row gap-4">

            <BaseInputText label="Códgo artículo" class="w-[125px]" v-model="articleCode"
              @keydown.enter.prevent="onArticleCommit" @blur="onArticleCommit" />

            <p v-if="articleError" class="text-xs text-red-600 mt-1">{{ articleError }}</p>
            <p v-if="priceError" class="text-xs text-red-600 mt-1">{{ priceError }}</p>


            <BaseInputText label="Nombre artículo" class="w-[400px]" :modelValue="articleName" :disabled="true">
              <template #iconRight>
                <IconLockClosed class="h-4 w-4" />
              </template>
            </BaseInputText>
          </div>

          <!-- Modo simple (variasListas = false) -->
          <div v-if="!variasListas" class="w-full flex flex-row place-content-between items-end gap-4">
            <BaseDropdown label="Lista de precios" placeholder="Seleccionar" v-model="selectedPriceList"
              :options="opciones" :searchable="true" class="w-[260px]" @update:modelValue="fetchCurrentPrice" />

            <BaseInputText label="Precio actual" :disabled="true"
              :modelValue="currentPrice === null ? '-' : `$${currentPrice}`" class="w-[125px]">
              <template #iconRight>
                <IconLockClosed class="h-4 w-4" />
              </template>
            </BaseInputText>

            <BaseInputText label="Precio nuevo" :modelValue="newPrice ?? ''" @update:modelValue="onNewPrice"
              class="w-[125px]" />
          </div>

          <div v-if="!variasListas" class="w-full">
            <BaseTextarea label="Nota (opcional)" placeholder="Escribe tu nota aquí" v-model="note" :rows="4"
              class="w-full" />
          </div>

          <!-- Modo varias listas (variasListas = true) -->
          <div v-else class="w-full flex flex-col gap-4">

            <div v-for="row in priceRows" :key="row.id" class="w-full flex flex-col gap-2">
              <!-- Fila principal -->
              <div class="w-full relative flex flex-row items-end gap-4">
                <!-- Botón eliminar -->
                <button v-if="canRemove" type="button" class="absolute -left-7 bottom-[10px] h-5 w-5 rounded-full border border-red-400 text-red-500
         flex items-center justify-center hover:bg-red-50 cursor-pointer" title="Eliminar lista"
                  @click="removeRow(row.id)">
                  <span class="block text-[12px] font-bold leading-[1] translate-y-[-1px]">−</span>
                </button>


                <BaseDropdown label="Lista de precios" placeholder="Seleccionar" :modelValue="row.lista"
                  @update:modelValue="val => setRowLista(row, val)" :options="optionForRow(row)" :searchable="true"
                  class="w-[260px]" />

                <BaseInputText label="Precio actual" :disabled="true"
                  :modelValue="row.loadingPrev ? 'Cargando...' : (row.precioAnterior === null ? '-' : `$${row.precioAnterior}`)"
                  class="w-[125px]">
                  <template #iconRight>
                    <IconLockClosed class="h-4 w-4" />
                  </template>
                </BaseInputText>

                <BaseInputText label="Precio nuevo" v-model="row.precioNuevo" class="w-[125px]" />

                <p v-if="row.rowError" class="text-xs text-red-600 pl-[29px]">
                  {{ row.rowError }}
                </p>


                <!-- Botón Nota -->
                <div class="absolute -right-20 bottom-[6px]">
                  <!-- label fantasma para calzar con BaseInputText/BaseDropdown -->
                  <span class="text-[14px] text-transparent select-none">.</span>

                  <button type="button"
                    class="inline-flex items-center gap-2 text-sm text-slate-500 hover:text-slate-700 h-[40px]"
                    @click="toggleNota(row)">
                    <span class="h-5 w-5 rounded-full border flex items-center justify-center transition-colors" :class="row.showNota
                      ? 'border-red-400 text-red-500 bg-red-50'
                      : 'border-slate-300 text-slate-500 bg-white'">
                      <!-- usa leading-none + flex, pero además fija line-height -->
                      <span class="block text-[12px] font-bold leading-[1] translate-y-[-1px]">
                        {{ row.showNota ? '−' : '+' }}
                      </span>
                    </span>

                    <span>Nota</span>
                  </button>
                </div>

              </div>

              <!-- Nota (solo de esta fila) -->
              <div v-show="row.showNota" class="pl-[29px]">
                <BaseTextarea label="Nota" placeholder="Escribe tu nota aquí" v-model="row.nota" :rows="4"
                  class="w-[570px]" />
              </div>
            </div>

            <!-- Botón agregar lista -->
            <div class="flex justify-center pt-2">
              <BaseButton label="Agregar lista" variant="secondary"
                class="w-[220px] bg-[#B8B8B8] hover:bg-[#A6A6A6] active:bg-[#8F8F8F]" :disabled="!canAddRow"
                @click="addRow" />
            </div>

            <div v-if="duplicatedListError" class="text-xs text-red-600 pl-[29px]">
              {{ duplicatedListError }}
            </div>
          </div>

        </form>
      </div>
    </main>

    <FooterBar>
      <template #center>
        <div class="flex flex-col justify-center items-center gap-[10px]">
          <div class="flex flex-row">
            <BaseButton label="Continuar" class="w-[200px] h-[40px]" @click="openConfirm" />
          </div>

          <div class="flex flex-row justify-center items-center gap-[10px]">
            <h1 class="text-sm text-slate-500">Varias listas</h1>
            <BaseSwitch v-model="variasListas" />
          </div>
        </div>
      </template>
    </FooterBar>

    <!-- Modal de confirmación -->
    <ConfirmPriceChangeModal 
      v-model:open="confirmOpen" 
      :multiple="variasListas" 
      :changes="multipleChangesForModal" 
      :multipleNote="note?.trim() ? note.trim() : ''" 
      :listName="listaSeleccionada" 
      :prevPrice="precioAnteriorModal"
      :nextPrice="precioNuevoModal" 
      @confirm="onConfirm" />
  </section>
</template>