import { apiFetch, resetCsrf } from './http'
import type { Usuario } from '../types'

export async function login(email: string, senha: string) {
    const usuario = await apiFetch<Usuario>('/api/auth/login', {
        method: 'POST',
        body: { email, senha },
    })
    // O backend troca a sessão e invalida o token usado no login.
    resetCsrf()
    return usuario
}

export function me() {
    return apiFetch<Usuario>('/api/auth/me')
}

export async function logout() {
    try {
        await apiFetch<void>('/api/auth/logout', { method: 'POST' })
    } finally {
        resetCsrf()
    }
}
