<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useAuthStore } from './stores/auth.store';
import { useRoute, useRouter } from 'vue-router';
import HeaderBar from './components/layout/HeaderBar.vue';
import BaseTabs from './components/common/BaseTabs.vue';

const route = useRoute()
const router = useRouter()
const auth = useAuthStore();

onMounted(() => {
  auth.init();
});

const layout = computed(() => route.meta.layout ?? "app")

const tab = ref('precio');
const tabs = [
  { value: 'precio', label: 'Cambio de precio de artículo' },
  { value: 'comparar', label: 'Comparar listas de precios' },
];

const tabToRouteName = {
  precio: 'register-price',
  comparar: 'compare-lists',
};

const showTabs = computed(() => ['register-price', 'compare-lists'].includes(String(route.name)));

function syncTabWithRoute() {
  if (route.name === 'register-price') {
    tab.value = 'precio'
    return
  }

  if (route.name === 'compare-lists') {
    tab.value = 'comparar';
  }
}

syncTabWithRoute();

watch(
  () => route.name,
  () => {
    syncTabWithRoute();
  },
);

watch(tab, (value) => {
  if (!showTabs.value) return

  const targetRoute = tabToRouteName[value];
  if (!targetRoute || targetRoute === route.name) return;
  
  router.push({ name: targetRoute });
});
</script>

<template>
  <div class="h-dvh bg-[#F4F4F4] flex flex-col">
    <HeaderBar v-if="layout === 'app' && auth.isAuthenticated">
      <template v-if="showTabs" #center>
        <BaseTabs v-model="tab" :tabs="tabs" />
      </template>
    </HeaderBar>

    <main class="flex-1 min-h-0 overflow-auto">
      <router-view />
    </main>
  </div>
</template>


<style scoped></style>
