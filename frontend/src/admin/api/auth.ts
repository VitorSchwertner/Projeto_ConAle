import { apiFetch } from './http'
import type { Usuario } from '../types'

export function login(email: string, senha: string) {
    return apiFetch<Usuario>('/api/auth/login', { method: 'POST', body: { email, senha } })
}

export function me() {
    return apiFetch<Usuario>('/api/auth/me')
}