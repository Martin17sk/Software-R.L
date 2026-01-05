<script setup>
// ----- IMPORTS -----

// Utilidades
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

// Servicios
import api from '@/services/axios'

// Componentes
import BaseButton from '@/components/common/BaseButton.vue'
import BaseInputText from '@/components/common/BaseInputText.vue'
import BaseIconButton from '@/components/common/BaseIconButton.vue'

// Composables
import { useParameters } from '../composables/useParameters'

// Layouts
import SettingsLayout from '@/components/layout/SettingsLayout.vue'

// Modales
import EditArticleModal from '@/components/modals/EditArticleModal.vue'
import AddUserModal from '@/components/modals/AddUserModal.vue'
import AddPriceListModal from '@/components/modals/AddPriceListModal.vue'
import AddArticleModal from '@/components/modals/AddArticleModal.vue'

// Iconos
import IconChevronDown from '@/icons/chevron-down.svg?component'
import IconPlus from '@/icons/plus.svg?component'
import IconPencilSquare from '@/icons/pencil-square.svg?component'
import IconArrowLeft from '@/icons/arrow-left.svg'
import IconSearch from '@/icons/magnifying-glass.svg'
import IconChevronLeft from '@/icons/chevron-left.svg'
import IconChevronRight from '@/icons/chevron-right.svg'


// ----- CONSTS -----

// Utilidades
const router = useRouter()
const { listasPrecio, articulos, error, loadAll } = useParameters()

// Refs
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

const listasOpen = ref(false)
const articulosOpen = ref(false)

// --- buscador ---
const searchArticulo = ref('')
const searchLista = ref('')

// --- paginacion ---
const currentPage = ref(1)
const pageSize = 20

const filteredArticulos = computed(() => {
  const q = (searchArticulo.value ?? '').trim().toLowerCase()
  if (!q) return articulosOptions.value

  // Filtra por código o nombre (como tu label trae "codigo — nombre", sirve igual)
  return articulosOptions.value.filter(o =>
    String(o.label).toLowerCase().includes(q) || String(o.value).toLowerCase().includes(q)
  )
})

const filteredListas = computed(() => {
  const q = (searchLista.value ?? '').trim().toLowerCase()
  if (!q) return listasOptions.value

  // Filtra por código o nombre (como tu label trae "codigo — nombre", sirve igual)
  return listasOptions.value.filter(o =>
    String(o.label).toLowerCase().includes(q) || String(o.value).toLowerCase().includes(q)
  )
})

const paginatedArticulos = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredArticulos.value.slice(start, start + pageSize)
})

const paginatedListas = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredListas.value.slice(start, start + pageSize)
})

const totalPagesArticles = computed(() =>
  Math.max(1, Math.ceil(filteredArticulos.value.length / pageSize))
)

const totalPagesLists = computed(() =>
  Math.max(1, Math.ceil(filteredListas.value.length / pageSize))
)

// Computeds
const listasOptions = computed(() =>
  (listasPrecio.value ?? []).map(lp => ({ value: lp.id, label: lp.nombre }))
)

const articulosOptions = computed(() =>
  (articulos.value ?? []).map(a => ({ value: a.codigo, label: `${a.codigo} — ${a.nombre}` }))
)

const sections = [
  { id: 'listas', label: 'Listas de precios' },
  { id: 'articulos', label: 'Artículos' },
  { id: 'usuarios', label: 'Usuarios' }
]

const activeId = ref('listas')

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

watch(searchArticulo, () => {
  currentPage.value = 1
})

// Si cambia el total de páginas (por filtro o carga), asegura página válida
watch(totalPagesArticles, () => {
  if (currentPage.value > totalPagesArticles.value) currentPage.value = totalPagesArticles.value
})

watch(totalPagesLists, () => {
  if (currentPage.value > totalPagesLists.value) currentPage.value = totalPagesLists.value
})

onMounted(async () => {
  await loadAll()
})

</script>

