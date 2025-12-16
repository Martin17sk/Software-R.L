<script setup>
import { ref } from 'vue'
import BaseDropzone from '@/components/common/BaseDropzone.vue'
import BaseButton from '@/components/common/BaseButton.vue'
import BaseTabs from '@/components/common/BaseTabs.vue'
import BaseSwitch from '@/components/common/BaseSwitch.vue'

import compareIcon from '@/icons/Compare.png'

const excelFile = ref(null)

const tab = ref('comparar');
const tabs = [
  { value: 'precio', label: 'Cambio de precio de artículo' },
  { value: 'comparar', label: 'Comparar listas de precios' },
]

function onDone() {
  if (!excelFile.value) return
  // aquí llamas al backend (FormData)
}
</script>

<template>
  <section class="bg-[#F4F4F4] h-screen w-screen flex flex-col justify-center items-center place-content-between">
    <header class="h-[180px] w-full flex flex-row py-[50px] justify-center">
      <div class="w-auto">
        <BaseTabs v-model="tab" :tabs="tabs" />
      </div>
    </header>

    <body class="h-full w-full flex flex-col justify-start items-center pt-[80px]">
      <div>
        <div class="flex flex-col gap-[20px]">
          <h1>Comparar listas de precios</h1>
          <div class="flex flex-row gap-[50px] items-center">
            <div class="flex flex-col w-[640px] bg-white p-[24px] rounded-[12px] gap-[32px] shadow-md">
              <BaseDropzone title="Subir archivo" subtitle="Por favor, suba el archivo en formato .xls o .xlsx"
                accept=".xls,.xlsx" v-model="excelFile" />

              <div class="flex flex-row items-center justify-between gap-[12px]">
                <BaseButton variant="outline" label="Cancelar" class="w-full"/>
                <BaseButton variant="accept" label="Aceptar" :disabled="!excelFile" class="w-full"/>
              </div>
            </div>
            <img :src="compareIcon" alt="" class="w-[80px] h-[100px]" />
            <div class="flex flex-col w-[640px] bg-white p-[24px] rounded-[12px] gap-[32px] shadow-md">
              <BaseDropzone title="Subir archivo" subtitle="Por favor, suba el archivo en formato .xls o .xlsx"
                accept=".xls,.xlsx" v-model="excelFile" />

              <div class="flex flex-row items-center justify-between gap-[12px]">
                <BaseButton variant="outline" label="Cancelar" class="w-full"/>
                <BaseButton variant="accept" label="Aceptar" :disabled="!excelFile" class="w-full"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </body>

    <footer class="flex flex-col border-t border-t-black gap-[30px] w-full h-[180px] justify-center items-center">
      <div class="flex flex-row gap-[30px]">
        <BaseButton label="Comparar" variant="accept" class="w-[250px]" />
      </div>
    </footer>
  </section>
</template>
