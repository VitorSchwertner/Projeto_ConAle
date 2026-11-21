import { CLIENT_PORTAL_URL } from '../constants'
import { useBanners } from '../hooks/useBanners'
import './ClientAreaBar.css'

export default function ClientAreaBar() {
    const { areaClienteBanner } = useBanners()

    return (
        <section className="client-area-bar">
            <div className="container client-area-bar__inner">
                <h3>Área do Cliente</h3>
                <a href={areaClienteBanner?.link ?? CLIENT_PORTAL_URL} className="btn btn--primary">Acessar</a>
            </div>
        </section>
    )
}
