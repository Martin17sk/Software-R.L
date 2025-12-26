<script setup>
import BaseIconButton from "@/components/common/BaseIconButton.vue";
import IconHistory from "@/icons/clock.svg";
import IconSettings from "@/icons/settings.svg";
import { useRouter } from "vue-router";
import UserMenuDropdown from "./UserMenuDropdown.vue";

const router = useRouter();

function goToHistory() {
    router.push({ name: "history" });
}

function goToSettings() {
    router.push({ name: "configuration" });
}
</script>

<template>
    <header class="h-[120px] w-full flex flex-row px-[60px] items-center bg-[#F4F4F4]">

        <!-- Izquierda -->
        <div class="flex-1 flex items-center">
            <slot name="left" />
        </div>

        <!-- Centro -->
        <div class="flex justify-center">
            <slot name="center" />
        </div>

        <!-- Derecha (SIEMPRE) -->
        <div class="flex-1 flex justify-end items-center gap-2">
            <BaseIconButton title="Historial" @click="goToHistory">
                <IconHistory class="h-8 w-8" />
            </BaseIconButton>

            <BaseIconButton title="Configuración" @click="goToSettings">
                <IconSettings class="h-8 w-8" />
            </BaseIconButton>

            <!-- Divider -->
            <div class="w-px h-8 bg-black/30"></div>

            <UserMenuDropdown />
        </div>

    </header>
</template>
