import { publicFetch } from './http'
import type { Banner, IndicadorInstitucional, Rodape, MensagensClientes } from '../types/content'

export function listarBannersAtivos() {
    return publicFetch<Banner[]>('/api/banners/ativos')
}

export function listarIndicadores() {
    return publicFetch<IndicadorInstitucional[]>('/api/indicadores-institucionais')
}

export function buscarRodape() {
    return publicFetch<Rodape>('/api/rodape')
}

export function listarMensagensClientes() {
    return publicFetch<MensagensClientes[]>('/api/mensagens-clientes')
}