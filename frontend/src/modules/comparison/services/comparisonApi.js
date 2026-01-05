import { useHttpClient } from "@/composables/useHttpClient";

export async function compararListas({ listaA, listaB, sistemaId}) {
    const {post} = useHttpClient()

    const form = new FormData()
    form.append('listaA', listaA)
    form.append('listaB', listaB)

    return await post(`/api/comparacion/listas?sistemaId=${encodeURIComponent(sistemaId)}`, form)
}