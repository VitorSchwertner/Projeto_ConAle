const BASE_URL = 'http://localhost:8080'

export async function publicFetch<T>(path: string): Promise<T> {
    const response = await fetch(`${BASE_URL}${path}`)
    if (!response.ok) {
        throw new Error(`Erro ${response.status} ao buscar ${path}`)
    }
    return response.json()
}