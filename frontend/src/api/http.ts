const configuredBaseUrl = import.meta.env.VITE_API_URL?.trim()
const BASE_URL = configuredBaseUrl ? configuredBaseUrl.replace(/\/$/, '') : ''

export async function publicFetch<T>(path: string): Promise<T> {
    const response = await fetch(`${BASE_URL}${path}`)
    if (!response.ok) {
        throw new Error(`Erro ${response.status} ao buscar ${path}`)
    }
    return response.json()
}
