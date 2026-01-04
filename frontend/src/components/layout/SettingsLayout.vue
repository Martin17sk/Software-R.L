<script setup>
import IconUser from '@/icons/user.svg'
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';
import IconLogout from '@/icons/logout.svg'
import BaseIconButton from '../common/BaseIconButton.vue';

const auth = useAuthStore()
const router = useRouter()

const props = defineProps({
  title: { type: String, default: 'Configuración' },
  sections: { type: Array, default: () => [] }, // [{ id, label }]
  activeId: { type: String, required: true },
})

const emit = defineEmits(['update:activeId'])

function select(id) {
  emit('update:activeId', id)
}

async function doLogout() {
    // cierre inmediato del menú para UX
    close();
    await auth.logout();
    router.replace({ name: "login" });
}
</script>

<template>
  <div class="h-screen w-screen bg-white flex">
    <!-- Sidebar -->
    <aside class="w-[260px] border-r border-slate-200 flex flex-col h-screen">
      <div class="px-6 py-5 shrink-0">
        <h1 class="text-lg font-semibold text-slate-900">{{ title }}</h1>
      </div>

      <nav class="px-2 flex-1 overflow-auto">
        <button v-for="s in sections" :key="s.id" type="button"
          class="w-full text-left px-4 py-3 rounded-md text-sm font-medium transition cursor-pointer" :class="activeId === s.id
            ? 'bg-slate-100 text-slate-900 font-semibold'
            : 'text-slate-700 hover:bg-slate-50'" @click="select(s.id)">
          {{ s.label }}
        </button>
      </nav>

      <div class="h-[90px] border-t border-slate-200 px-4 py-4 shrink-0 flex flex-row items-center">
        <div class="flex flex-row flex-1 gap-1">
          <IconUser class="h-6 w-6" />
          <span>{{ auth.user?.username || auth.user?.nombre }}</span>
        </div>

        <BaseIconButton title="Cerrar sesión" @click="doLogout">
          <IconLogout class="h-6 w-6"/>
        </BaseIconButton>
      </div>

    </aside>

    <!-- Content -->
    <section class="flex-1 flex flex-col">
      <!-- Scrollable content -->
      <div class="flex-1 overflow-auto px-10 py-8">
        <slot />
      </div>

      <!-- Footer fijo -->
      <div class="border-t border-slate-200 px-10 py-6 flex items-center justify-between">
        <slot name="footer-left" />
        <slot name="footer-right" />
      </div>
    </section>
  </div>
</template>
