import { useEffect, useState } from 'react'
import { listarBannersAtivos } from '../api/content'
import type { Banner, BannerPosition } from '../types/content'

function encontrarPorPosicao(
    banners: Banner[],
    posicao: BannerPosition,
    indiceReserva: number,
): Banner | null {
    const porPosicao = banners.find((b) => b.posicao === posicao)
    if (porPosicao) return porPosicao

    const ordenados = [...banners].sort((a, b) => a.id - b.id)
    return ordenados[indiceReserva] ?? null
}

export function useBanners() {
    const [homeBanner, setHomeBanner] = useState<Banner | null>(null)
    const [servicosBanner, setServicosBanner] = useState<Banner | null>(null)
    const [sobreBanner, setSobreBanner] = useState<Banner | null>(null)
    const [areaClienteBanner, setAreaClienteBanner] = useState<Banner | null>(null)
    const [sobreMimFotoBanner, setSobreMimFotoBanner] = useState<Banner | null>(null)
    const [carregando, setCarregando] = useState(true)

    useEffect(() => {
        listarBannersAtivos()
            .then((banners) => {
                setHomeBanner(encontrarPorPosicao(banners, 'HOME_TOPO', 0))
                setServicosBanner(encontrarPorPosicao(banners, 'SERVICOS_TOPO', 1))
                setSobreBanner(encontrarPorPosicao(banners, 'SOBRE_NOS', 2))
                setAreaClienteBanner(encontrarPorPosicao(banners, 'AREA_CLIENTE', 3))
                setSobreMimFotoBanner(encontrarPorPosicao(banners, 'SOBRE_MIM_FOTO', 4))
            })
            .catch(() => {
                setHomeBanner(null)
                setServicosBanner(null)
                setSobreBanner(null)
                setAreaClienteBanner(null)
                setSobreMimFotoBanner(null)
            })
            .finally(() => setCarregando(false))
    }, [])

    return {
        homeBanner,
        servicosBanner,
        sobreBanner,
        areaClienteBanner,
        sobreMimFotoBanner,
        carregando,
    }
}
