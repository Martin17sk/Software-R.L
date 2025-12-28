<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import SettingsLayout from '@/components/layout/SettingsLayout.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import IconChevronDown from '@/icons/chevron-down.svg?component'
import IconPlus from '@/icons/plus.svg?component'
import IconPencilSquare from '@/icons/pencil-square.svg?component'
import IconArrowLeft from '@/icons/arrow-left.svg'
import AddPriceListModal from '@/components/modals/AddPriceListModal.vue'
import AddArticleModal from '@/components/modals/AddArticleModal.vue'
import api from '@/services/axios'
import { useRouter } from 'vue-router'
import { useParameters } from '../composables/useParameters'
import BaseDropdown from '@/components/common/BaseDropdown.vue'
import EditArticleModal from '@/components/modals/EditArticleModal.vue'
import AddUserModal from '@/components/modals/AddUserModal.vue'

const router = useRouter()

const addListOpen = ref(false)
const addListSubmitting = ref(false)
const addListError = ref('')

const addArticleOpen = ref(false)
const addArticleSubmitting = ref(false)
const addArticleError = ref('')

const editArticleOpen = ref(false)
const editArticleSubmitting = ref(false)
const editArticleError = ref('')

const addUserOpen = ref(false)
const addUserSubmitting = ref(false)
const addUserError = ref('')

const { systems, listasPrecio, articulos, loading, error, loadAll } = useParameters()

const listasOpen = ref(false)
const articulosOpen = ref(false)

const listasOptions = computed(() =>
  (listasPrecio.value ?? []).map(lp => ({ value: lp.id, label: lp.nombre }))
)

const articulosOptions = computed(() =>
  (articulos.value ?? []).map(a => ({ value: a.codigo, label: `${a.codigo} — ${a.nombre}` }))
)
const selectedArticuloCodigo = ref(null)


const sections = [
  { id: 'general', label: 'General' },
  { id: 'listas', label: 'Listas de precios' },
  { id: 'articulos', label: 'Artículos' },
  { id: 'usuarios', label: 'Usuarios' }
]

const activeId = ref('general')

// ejemplo de estado
const deleteWindowValue = ref('24')

function save() {
  // llamar API
}

function goToRegister() {
  router.push({ name: 'register-price' })
}

function openAddList() {
  addListOpen.value = true
}

function openAddArticle() {
  addArticleOpen.value = true
}

function openEditArticle() {
  editArticleOpen.value = true
}

function openAddUser() {
  addUserOpen.value = true
}

async function onAddList(payload) {
  // payload debe venir como: { nombre, sistemaId }
  addListError.value = ''
  addListSubmitting.value = true

  try {
    await api.post('/listas-precio', payload)
    addListOpen.value = false
    await loadAll()
  } catch (e) {
    addListError.value = e?.response?.data?.message || (typeof e?.response?.data === 'string' ? e.response.data : '') || 'No se pudo crear la lista'
    console.error(e)
  } finally {
    addListSubmitting.value = false
  }
}

async function onAddArticle(payload) {
  // payload debe venir como: { codigo, nombre, um }
  addArticleError.value = ''
  addArticleSubmitting.value = true

  try {
    await api.post('/articulos', payload)
    addArticleOpen.value = false
    await loadAll()
  } catch (e) {
    addArticleError.value = e?.response?.data?.message || (typeof e?.response?.data === 'string' ? e.response.data : '') || 'No se pudo crear el artículo'
    console.error(e)
  } finally {
    addArticleSubmitting.value = false
  }
}

async function onEditArticle(payload) {
  // payload debe venir como: { codigo, nombre, um }
  editArticleError.value = ''
  editArticleSubmitting.value = true

  try {
    await api.put(`/articulos/${payload.codigo}`, {
      nombre: payload.nombre,
      unidadMedida: payload.unidadMedida
    })

    editArticleOpen.value = false
    await loadAll()
  } catch (e) {
    editArticleError.value =
      e?.response?.data?.message ||
      (typeof e?.response?.data === 'string' ? e.response.data : '') ||
      'No se pudo actualizar el artículo'
    console.error(e)
  } finally {
    editArticleSubmitting.value = false
  }
}

async function onAddUser(payload) {
  addUserError.value = ''
  addUserSubmitting.value = true
  try {
    await api.post('/usuarios', payload)
    addUserOpen.value = false
    await loadAll() // o carga usuarios si los manejas aparte
  } catch (e) {
    if (e?.response?.status === 409) {
      addUserError.value = 'Ese nombre de usuario ya existe.'
    } else {
      addUserError.value =
        e?.response?.data?.message ||
        (typeof e?.response?.data === 'string' ? e.response.data : '') ||
        'No se pudo crear el usuario'
    }
  } finally {
    addUserSubmitting.value = false
  }
}

function toggleListas() { listasOpen.value = !listasOpen.value }

