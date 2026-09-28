import './Hero.css'

export default function Hero() {
    return (
        <section className="hero" id="home">
            <div className="hero__stripes" aria-hidden="true" />
            <div className="container hero__content">
                <div className="hero__card">
                    <p>
                        Você administra a saúde dos pacientes.
                        <br />
                        <strong>Nós</strong> administramos a sua saúde contábil.
                    </p>
                    <a href="#servicos" className="btn btn--primary hero__cta hero__cta--mobile">
                        Saiba mais
                    </a>
                </div>

                <div className="hero__image" role="img" aria-label="Profissional da área da saúde">
                    <span>Foto aqui</span>
                </div>

                <a href="#servicos" className="btn btn--primary hero__cta hero__cta--desktop">
                    Saiba mais
                </a>
            </div>
        </section>
    )
}