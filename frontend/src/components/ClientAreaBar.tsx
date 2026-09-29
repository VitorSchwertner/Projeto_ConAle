import './ClientAreaBar.css'

export default function ClientAreaBar() {
    return (
        <section className="client-area-bar">
            <div className="container client-area-bar__inner">
                <h3>Área do Cliente</h3>
                <button className="btn btn--primary">Acessar</button>
            </div>
        </section>
    )
}