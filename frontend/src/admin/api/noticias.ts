import { apiFetch } from './http'
import type { Noticia } from '../types'

export type NoticiaInput = Omit<Noticia, 'id'>

interface NoticiaApi {
    id: number
    titulo: string
    sub_titulo: string | null
}

// O backend usa "sub_titulo" (com underline) no JSON, então convertemos aqui
// pra manter "subTitulo" (camelCase) no resto do código TypeScript.
function paraNoticia(json: NoticiaApi): Noticia {
    return {
        id: json.id,
        titulo: json.titulo,
        subTitulo: json.sub_titulo,
    }
}

function paraCorpoRequisicao(data: NoticiaInput) {
    return {
        titulo: data.titulo,
        sub_titulo: data.subTitulo,
    }
}

export async function listarNoticias(): Promise<Noticia[]> {
    const json = await apiFetch<NoticiaApi[]>('/api/noticias')
    return json.map(paraNoticia)
}

export async function buscarNoticia(id: number): Promise<Noticia> {
    const json = await apiFetch<NoticiaApi>(`/api/noticias/${id}`)
    return paraNoticia(json)
}

export async function criarNoticia(data: NoticiaInput): Promise<Noticia> {
    const json = await apiFetch<NoticiaApi>('/api/noticias', {
        method: 'POST',
        body: paraCorpoRequisicao(data),
    })
    return paraNoticia(json)
}

export async function atualizarNoticia(id: number, data: NoticiaInput): Promise<Noticia> {
    const json = await apiFetch<NoticiaApi>(`/api/noticias/${id}`, {
        method: 'PUT',
        body: paraCorpoRequisicao(data),
    })
    return paraNoticia(json)
}

export function excluirNoticia(id: number) {
    return apiFetch<void>(`/api/noticias/${id}`, { method: 'DELETE' })
}