function toggleArticulos() { articulosOpen.value = !articulosOpen.value }

watch(activeId, () => {
  listasOpen.value = false
  articulosOpen.value = false
})

onMounted(async () => {
  await loadAll()
})

</script>

<template>
  <SettingsLayout v-model:activeId="activeId" :sections="sections" title="Configuración">
    <!-- Sección: General -->
    <p v-if="error" class="text-sm text-red-600">{{ error }}</p>
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
    <p v-if="error" class="text-sm text-red-600">{{ error }}</p>
    <div v-else-if="activeId === 'listas'" class="flex flex-col max-w-[700px] gap-[20px]">
      <div class="flex flex-col">
        <h2 class="text-base font-semibold text-slate-900">Listas de precios</h2>
        <p class="text-sm text-slate-500 mt-1">Crea listas de precios</p>
      </div>

      <div class="flex flex-col justify-center items-start gap-[10px]">
        <BaseButton label="Ver listas de precios" variant="ghost" class="gap-[10px] pt-0 pr-0 pb-0 pl-0"
          @click="toggleListas">
          <template #iconRight>
            <IconChevronDown class="h-4 w-4 text-black" :class="listasOpen ? 'rotate-180' : ''" />
          </template>
        </BaseButton>

        <div v-if="listasOpen" class="w-[300px]">
          <BaseDropdown label="Listas desde BD" :options="listasOptions" placeholder="Ver listas"
            :disabled="loading" searchable />
          <p v-if="!listasOptions.length && !loading" class="text-sm text-slate-500 mt-2"> No hay listas de precio
            registradas</p>
        </div>


        <BaseButton label="Agregar lista de precios" variant="secondary" class="gap-[10px]" @click="openAddList">
          <template #iconRight>
            <IconPlus class="h-5 w-5 text-white" />
          </template>
        </BaseButton>
      </div>
    </div>

    <!-- Sección: Artículos -->
    <p v-if="error" class="text-sm text-red-600">{{ error }}</p>
    <div v-else-if="activeId === 'articulos'" class="flex flex-col max-w-[700px] gap-[20px]">
      <div class="flex flex-col">
        <h2 class="text-base font-semibold text-slate-900">Artículos</h2>
        <p class="text-sm text-slate-500 mt-1">Crea artículos</p>
      </div>

      <div class="flex flex-col justify-center items-start gap-[10px]">
        <BaseButton label="Ver artículos" variant="ghost" class="gap-[10px] pt-0 pr-0 pb-0 pl-0"
          @click="toggleArticulos">
          <template #iconRight>
            <IconChevronDown class="h-4 w-4 text-black" :class="articulosOpen ? 'rotate-180' : ''" />
          </template>
        </BaseButton>

        <div v-if="articulosOpen" class="w-[500px]">
          <BaseDropdown v-model="selectedArticuloCodigo" label="Articulos desde BD" :options="articulosOptions" placeholder="Ver artículos"
            :disabled="loading" searchable />
          <p v-if="!articulosOptions.length && !loading" class="text-sm text-slate-500 mt-2"> No hay articulos
            registrados
          </p>
        </div>


        <div class="flex flex-row gap-[20px]">
          <BaseButton label="Agregar artículo" variant="secondary" class="gap-[10px]" @click="openAddArticle">
            <template #iconRight>
              <IconPlus class="h-5 w-5 text-white" />
            </template>
          </BaseButton>

          <BaseButton label="Editar artículo" variant="secondary" class="gap-[10px]" @click="openEditArticle">
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
        <BaseButton label="Agregar usuario" variant="secondary" class="gap-[10px]" @click="openAddUser">
          <template #iconRight>
            <IconPlus class="h-5 w-5 text-white" />
          </template>
        </BaseButton>
      </div>
    </div>

    <template #footer-left>
      <BaseButton label="Volver" variant="secondary" class="w-[160px] gap-[10px]" @click="goToRegister">
        <template #iconLeft>
          <IconArrowLeft class="h-6 w-6" />
        </template>
      </BaseButton>
    </template>

    <template #footer-right>
      <BaseButton label="Guardar cambios" variant="accept" class="w-[160px]" @click="save" />
    </template>

    <AddPriceListModal v-model:open="addListOpen" :server-error="addListError" :submitting="addListSubmitting"
      @confirm="onAddList" />

    <AddArticleModal v-model:open="addArticleOpen" :server-error="addArticleError" :submitting="addArticleSubmitting" @confirm="onAddArticle" />

    <EditArticleModal v-model:open="editArticleOpen" :server-error="editArticleError" :submitting="editArticleSubmitting" @confirm="onEditArticle"/>

    <AddUserModal v-model:open="addUserOpen" :server-error="addUserError" :submitting="addUserSubmitting" @confirm="onAddUser"/>
  </SettingsLayout>
</template>
