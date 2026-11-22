import { useBanners } from '../hooks/useBanners'
import BannerComChamada from './BannerComChamada'

export default function ServicesHero({ destinoPadrao = '#lista-servicos' }: { destinoPadrao?: string }) {
    const { servicosBanner } = useBanners()
    const destino = servicosBanner?.link ?? destinoPadrao

    return (
        <BannerComChamada
            variante="servicos"
            banner={servicosBanner}
            chamada="Falar com um especialista"
            destino={destino}
        />
    )
}
