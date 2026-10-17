import { apiFetch } from './http'
import type { Usuario } from '../types'

export interface UsuarioCreateInput {
    nome: string
    email: string
    senha: string
    administrador: boolean
}

export interface UsuarioUpdateInput {
    nome: string
    email: string
    senha?: string
    administrador: boolean
}

export function listarUsuarios() {
    return apiFetch<Usuario[]>('/api/usuarios')
}

export function buscarUsuario(id: number) {
    return apiFetch<Usuario>(`/api/usuarios/${id}`)
}

export function criarUsuario(data: UsuarioCreateInput) {
    return apiFetch<Usuario>('/api/usuarios', { method: 'POST', body: data })
}

export function atualizarUsuario(id: number, data: UsuarioUpdateInput) {
    return apiFetch<Usuario>(`/api/usuarios/${id}`, { method: 'PUT', body: data })
}

export function excluirUsuario(id: number) {
    return apiFetch<void>(`/api/usuarios/${id}`, { method: 'DELETE' })
}