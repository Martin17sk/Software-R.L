<script setup>
import BaseButton from '@/components/common/BaseButton.vue';
import BaseInputText from '@/components/common/BaseInputText.vue';
import BaseDropdown from '@/components/common/BaseDropdown.vue';
import BaseTextarea from '@/components/common/BaseTextarea.vue';
import BaseTabs from '@/components/common/BaseTabs.vue';
import BaseSwitch from '@/components/common/BaseSwitch.vue';

import Lock from '@/icons/Lock.png';

import { computed, ref, watch } from 'vue';
import ConfirmPriceChangeModal from '@/components/modals/ConfirmPriceChangeModal.vue';
import HeaderBar from '@/components/layout/HeaderBar.vue';

// --- Listas de precios (Traer de BD) ---
const opciones = [
  { value: 1, label: 'Lista de Precios 1' },
  { value: 2, label: 'Lista de Precios 2' },
  { value: 3, label: 'Lista de Precios 3' },
]

const tab = ref('precio');

const tabs = [
  { value: 'precio', label: 'Cambio de precio de artículo' },
  { value: 'comparar', label: 'Comparar listas de precios' },
]

const notaSimple = ref('');

const variasListas = ref(false);
const lista = ref('');

const duplicatedListError = ref('');

function goToHistory() {
  // router.push('/historial')
}

function goToSettings() {
  // abrir modal de configuración
}

const priceRows = ref([
  { id: crypto.randomUUID(), lista: '', precioAnterior: '$5.000', precioNuevo: '', nota: '', showNota: false },
]);

function addRow() {
  priceRows.value.push({ id: crypto.randomUUID(), lista: '', precioAnterior: '$5.000', precioNuevo: '', nota: '', showNota: false });
}

const canAddRow = computed(() => priceRows.value.length < opciones.length);

function removeRow(id) {
  if (priceRows.value.length <= 1) return
  priceRows.value = priceRows.value.filter(r => r.id !== id);
}

function toggleNota(row) {
  const willOpen = !row.showNota;

  // Cerrar todas las notas
  priceRows.value.forEach(r => {
    r.showNota = false;
  });

  // Abrir o cerrar la nota de la fila actual
  row.showNota = willOpen;
}

function setRowLista(row, value) {
  if (value === '' || value === null || value === undefined) {
    row.lista = '';
    duplicatedListError.value = '';
    return;
  }

  const exists = priceRows.value.some(r => r.id !== row.id && String(r.lista) === String(value));

  if (exists) {
    duplicatedListError.value = 'No puedes repetir la misma lista en "Varias listas".';
    return
  }

  row.lista = value;
  duplicatedListError.value = '';
}

function optionForRow(row) {
  const used = new Set(
    priceRows.value
      .filter(r => r.id !== row.id)
      .map(r => String(r.lista))
      .filter(v => v && v !== 'null' && v !== 'undefined')
  )

  return opciones.map(o => ({
    ...o,
    disabled: used.has(String(o.value))
  }));
}

function validateUniqueLists() {
  const chosen = priceRows.value.map(r => String(r.lista)).filter(v => v);
  return new Set(chosen).size === chosen.length;
}

const canRemove = computed(() => priceRows.value.length > 1);

// --- Modal ---
const confirmOpen = ref(false);

function onContinuar() {
  if (variasListas.value && !validateUniqueLists) {
    alert('No puedes repetir la misma lista en varias filas.')
    return
  }

  confirmOpen.value = true;
}

function onConfirm() {
  confirmOpen.value = false;
  // Lógica para continuar con el proceso
}

watch(variasListas, (v) => {
  if (v) {
    notaSimple.value = ''
  } else {
    priceRows.value.forEach(r => (r.showNota = false))
  }
});



// --- Modal info ---
const precioAnterior = ref(5000);
const precioNuevo = ref(6000);

const listaSeleccionada = computed(() => {
  return opciones.find(o => o.value === lista.value)?.label ?? '';
});
</script>

<template>
  <section class="bg-[#F4F4F4] h-screen w-screen flex flex-col justify-center items-center place-content-between">
    <HeaderBar>
      <template #center>
        <BaseTabs v-model="tab" :tabs="tabs" />
      </template>
    </HeaderBar>

    <main class="h-full">
      <div class="gap-[20px]">
        <h1>Cambio de precio de artículo</h1>
        <form action="" class="gap-[20px] flex flex-col">
          <div class="w-full place-content-between flex flex-row gap-4">
            <BaseInputText label="Códgo artículo" class="w-[125px]" />
            <BaseInputText label="Nombre artículo" class="w-[400px]" :iconRight="Lock" :disabled="true" />
          </div>

          <!-- Modo simple (variasListas = false) -->
          <div v-if="!variasListas" class="w-full flex flex-row place-content-between items-end gap-4">
            <BaseDropdown label="Lista de precios" placeholder="Seleccionar" v-model="lista" :options="opciones"
              :searchable="true" class="w-[260px]" />

            <BaseInputText label="Precio anterior" :disabled="true" :modelValue="'$5.000'" class="w-[125px]" />

            <BaseInputText label="Precio nuevo" v-model="precioNuevo" class="w-[125px]" />
          </div>

          <div v-if="!variasListas" class="w-full">
            <BaseTextarea label="Nota (opcional)" placeholder="Escribe tu nota aquí" v-model="notaSimple" :rows="4"
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

                <BaseInputText label="Precio anterior" :disabled="true" :modelValue="row.precioAnterior"
                  class="w-[125px]" />

                <BaseInputText label="Precio nuevo" v-model="row.precioNuevo" class="w-[125px]" />

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

    <footer class="flex flex-col border-t border-t-black gap-[30px] w-full h-[180px] justify-center items-center">
      <div class="flex flex-row gap-[30px]">
        <BaseButton label="Borrar todo" variant="outline" class="w-[250px]" />
        <BaseButton label="Continuar" variant="accept" class="w-[250px]" @click="onContinuar" />
      </div>

      <div class="flex flex-row justify-center items-center gap-[10px]">
        <h1 class="text-sm text-slate-500">
          Varias listas
        </h1>
        <BaseSwitch v-model="variasListas" />
      </div>
    </footer>

    <!-- Modal de confirmación -->
    <ConfirmPriceChangeModal v-model:open="confirmOpen" :listName="listaSeleccionada" :prevPrice="precioAnterior"
      :nextPrice="precioNuevo" @confirm="onConfirm" />
  </section>
</template>
