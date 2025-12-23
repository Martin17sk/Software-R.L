import { defineStore } from "pinia";
import { fetchPriceLists } from "@/modules/pricing/services/pricingApi";

export const usePricingStore = defineStore("pricing", {
    state: () => ({
        priceLists: [],
        loading: false,
        loaded: false,
    }),
    getters: {
        options: (s) => s.priceLists.map(x => ({value: x.id, label: x.nombre}))
    },
    actions: {
        async loadPriceLists(force = false) {
            if (this.loaded && !force) return
            this.loading = true
            try {
                this.priceLists = await fetchPriceLists()
                this.loaded = true
            } finally {
                this.loading = false
            }
        }
    }
})