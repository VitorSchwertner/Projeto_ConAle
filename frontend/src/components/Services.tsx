import './Services.css'

const SERVICES = [
    {
        title: 'Contabilidade',
        description:
            'Contabilidade estratégica para profissionais da saúde, com foco em organização, segurança tributária e crescimento do negócio.',
    },
    {
        title: 'Bpo Financeiro',
        description:
            'Organização financeira completa para seu consultório ou clínica, com mais controle, previsibilidade e menos burocracia.',
    },
    {
        title: 'Mentoria Financeira',
        description:
            'Acompanhamento estratégico para profissionais da saúde que querem entender, organizar e crescer financeiramente com mais clareza.',
    },
    {
        title: 'Soluções Complementares',
        description:
            'Soluções integradas com parceiros confiáveis para apoiar todas as necessidades do seu negócio.',
    },
]

export default function Services() {
    return (
        <section className="services" id="servicos">
            <div className="container">
                <span className="services__badge">Nossos Serviços</span>
                <div className="services__grid">
                    {SERVICES.map((service) => (
                        <div className="services__item" key={service.title}>
                            <h3>{service.title}</h3>
                            <p>{service.description}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}