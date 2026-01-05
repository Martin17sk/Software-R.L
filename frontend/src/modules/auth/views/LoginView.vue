<script setup>
import { ref } from "vue";
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';

import BaseButton from '@/components/common/BaseButton.vue';
import BaseInputText from '@/components/common/BaseInputText.vue';

const username = ref("")
const password = ref("")
const errorMessage = ref("");

const authStore = useAuthStore()
const router = useRouter()

if (authStore.isAuthenticated) {
  router.replace({ name: "register-price" })
}

async function onSubmit() {
  errorMessage.value = "";
  try {
    await authStore.login(username.value, password.value)
    router.replace({ name: "register-price" })
  } catch (e) {
    errorMessage.value = "Credenciales inválidas"
    console.log(e)
  }
}
</script>

<template>
  <section class="bg-[#F4F4F4] h-screen w-screen flex flex-col justify-center items-center p-4">
    <div>
      <h1>S.R.L</h1>
      <div class="bg-white px-[60px] py-[60px] rounded-[8px]">
        <form class=" gap-[25px] flex flex-col justify-center align-center" @submit.prevent="onSubmit">
          <h1>Iniciar sesión</h1>
          <h4>Por favor, ingresa tus credenciales para continuar</h4>

          <BaseInputText v-model="username" label="Nombre de usuario" placeholder="Ingresa tu nombre de usuario" />

          <BaseInputText v-model="password" label="Contraseña" placeholder="Ingresa tu contraseña" type="password" />

          <BaseButton label="Iniciar sesión" type="submit" class="bg-[#4880FF] hover:bg-[#2563EB] active:bg-[#2F5BD1]"
            :disabled="authStore.loading" />

          <p v-if="errorMessage" class="text-sm text-red-600">{{ errorMessage }}</p>
        </form>
      </div>
    </div>
  </section>
</template>
