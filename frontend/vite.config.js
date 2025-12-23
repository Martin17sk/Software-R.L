import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    svgLoader()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@auth': fileURLToPath(new URL('./src/modules/auth', import.meta.url)),
      '@pricing': fileURLToPath(new URL('./src/modules/pricing', import.meta.url)),
      '@comparison': fileURLToPath(new URL('./src/modules/comparison', import.meta.url)),
      '@config': fileURLToPath(new URL('./src/modules/config', import.meta.url)),
      '@components': fileURLToPath(new URL('./src/components', import.meta.url)),
      '@composables': fileURLToPath(new URL('./src/composables', import.meta.url)),
      '@services': fileURLToPath(new URL('./src/modules', import.meta.url)),
    },
  },
  css: {
    transformer: 'postcss',
  }
})
