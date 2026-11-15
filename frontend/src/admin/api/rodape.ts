import { apiFetch } from './http'
import type { Rodape } from '../types'

const ROTA = '/api/rodape/modelos'

export interface RodapeModelo {
    nome: string
    ativo: boolean
    dados: Rodape
}

export interface RodapeModeloInput {
    nome: string
    dados: Omit<Rodape, 'id'>
}

export function listarRodapes() {
    return apiFetch<RodapeModelo[]>(ROTA)
}

export function criarRodape(data: RodapeModeloInput) {
    return apiFetch<RodapeModelo>(ROTA, { method: 'POST', body: data })
}

export function atualizarRodape(id: number, data: RodapeModeloInput) {
    return apiFetch<RodapeModelo>(`${ROTA}/${id}`, { method: 'PUT', body: data })
}

export function ativarRodape(id: number) {
    return apiFetch<void>(`${ROTA}/${id}/ativacao`, { method: 'PUT' })
}

export function excluirRodape(id: number) {
    return apiFetch<void>(`${ROTA}/${id}`, { method: 'DELETE' })
}
