<script setup>
import { onMounted } from 'vue';
import { useAuthStore } from './stores/auth.store';

const auth = useAuthStore();

onMounted(() => {
  auth.init();
});
</script>

<template>
  <div id="app">
    <router-view />
  </div>
</template>


<style scoped></style>
