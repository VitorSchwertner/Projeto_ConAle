import { SERVICOS, type LinksSite } from '../siteLinks'
import './ServicesList.css'

const SERVICOS_DETALHADOS = [
    {
        ...SERVICOS.contabilidade,
        foto: '/services-contabilidade.png',
        intro:
            'A contabilidade da ConAle vai além das obrigações fiscais que profissionais da saúde tenham clareza sobre seus números, segurança tributária e suporte para tomada de decisões.',
        bullet: 'Cuidamos de toda rotina contábil, fiscal e trabalhista',
        closing:
            'Nosso objetivo é transformar a contabilidade em uma ferramenta de gestão, e não apenas uma obrigação mensal.',
    },
    {
        ...SERVICOS.bpo,
        foto: '/services-bpo.jpg',
        intro:
            'O BPO Financeiro é a terceirização da gestão financeira do seu negócio. Assumimos rotinas como contas a pagar e a receber, conciliação bancária e organização do fluxo financeiro.',
        bullet:
            'Você passa a ter uma visão clara da sua realidade financeira, com relatórios organizados e informações que facilitam a tomada de decisão.',
        closing:
            'Mais do que organização, o BPO traz previsibilidade e reduz o risco de erros financeiros, permitindo que você foque no atendimento aos seus pacientes e no crescimento da sua carreira.',
    },
    {
        ...SERVICOS.mentoria,
        foto: '/services-mentoria.webp',
        intro:
            'A mentoria financeira é voltada para profissionais da saúde que desejam compreender melhor seus números e tomar decisões mais estratégicas sobre o próprio negócio.',
        bullet:
            'Ajudamos a organizar a visão financeira do consultório ou clínica, identificar gargalos, entender lucratividade e construir uma estrutura mais saudável para crescimento.',
        closing:
            'É um acompanhamento estratégico, focado em clareza, planejamento e evolução financeira.',
    },
    {
        ...SERVICOS.solucoes,
        foto: '/services-solucoes.webp',
        intro:
            'Além dos serviços contábeis e financeiros, a ConAle oferece suporte por meio de parceiros especializados em áreas essenciais para profissionais da saúde.',
        bullet:
            'Isso inclui certificado digital, sistemas de gestão financeira, assessoria jurídica e registro de marca.',
        closing:
            'Nosso objetivo é facilitar a vida do cliente, reunindo em um só ecossistema tudo o que ele precisa para estruturar, proteger e profissionalizar seu negócio.',
    },
]

export default function ServicesList({ links }: { links: LinksSite }) {
    return (
        <section className="services-list" id="lista-servicos">
            <div className="container">
                <h2>Nossos Serviços</h2>
                <p className="services-list__subtitle">
                    Confira a lista dos nossos principais serviços e entenda como a
                    ConAle pode ajudar sua empresa!
                </p>

                {SERVICOS_DETALHADOS.map((service, index) => (
                    <div
                        className={`services-list__row ${index % 2 === 1 ? 'is-reversed' : ''}`}
                        key={service.codigo}
                    >
                        <div className="services-list__image-wrap">
                            <img src={service.foto} alt={service.titulo} className="services-list__image" />
                            <div className="services-list__image-gradient" aria-hidden="true" />
                        </div>

                        <div className="services-list__text">
                            <span className="services-list__accent" aria-hidden="true" />
                            <h3>{service.titulo}</h3>
                            <p>{service.intro}</p>
                            <ul>
                                <li>{service.bullet}</li>
                            </ul>
                            <p className="services-list__closing">{service.closing}</p>
                            {links[service.codigo] ? (
                                <a
                                    className="btn btn--primary"
                                    href={links[service.codigo]}
                                    aria-label={`Contratar ${service.titulo}`}
                                >
                                    Contratar esse serviço
                                </a>
                            ) : (
                                <button className="btn btn--primary" type="button" disabled>
                                    Contratar esse serviço
                                </button>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </section>
    )
}