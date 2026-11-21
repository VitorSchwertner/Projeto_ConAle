import { apiFetch } from './http'
import type { Banner, BannerPosition } from '../types'

export type BannerInput = Omit<Banner, 'id'>

export async function enviarImagem(arquivo: File, posicao: BannerPosition, mobile: boolean) {
    const dados = new FormData()
    dados.append('arquivo', arquivo)
    dados.append('posicao', posicao)
    dados.append('mobile', String(mobile))

    const { caminho } = await apiFetch<{ caminho: string }>('/api/banners/upload', {
        method: 'POST',
        body: dados,
    })
    return caminho
}

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
