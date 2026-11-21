export type Formato = { largura: number; altura: number }

export const FORMATOS: Record<string, { desktop: Formato; mobile: Formato | null }> = {
    HOME_TOPO: {
        desktop: { largura: 1351, altura: 468 },
        mobile: { largura: 800, altura: 800 },
    },
    SERVICOS_TOPO: {
        desktop: { largura: 1351, altura: 468 },
        mobile: { largura: 800, altura: 800 },
    },
    SOBRE_NOS: {
        desktop: { largura: 572, altura: 320 },
        mobile: { largura: 576, altura: 300 },
    },
    AREA_CLIENTE: { desktop: { largura: 684, altura: 220 }, mobile: null },
    SOBRE_MIM_FOTO: { desktop: { largura: 448, altura: 320 }, mobile: null },
}