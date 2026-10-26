import { useBanners } from '../hooks/useBanners'
import './Hero.css'

export default function Hero() {
    const { homeBanner } = useBanners()

    return (
        <section className="hero" id="home">
            {homeBanner ? (
                <img src={homeBanner.imagem} alt={homeBanner.titulo} className="hero__banner" />
            ) : (
                <div className="hero__banner hero__banner--placeholder">
                    <div className="hero__stripes" />
                </div>
            )}

            <div className="container hero__content">
                <div className="hero__card">
                    <p>
                        Você administra a saúde dos pacientes.
                        <br />
                        <strong>Nós</strong> administramos a sua saúde contábil.
                    </p>
                    <a
                        href={homeBanner?.link ?? '#servicos'}
                        className="btn btn--primary hero__cta hero__cta--mobile"
                    >
                        Saiba mais
                    </a>
                </div>

                <a
                    href={homeBanner?.link ?? '#servicos'}
                    className="btn btn--primary hero__cta hero__cta--desktop"
                >
                    Saiba mais
                </a>
            </div>
        </section>
    )
}