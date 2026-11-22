import { useBanners } from '../hooks/useBanners'
import BannerComChamada from './BannerComChamada'

export default function Hero() {
    const { homeBanner } = useBanners()
    return (
        <BannerComChamada
            id="home"
            variante="home"
            banner={homeBanner}
            chamada="Saiba mais"
            destino={homeBanner?.link ?? '#servicos'}
        />
    )
}
