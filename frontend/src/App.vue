<script setup>
import { computed, onMounted } from 'vue';
import { useAuthStore } from './stores/auth.store';
import { useRoute } from 'vue-router';
import HeaderBar from './components/layout/HeaderBar.vue';

const route = useRoute()
const auth = useAuthStore();

onMounted(() => {
  auth.init();
});

const layout = computed(() => route.meta.layout ?? "app")
</script>

<template>
  <div>
    <!-- Header solo para layout APP --> 
    <HeaderBar v-if="layout === 'app' && auth.isAuthenticated"/>

    <router-view />
  </div>
</template>


<style scoped></style>
