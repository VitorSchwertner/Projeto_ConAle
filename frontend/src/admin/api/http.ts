const configuredBaseUrl = import.meta.env.VITE_API_URL?.trim()
const BASE_URL = configuredBaseUrl ? configuredBaseUrl.replace(/\/$/, '') : ''

let csrf: { headerName: string; token: string } | null = null

export function resetCsrf() {
    csrf = null
}

async function fetchCsrf() {
    const res = await fetch(`${BASE_URL}/api/auth/csrf`, { credentials: 'include' })
    if (!res.ok) throw new Error('Falha ao obter token CSRF')
    csrf = await res.json()
    return csrf
}

interface RequestOptions {
    method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
    body?: unknown
}

export async function apiFetch<T>(path: string, options: RequestOptions = {}): Promise<T> {
    const method = options.method ?? 'GET'
    const isMutating = method !== 'GET'
    const isForm = options.body instanceof FormData

    const headers: Record<string, string> = {}

    // Com FormData o navegador define o Content-Type (multipart + boundary).
    if (options.body !== undefined && !isForm) {
        headers['Content-Type'] = 'application/json'
    }

    if (isMutating) {
        if (!csrf) await fetchCsrf()
        if (csrf) headers[csrf.headerName] = csrf.token
    }

    const serializarBody = (): BodyInit | undefined => {
        if (options.body === undefined) return undefined
        return isForm ? (options.body as FormData) : JSON.stringify(options.body)
    }

    const doFetch = () =>
        fetch(`${BASE_URL}${path}`, {
            method,
            headers,
            credentials: 'include',
            body: serializarBody(),
        })

    let response = await doFetch()

    // Token pode ter expirado: tenta renovar uma vez antes de desistir
    if (response.status === 403 && isMutating) {
        await fetchCsrf()
        if (csrf) headers[csrf.headerName] = csrf.token
        response = await doFetch()
    }

    if (response.status === 204) {
        return undefined as T
    }

    if (!response.ok) {
        const message = await response.text().catch(() => '')
        throw new Error(message || `Erro ${response.status}`)
    }

    const text = await response.text()
    return (text ? JSON.parse(text) : undefined) as T
}