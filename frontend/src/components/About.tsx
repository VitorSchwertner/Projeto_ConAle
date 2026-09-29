import './About.css'

const BADGES = [
    'Mais de 10 anos de atuação',
    '15 empresas abertas',
    'ATENDIMENTO PARA TODO BRASIL',
]

export default function About() {
    return (
        <section className="about" id="sobre">
            <div className="container about__grid">
                <div className="about__media">
                    <div className="about__image" role="img" aria-label="Profissionais da ConAle">
                        <span>Foto aqui</span>
                    </div>
                    <div className="about__badges">
                        {BADGES.map((badge) => (
                            <span className="about__badge" key={badge}>
                {badge}
              </span>
                        ))}
                    </div>
                </div>

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