<script setup>
import { computed, onMounted } from 'vue';
import { useAuthStore } from './stores/auth.store';
import { useRoute, useRouter } from 'vue-router';
import { useNotifications } from './composables/useNotifications';
import HeaderBar from './components/layout/HeaderBar.vue';
import BaseTabs from './components/common/BaseTabs.vue';

const route = useRoute()
const router = useRouter()
const auth = useAuthStore();

onMounted(() => {
  auth.init();
});

const layout = computed(() => route.meta.layout ?? "app")

const tabs = [
  { value: 'register-price', label: 'Cambio de precio de artículo' },
  { value: 'compare-lists', label: 'Comparar listas de precios' },
]

const activeTab = computed({
  get() {
    return String(route.name ?? '')
  },
  set(name) {
    if (!showTabs.value) return
    if (name && name !== route.name) {
      router.push({ name })
    }
  }
})

const showTabs = computed(() =>
  ['register-price', 'compare-lists'].includes(String(route.name))
)

const { toasts } = useNotifications()

</script>

<template>
  <div class="h-dvh bg-[#F4F4F4] flex flex-col">

    <!-- Toasts gloabels -->
    <div class="fixed top-4 right-4 z-[9999] flex flex-col gap-2 w-[320px]">
      <div v-for="t in toasts" :key="t.id" class="rounded-xl px-4 py-3 shadow-md border text-sm" :class="{
        'bg-white border-slate-200 text-slate-900': t.type === 'info',
        'bg-white border-green-300 text-green-800': t.type === 'success',
        'bg-white border-red-300 text-red-800': t.type === 'error',
      }">
        {{ t.message }}
      </div>
    </div>

    <HeaderBar v-if="layout === 'app' && auth.isAuthenticated">
      <template v-if="showTabs" #center>
        <BaseTabs v-model="activeTab" :tabs="tabs" />
      </template>
    </HeaderBar>

    <main class="flex-1 min-h-0 overflow-hidden">
      <RouterView />
    </main>
  </div>
</template>


<style scoped></style>
