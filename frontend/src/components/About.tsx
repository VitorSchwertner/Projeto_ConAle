import { useBanners } from '../hooks/useBanners'
import './About.css'

export default function About() {
    const { sobreBanner } = useBanners()

    return (
        <section className="about" id="sobre">
            <div className="container about__grid">
                {sobreBanner ? (
                    <img src={sobreBanner.imagem} alt={sobreBanner.titulo} className="about__banner" />
                ) : (
                    <div className="about__banner about__banner--placeholder" />
                )}

                <div className="about__text">
                    <h2>Sobre Nós</h2>
                    <p>
                        A ConAle atua ao lado de profissionais da saúde na organização
                        contábil, fiscal e tributária de seus negócios, oferecendo
                        suporte estratégico para uma gestão mais clara, eficiente e
                        segura.
                    </p>
                    <a href="#servicos" className="btn btn--primary">
                        Saiba Mais
                    </a>
                </div>
            </div>
        </section>
    )
}