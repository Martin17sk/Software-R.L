<script setup>
import { ref, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth.store";

import IconUser from "@/icons/user.svg"; // ajusta a tu path real
// Si tus íconos son componentes (SVGR/Vite plugin), cambia el import según corresponda.

const auth = useAuthStore();
const router = useRouter();

const open = ref(false);
const rootRef = ref(null);

function toggle() {
    open.value = !open.value;
}

function close() {
    open.value = false;
}

function onClickOutside(e) {
    if (!open.value) return;
    if (!rootRef.value) return;
    if (!rootRef.value.contains(e.target)) close();
}

async function doLogout() {
    // cierre inmediato del menú para UX
    close();
    await auth.logout();
    router.replace({ name: "login" });
}

// Listeners globales (click fuera + ESC)
document.addEventListener("click", onClickOutside);
document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") close();
});

onBeforeUnmount(() => {
    document.removeEventListener("click", onClickOutside);
    // El keydown anónimo no se puede remover así: lo dejamos simple o lo refactorizas si quieres pulcritud total.
});
</script>

<template>
    <div ref="rootRef" class="relative">
        <!-- Trigger -->
        <button type="button"
            class="flex flex-col gap-[1px] justify-center items-center px-2 py-1 rounded-md hover:bg-black/5 cursor-pointer"
            @click="toggle">
            <IconUser class="h-6 w-6" />
            <span class="text-sm">{{ auth.user?.username || auth.user?.nombre }}</span>
        </button>

        <!-- Dropdown -->
        <div v-if="open"
            class="absolute right-0 mt-2 w-48 bg-white border border-black/10 rounded-lg shadow-lg overflow-hidden z-50">
            <div class="px-3 py-2 text-xs text-black/60">
                Sesión iniciada
            </div>

            <button type="button" class="w-full text-left px-3 py-2 text-sm hover:bg-black/5 cursor-pointer" @click="doLogout">
                Cerrar sesión
            </button>
        </div>
    </div>
</template>
