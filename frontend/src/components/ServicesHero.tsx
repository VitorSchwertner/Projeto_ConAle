import { useBanners } from '../hooks/useBanners'
import BannerComChamada from './BannerComChamada'

function destinoDaChamada(link: string | null | undefined, destinoPadrao: string) {
    if (link === '#lista-servicos' && destinoPadrao === '#servicos') return '/servicos#lista-servicos'
    if (link === '#servicos' && destinoPadrao === '#lista-servicos') return '/#servicos'
    return link ?? destinoPadrao
}

export default function ServicesHero({ destinoPadrao = '#lista-servicos' }: { destinoPadrao?: string }) {
    const { servicosBanner } = useBanners()
    const destino = destinoDaChamada(servicosBanner?.link, destinoPadrao)

    return (
        <BannerComChamada
            variante="servicos"
            banner={servicosBanner}
            chamada="Falar com um especialista"
            destino={destino}
        />
    )
}
