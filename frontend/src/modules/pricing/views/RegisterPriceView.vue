<script setup>
  import BaseButton from '@/components/common/BaseButton.vue';
  import BaseInputText from '@/components/common/BaseInputText.vue';
  import BaseDropdown from '@/components/common/BaseDropdown.vue';
  import BaseTextarea from '@/components/common/BaseTextarea.vue';
  import BaseTabs from '@/components/common/BaseTabs.vue';
  import BaseSwitch from '@/components/common/BaseSwitch.vue';
import { ref } from 'vue';

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

  const variasListas = ref(false);
</script>

<template>
  <section class="bg-[#F4F4F4] h-screen w-screen flex flex-col justify-center items-center p-4 place-content-between">
    <header class="h-[180px] w-full flex flex-row py-[50px] justify-center">
      <div class="w-auto">
        <BaseTabs v-model="tab" :tabs="tabs"/>
      </div>
    </header>

    <body class="h-full">
      <div class="gap-[20px]">
        <h1>Cambio de precio de artículo</h1>
        <form action="" class="gap-[20px] flex flex-col">
          <div class="w-full place-content-between flex flex-row">
            <BaseInputText label="Códgo artículo" class="w-[125px]"/>
            <BaseInputText label="Nombre artículo" class="w-[415px]" iconRight="frontend/src/icons/Lock.png" disabled="true"/>
          </div>
          <div class="w-full flex flex-row place-content-between" >
            <BaseDropdown label="Lista de precios" placeholder="Seleccionar" v-model="lista" :options="opciones" class="w-[260px]"/>
            <BaseInputText label="Precio anterior" disabled="true" iconRight="frontend\src\icons\Lock.png" class="w-[125px]"/>
            <BaseInputText label="Precio nuevo" class="w-[125px]"/>
          </div>
          <BaseTextarea label="Nota (opcional)" placeholder="Escribe tu nota aquí" rows="4" class="w-[570px]"/>
        </form>
      </div>
    </body>

    <footer class="flex flex-col border-t-black gap-[30px]">
      <div class="flex flex-row gap-[30px]">
        <BaseButton label="Borrar todo" variant="outline" class="w-[250px]"/>
        <BaseButton label="Continuar" variant="accept" class="w-[250px]"/>
      </div>
      
      <div class="flex flex-row justify-center items-center gap-[10px]">
        <h1 class="text-sm text-slate-500">
          Varias listas
        </h1>
        <BaseSwitch v-model="variasListas"/>
      </div>
    </footer>
  </section>
</template>
