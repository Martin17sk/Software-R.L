<script setup>
</script>

<template>
  <footer class="min-h-[120px] w-full flex flex-row px-[60px] items-center bg-[#F4F4F4] border-t border-black">
    <!-- Izquierda -->
    <div class="flex-1 flex items-center">
      <slot name="left" />
    </div>

    <!-- Centro -->
    <div class="flex justify-center items-center">
      <slot name="center" />
    </div>

    <!-- Derecha -->
    <div class="flex-1 flex justify-end items-center">
      <slot name="right" />
    </div>
  </footer>
</template>
