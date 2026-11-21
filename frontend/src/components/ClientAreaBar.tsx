import { CLIENT_PORTAL_URL } from '../constants'
import './ClientAreaBar.css'

export default function ClientAreaBar() {
    return (
        <section className="client-area-bar">
            <div className="container client-area-bar__inner">
                <h3>Área do Cliente</h3>
                <a href={CLIENT_PORTAL_URL} className="btn btn--primary">Acessar</a>
            </div>
        </section>
    )
}