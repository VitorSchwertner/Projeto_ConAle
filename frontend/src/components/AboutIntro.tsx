import './AboutIntro.css'

const PILLARS = [
    {
        title: 'Propósito',
        text: 'Cuidar da saúde financeira de profissionais da saúde, transformando a contabilidade em uma ferramenta de clareza, segurança e crescimento para suas carreiras e negócios.',
    },
    {
        title: 'Missão',
        text: 'Oferecer soluções contábeis, financeiras e estratégicas especializadas para profissionais da área da saúde, com atendimento consultivo, humano e digital, garantindo organização, eficiência e suporte para tomadas de decisão mais seguras.',
    },
]

const VALUES = [
    {
        title: 'Clareza',
        text: 'Acreditamos em uma contabilidade simples, transparente e fácil de entender.',
    },
    {
        title: 'Segurança',
        text: 'Atuamos com responsabilidade para garantir conformidade fiscal e proteção ao cliente.',
    },
    {
        title: 'Eficiência',
        text: 'Processos digitais e organizados para reduzir burocracia e otimizar tempo.',
    },
    {
        title: 'Confiança',
        text: 'Construímos relações de longo prazo baseadas em ética e credibilidade.',
    },
]

export default function AboutIntro() {
    return (
        <section className="about-intro" id="sobre">
            <div className="container about-intro__grid">
                <div className="about-intro__image" role="img" aria-label="Fundadora da ConAle">
                    <span>Foto aqui</span>
                </div>

                <div className="about-intro__text">
                    <h2>Conheça a ConAle</h2>
                    <p>
                        A ConAle atua ao lado de profissionais da saúde na organização
                        contábil, fiscal e tributária de seus negócios, oferecendo
                        suporte estratégico para uma gestão mais clara, eficiente e
                        segura. Médicos, dentistas, psicólogos, fisioterapeutas,
                        nutricionistas e gestores de clínicas contam com uma assessoria
                        que entende a rotina intensa da área da saúde e as necessidades
                        específicas de quem precisa de previsibilidade financeira.
                    </p>
                    <p>
                        Nosso trabalho vai além do cumprimento de obrigações fiscais.
                        Atuamos na estruturação da gestão, na análise tributária e no
                        suporte às decisões financeiras, ajudando nossos clientes a
                        alcançarem seus resultados e conquistarem oportunidades de
                        crescimento.
                    </p>
                </div>
            </div>

            <div className="container about-intro__pillars">
                {PILLARS.map((pillar) => (
                    <div className="about-intro__pillar" key={pillar.title}>
                        <h3>{pillar.title}</h3>
                        <p>{pillar.text}</p>
                    </div>
                ))}
            </div>

            <div className="container about-intro__values">
                <h3>Valores</h3>
                <div className="about-intro__values-grid">
                    {VALUES.map((value) => (
                        <div className="about-intro__value" key={value.title}>
                            <h4>{value.title}</h4>
                            <p>{value.text}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}