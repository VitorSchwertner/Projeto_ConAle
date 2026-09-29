import './ServicesHero.css'

export default function ServicesHero() {
    return (
        <section className="services-hero">
            <div className="container services-hero__content">
                <div className="services-hero__card">
                    <p>
                        Fale com quem <strong>entende</strong> da sua rotina e da sua
                        área.
                    </p>
                </div>

                <div className="services-hero__image" role="img" aria-label="Especialista ConAle">
                    <span>Foto aqui</span>
                </div>

                <a href="#lista-servicos" className="btn btn--primary services-hero__cta">
                    Fale com um especialista
                </a>
            </div>
        </section>
    )
}