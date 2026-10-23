import { apiFetch } from './http'
import type { MensagensClientes } from '../types'

export type MensagemInput = Omit<MensagensClientes, 'id'>

export function listarMensagens() {
    return apiFetch<MensagensClientes[]>('/api/mensagens-clientes')
}

export function buscarMensagem(id: number) {
    return apiFetch<MensagensClientes>(`/api/mensagens-clientes/${id}`)
}

export function criarMensagem(data: MensagemInput) {
    return apiFetch<MensagensClientes>('/api/mensagens-clientes', {
        method: 'POST',
        body: data,
    })
}

export function atualizarMensagem(id: number, data: MensagemInput) {
    return apiFetch<MensagensClientes>(`/api/mensagens-clientes/${id}`, {
        method: 'PUT',
        body: data,
    })
}

export function excluirMensagem(id: number) {
    return apiFetch<void>(`/api/mensagens-clientes/${id}`, { method: 'DELETE' })
}