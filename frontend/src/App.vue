<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useAuthStore } from './stores/auth.store';
import { useRoute, useRouter } from 'vue-router';
import HeaderBar from './components/layout/HeaderBar.vue';

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
  if (route.name === 'compare-lists') {
    tab.value = 'comparar';
  } else {
    tab.value = 'precio';
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
  const targetRoute = tabToRouteName[value];
  if (!targetRoute || targetRoute === route.name) return;
  router.push({ name: targetRoute });
});
</script>

<template>
  <div>
    <HeaderBar v-if="layout === 'app' && auth.isAuthenticated">
      <template v-if="showTabs" #center>
        <BaseTabs v-model="tab" :tabs="tabs" />
      </template>
    </HeaderBar>

    <router-view />
  </div>
</template>


<style scoped></style>
