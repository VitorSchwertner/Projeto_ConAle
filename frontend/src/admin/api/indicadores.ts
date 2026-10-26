import { apiFetch } from './http'
import type { IndicadorInstitucional } from '../types'

export type IndicadorInput = Omit<IndicadorInstitucional, 'id'>

export function listarIndicadores() {
    return apiFetch<IndicadorInstitucional[]>('/api/indicadores-institucionais')
}

export function buscarIndicador(id: number) {
    return apiFetch<IndicadorInstitucional>(`/api/indicadores-institucionais/${id}`)
}

export function criarIndicador(data: IndicadorInput) {
    return apiFetch<IndicadorInstitucional>('/api/indicadores-institucionais', {
        method: 'POST',
        body: data,
    })
}

export function atualizarIndicador(id: number, data: IndicadorInput) {
    return apiFetch<IndicadorInstitucional>(`/api/indicadores-institucionais/${id}`, {
        method: 'PUT',
        body: data,
    })
}

export function excluirIndicador(id: number) {
    return apiFetch<void>(`/api/indicadores-institucionais/${id}`, { method: 'DELETE' })
}