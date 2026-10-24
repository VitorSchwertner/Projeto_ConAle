import { apiFetch } from './http'
import type { Rodape } from '../types'

export type RodapeInput = Omit<Rodape, 'id'>

export function buscarRodape() {
    return apiFetch<Rodape>('/api/rodape')
}

export function atualizarRodape(data: RodapeInput) {
    return apiFetch<Rodape>('/api/rodape', { method: 'PUT', body: data })
}