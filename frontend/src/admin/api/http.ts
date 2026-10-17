const BASE_URL = 'http://localhost:8080'

let csrf: { headerName: string; token: string } | null = null

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

    const headers: Record<string, string> = {
        'Content-Type': 'application/json',
    }

    if (isMutating) {
        if (!csrf) await fetchCsrf()
        if (csrf) headers[csrf.headerName] = csrf.token
    }

    const doFetch = () =>
        fetch(`${BASE_URL}${path}`, {
            method,
            headers,
            credentials: 'include',
            body: options.body !== undefined ? JSON.stringify(options.body) : undefined,
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