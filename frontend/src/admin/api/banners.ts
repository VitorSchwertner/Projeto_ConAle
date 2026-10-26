import { apiFetch } from './http'
import type { Banner } from '../types'

export type BannerInput = Omit<Banner, 'id'>

export function listarBanners() {
    return apiFetch<Banner[]>('/api/banners')
}

export function buscarBanner(id: number) {
    return apiFetch<Banner>(`/api/banners/${id}`)
}

export function criarBanner(data: BannerInput) {
    return apiFetch<Banner>('/api/banners', { method: 'POST', body: data })
}

export function atualizarBanner(id: number, data: BannerInput) {
    return apiFetch<Banner>(`/api/banners/${id}`, { method: 'PUT', body: data })
}

export function excluirBanner(id: number) {
    return apiFetch<void>(`/api/banners/${id}`, { method: 'DELETE' })
}