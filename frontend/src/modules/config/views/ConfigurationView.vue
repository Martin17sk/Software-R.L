<script setup>
import { ref } from 'vue'
import SettingsLayout from '@/components/layout/SettingsLayout.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import IconChevronDown from '@/icons/chevron-down.svg?component'
import IconPlus from '@/icons/plus.svg?component'
import IconPencilSquare from '@/icons/pencil-square.svg?component'
import AddPriceListModal from '@/components/modals/AddPriceListModal.vue'

const sections = [
  { id: 'general', label: 'General' },
  { id: 'listas', label: 'Listas de precios' },
  { id: 'articulos', label: 'Artículos' },
  { id: 'usuarios', label: 'Usuarios' }
]

const activeId = ref('general')

// ejemplo de estado
const deleteWindowValue = ref('24')

function resetAll() {
  deleteWindowValue.value = '24'
}

function save() {
  // llamar API
}

const addListOpen = ref(false)

function openAddList() {
  addListOpen.value = true
}

function onAddList(name) {
  // llamar API para crear lista
  console.log('crear lista:', name)
}

</script>

<template>
  <SettingsLayout v-model:activeId="activeId" :sections="sections" title="Configuración">
    <!-- Sección: General -->
    <div v-if="activeId === 'general'" class="max-w-[700px]">
      <h2 class="text-base font-semibold text-slate-900">General</h2>
      <p class="text-sm text-slate-500 mt-1">Ajusta parámetros globales</p>

      <div class="mt-6 flex items-end gap-4">
        <div class="text-sm text-slate-700 w-[260px]">
          Tiempo límite para eliminación de registro (en horas)
        </div>

        <BaseInputText class="w-[80px]" v-model="deleteWindowValue" />
      </div>
    </div>

    <!-- Sección: Listas -->
    <div v-else-if="activeId === 'listas'" class="flex flex-col max-w-[700px] gap-[20px]">
      <div class="flex flex-col">
        <h2 class="text-base font-semibold text-slate-900">Listas de precios</h2>
        <p class="text-sm text-slate-500 mt-1">Crea listas de precios</p>
      </div>

      <div class="flex flex-col justify-center items-start gap-[10px]">
        <BaseButton label="Ver listas de precios" variant="ghost" class="gap-[10px] pt-0 pr-0 pb-0 pl-0">
          <template #iconRight>
            <IconChevronDown class="h-4 w-4 text-black" />
          </template>
        </BaseButton>

        <BaseButton label="Agregar lista de precios" variant="secondary" @click="openAddList">
          <template #iconRight>
            <IconPlus class="h-5 w-5 text-white" />
          </template>
        </BaseButton>
      </div>
    </div>

    <!-- Sección: Artículos -->
    <div v-else-if="activeId === 'articulos'" class="flex flex-col max-w-[700px] gap-[20px]">
      <div class="flex flex-col">
        <h2 class="text-base font-semibold text-slate-900">Artículos</h2>
        <p class="text-sm text-slate-500 mt-1">Crea artículos</p>
      </div>

      <div class="flex flex-col justify-center items-start gap-[10px]">
        <BaseButton label="Ver artículos" variant="ghost" class="gap-[10px] pt-0 pr-0 pb-0 pl-0">
          <template #iconRight>
            <IconChevronDown class="h-4 w-4 text-black" />
          </template>
        </BaseButton>

        <div class="flex flex-row gap-[20px]">
          <BaseButton label="Agregar artículo" variant="secondary" class="gap-[10px]">
            <template #iconRight>
              <IconPlus class="h-5 w-5 text-white" />
            </template>
          </BaseButton>

          <BaseButton label="Editar artículo" variant="secondary" class="gap-[10px]">
            <template #iconRight>
              <IconPencilSquare class="h-5 w-5 text-white" />
            </template>
          </BaseButton>
        </div>
      </div>
    </div>

    <!-- Sección: Usuarios -->
    <div v-else-if="activeId === 'usuarios'" class="flex flex-col max-w-[700px] gap-[20px]">
      <div class="flex flex-col">
        <h2 class="text-base font-semibold text-slate-900">Usuarios</h2>
        <p class="text-sm text-slate-500 mt-1">Crea usuarios</p>
      </div>

      <div class="flex flex-col justify-center items-start gap-[10px]">
        <BaseButton label="Agregar usuario" variant="secondary" class="gap-[10px]">
          <template #iconRight>
            <IconPlus class="h-5 w-5 text-white" />
          </template>
        </BaseButton>
      </div>
    </div>

    <template #footer-left>
      <button type="button" class="text-sm text-slate-600 underline cursor-pointer" @click="resetAll">
        Restablecer todo
      </button>
    </template>

    <template #footer-right>
      <BaseButton label="Guardar cambios" variant="accept" class="w-[160px]" @click="save" />
    </template>

    <AddPriceListModal v-model:open="addListOpen" @confirm="onAddList" />
  </SettingsLayout>
</template>
