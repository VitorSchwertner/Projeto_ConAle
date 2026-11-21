import type { BannerPosition } from '../../types'

export type Formato = { largura: number; altura: number }

export const FORMATOS: Record<BannerPosition, { desktop: Formato; mobile: Formato }> = {
    HOME_TOPO: {
        desktop: { largura: 1923, altura: 699 },
        mobile: { largura: 375, altura: 360 },
    },
    SERVICOS_TOPO: {
        desktop: { largura: 1923, altura: 699 },
        mobile: { largura: 383, altura: 332 },
    },
    SOBRE_NOS: {
        desktop: { largura: 572, altura: 320 },
        mobile: { largura: 576, altura: 300 },
    },
    AREA_CLIENTE: {
        desktop: { largura: 684, altura: 220 },
        mobile: { largura: 576, altura: 324 },
    },
    SOBRE_MIM_FOTO: {
        desktop: { largura: 491, altura: 574 },
        mobile: { largura: 255, altura: 283 },
    },
}
