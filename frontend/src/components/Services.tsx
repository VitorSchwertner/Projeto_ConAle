import { SERVICOS, type LinksSite } from '../siteLinks'
import './Services.css'

const SERVICOS_RESUMO = [
    {
        ...SERVICOS.contabilidade,
        description:
            'Contabilidade estratégica para profissionais da saúde, com foco em organização, segurança tributária e crescimento do negócio.',
    },
    {
        ...SERVICOS.bpo,
        description:
            'Organização financeira completa para seu consultório ou clínica, com mais controle, previsibilidade e menos burocracia.',
    },
    {
        ...SERVICOS.mentoria,
        description:
            'Acompanhamento estratégico para profissionais da saúde que querem entender, organizar e crescer financeiramente com mais clareza.',
    },
    {
        ...SERVICOS.solucoes,
        description:
            'Soluções integradas com parceiros confiáveis para apoiar todas as necessidades do seu negócio.',
    },
]

export default function Services({ links }: { links: LinksSite }) {
    return (
        <section className="services" id="servicos">
            <div className="container">
                <span className="services__badge">Nossos Serviços</span>
                <div className="services__grid">
                    {SERVICOS_RESUMO.map(({ codigo, titulo, description }) => {
                        const link = links[codigo]
                        const conteudo = <><h3>{titulo}</h3><p>{description}</p></>
                        return link ? (
                            <a className="services__item" href={link} key={codigo}>{conteudo}</a>
                        ) : (
                            <div className="services__item" key={codigo}>{conteudo}</div>
                        )
                    })}
                </div>
            </div>
        </section>
    )
}