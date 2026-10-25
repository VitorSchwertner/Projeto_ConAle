import { useBanners } from '../hooks/useBanners'
import './ServicesHero.css'

export default function ServicesHero() {
    const { servicosBanner } = useBanners()

    return (
        <section className="services-hero">
            {servicosBanner ? (
                <img
                    src={servicosBanner.imagem}
                    alt={servicosBanner.titulo}
                    className="services-hero__banner"
                />
            ) : (
                <div className="services-hero__banner services-hero__banner--placeholder">
                    <div className="services-hero__stripes" />
                </div>
            )}

            <div className="container services-hero__content">
                <div className="services-hero__card">
                    <p>
                        Fale com quem <strong>entende</strong> da sua rotina e da sua
                        área.
                    </p>
                </div>

                <a href="#lista-servicos" className="btn btn--primary services-hero__cta">
                    Fale com um especialista
                </a>
            </div>
        </section>
    )
}