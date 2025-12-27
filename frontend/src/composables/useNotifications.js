import { ref } from "vue";

const toasts = ref([])

function pushToast({ type = 'info', message = '', timeout = 3000 } = {}) {
    const id = crypto.randomUUID()
    toasts.value.push({ id, type, message })

    window.setTimeout(() => {
        toasts.value = toasts.value.filter(t => t.id !== id)
    }, timeout)

    return id
}

export function useNotifications() {
    function notifySuccess(message, timeout = 2500) {
        return pushToast({ type: 'success', message, timeout })
    }

    function notifyError(message, timeout = 4000) {
        return pushToast({ type: 'error', message, timeout })
    }

    function notifyInfo(message, timeout = 3000) {
        return pushToast({ type: 'info', message, timeout })
    }

    return { toasts, notifySuccess, notifyError, notifyInfo }
}