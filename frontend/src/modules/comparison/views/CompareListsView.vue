<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BaseDropzone from '@/components/common/BaseDropzone.vue'
import BaseButton from '@/components/common/BaseButton.vue'

import compareIcon from '@/icons/Compare.png'
import FooterBar from '@/components/layout/FooterBar.vue'

const excelFileLeft = ref(null)
const excelFileRight = ref(null)

const router = useRouter()
const route = useRoute()

function goToResults() {
  router.push({name: 'compare-result'})
}
</script>

<template>
  <section class="bg-[#F4F4F4] h-full w-full flex flex-col">
    <main class="h-full w-full flex flex-col justify-start items-center pt-[80px]">
      <div>
        <div class="flex flex-col gap-[20px]">
          <h1>Comparar listas de precios</h1>
          <div class="flex flex-row gap-[50px] items-center">
            <div class="flex flex-col w-[640px] bg-white p-[24px] rounded-[12px] gap-[32px] shadow-md">
              <BaseDropzone title="Subir archivo" subtitle="Por favor, suba el archivo en formato .xls o .xlsx"
                accept=".xls,.xlsx" v-model="excelFileLeft" />

              <div class="flex flex-row items-center justify-between gap-[12px]">
                <BaseButton variant="outline" label="Cancelar" class="w-full"/>
                <BaseButton variant="accept" label="Aceptar" :disabled="!excelFileLeft" class="w-full"/>
              </div>
            </div>
            <img :src="compareIcon" alt="" class="w-[80px] h-[100px]" />
            <div class="flex flex-col w-[640px] bg-white p-[24px] rounded-[12px] gap-[32px] shadow-md">
              <BaseDropzone title="Subir archivo" subtitle="Por favor, suba el archivo en formato .xls o .xlsx"
                accept=".xls,.xlsx" v-model="excelFileRight" />

              <div class="flex flex-row items-center justify-between gap-[12px]">
                <BaseButton variant="outline" label="Cancelar" class="w-full"/>
                <BaseButton variant="accept" label="Aceptar" :disabled="!excelFileRight" class="w-full"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <FooterBar>
      <template #center>
        <BaseButton label="Comparar" variant="accept" class="w-[200px]" :disabled="!excelFileLeft || !excelFileRight" @click="goToResults"/>
      </template>

      <!-- solo para debug -->
      <template #right>
        <BaseButton label="Continuar (debug)" variant="secondary" class="w-[200px]" @click="goToResults"/>
      </template>
    </FooterBar>
  </section>
</template>