<template>
  <SettingsLayout v-model:activeId="activeId" :sections="sections" title="Configuración">

    <!-- Sección: Listas de precios -->
    <p v-if="error" class="text-sm text-red-600">{{ error }}</p>
    <div v-else-if="activeId === 'listas'" class="flex flex-col max-w-[700px] gap-[20px]">
      <div class="flex flex-col">
        <h2 class="text-base font-semibold text-slate-900">Listas de precios</h2>
        <p class="text-sm text-slate-500 mt-1">Crea listas de precios</p>
      </div>

      <div class="flex flex-col justify-center items-start gap-[10px]">
        <!-- Toggle listas -->
        <BaseButton label="Ver listas de precios" variant="ghost" class="gap-[10px] pt-0 pr-0 pb-0 pl-0"
          @click="toggleListas">
          <template #iconRight>
            <IconChevronDown class="h-4 w-4 text-black" :class="listasOpen ? 'rotate-180' : ''" />
          </template>
        </BaseButton>


        <div v-if="listasOpen" class="space-y-3">
          <!-- Buscador -->
          <BaseInputText v-model="searchLista" placeholder="Buscar lista">
            <template #iconRight>
              <IconSearch class="h-4 w-4" />
            </template>
          </BaseInputText>

          <!-- Lista de resultados -->
          <ul class="list-disc pl-5 text-sm text-slate-600 space-y-1">
            <li v-for="item in paginatedListas" :key="item.value">
              {{ item.label }}
            </li>
          </ul>

          <!-- Botones paginación -->
          <div class="flex gap-2 items-center">
            <BaseIconButton title="Anterior" :disabled="currentPage <= 1"
              @click="currentPage = Math.max(1, currentPage - 1)">
              <IconChevronLeft class="h-4 w-4" />
            </BaseIconButton>

            <span class="text-sm">
              Página {{ currentPage }} de {{ totalPagesLists }} · {{ filteredListas.length }} resultados
            </span>

            <BaseIconButton title="Siguiente" :disabled="currentPage >= totalPagesLists"
              @click="currentPage = Math.min(totalPagesLists, currentPage + 1)">
              <IconChevronRight class="h-4 w-4" />
            </BaseIconButton>
          </div>
        </div>

        <!-- Agregar lista -->
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

      <!-- Toggle articulos -->
      <div class="flex flex-col justify-center items-start gap-[10px]">
        <BaseButton label="Ver artículos" variant="ghost" class="gap-[10px] pt-0 pr-0 pb-0 pl-0"
          @click="toggleArticulos">
          <template #iconRight>
            <IconChevronDown class="h-4 w-4 text-black" :class="articulosOpen ? 'rotate-180' : ''" />
          </template>
        </BaseButton>

        <div v-if="articulosOpen" class="space-y-3">
          <!-- Buscador artículos-->
          <BaseInputText v-model="searchArticulo" placeholder="Buscar artículo (código o nombre)">
            <template #iconRight>
              <IconSearch class="h-4 w-4" />
            </template>
          </BaseInputText>

          <!-- Lista de resultados -->
          <ul class="list-disc pl-5 text-sm text-slate-600 space-y-1">
            <li v-for="item in paginatedArticulos" :key="item.value">
              {{ item.label }}
            </li>
          </ul>

          <!-- Botones de paginación -->
          <div class="flex gap-2 items-center">
            <BaseIconButton title="Anterior" :disabled="currentPage <= 1"
              @click="currentPage = Math.max(1, currentPage - 1)">
              <IconChevronLeft class="h-4 w-4" />
            </BaseIconButton>

            <span class="text-sm">
              Página {{ currentPage }} de {{ totalPagesArticles }} · {{ filteredArticulos.length }} resultados
            </span>

            <BaseIconButton title="Siguiente" :disabled="currentPage >= totalPagesArticles"
              @click="currentPage = Math.min(totalPagesArticles, currentPage + 1)">
              <IconChevronRight class="h-4 w-4" />
            </BaseIconButton>
          </div>
        </div>

        <!-- Botones agregar/editar artículos -->
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

    <AddPriceListModal v-model:open="addListOpen" :server-error="addListError" :submitting="addListSubmitting"
      @confirm="onAddList" />

    <AddArticleModal v-model:open="addArticleOpen" :server-error="addArticleError" :submitting="addArticleSubmitting"
      @confirm="onAddArticle" />

    <EditArticleModal v-model:open="editArticleOpen" :server-error="editArticleError"
      :submitting="editArticleSubmitting" @confirm="onEditArticle" />

    <AddUserModal v-model:open="addUserOpen" :server-error="addUserError" :submitting="addUserSubmitting"
      @confirm="onAddUser" />
  </SettingsLayout>
</template>
