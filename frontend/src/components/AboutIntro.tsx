import { useBanners } from '../hooks/useBanners';
import './AboutIntro.css';

interface Pillar {
    title: string;
    text: string;
}

interface Value {
    title: string;
    text: string;
}

const PILLARS: Pillar[] = [
    {
        title: 'Propósito',
        text: 'Cuidar da saúde financeira de profissionais da saúde, transformando a contabilidade em uma ferramenta de clareza, segurança e crescimento para suas carreiras e negócios.',
    },
    {
        title: 'Missão',
        text: 'Oferecer soluções contábeis, financeiras e estratégicas especializadas para profissionais da área da saúde, com atendimento consultivo, humano e digital, garantindo organização, eficiência e suporte para tomadas de decisão mais seguras.',
    },
];

const VALUES: Value[] = [
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
];

export default function AboutIntro() {
    const { sobreMimFotoBanner } = useBanners() as {
        sobreMimFotoBanner?: { imagem: string; titulo: string }
    };

    return (
        <section className="about-intro" id="sobre">
            <div className="about-intro__decor-wrap">
                <div className="container">
                    {/* Bloco Superior: Foto e Texto Principal */}
                    <div className="about-intro__grid">
                        {sobreMimFotoBanner ? (
                            <img
                                src={sobreMimFotoBanner.imagem}
                                alt={sobreMimFotoBanner.titulo}
                                className="about-intro__image"
                            />
                        ) : (
                            <div className="about-intro__image about-intro__image--placeholder" />
                        )}

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

                    {/* Bloco Intermediário: Propósito e Missão com o "L" menor no canto */}
                    <div className="about-intro__pillars-grid">
                        {PILLARS.map((pillar, index) => (
                            <div className="about-intro__pillar" key={pillar.title}>
                                <svg
                                    className={`about-intro__corner-l about-intro__corner-l--${index === 0 ? 'left' : 'right'}`}
                                    viewBox="0 0 40 40"
                                    fill="none"
                                    aria-hidden="true"
                                >
                                    {index === 0 ? (
                                        <path d="M 40 0 L 10 0 Q 0 0 0 10 L 0 40" stroke="var(--color-navy-light)" strokeWidth="2" />
                                    ) : (
                                        <path d="M 0 0 L 30 0 Q 40 0 40 10 L 40 40" stroke="var(--color-navy-light)" strokeWidth="2" />
                                    )}
                                </svg>
                                <h3>{pillar.title}</h3>
                                <p>{pillar.text}</p>
                            </div>
                        ))}
                    </div>

                    {/* Bloco Inferior: Valores com o "L" menor e título em verde */}
                    <div className="about-intro__values">
                        <h3>Valores</h3>
                        <div className="about-intro__values-wrapper">
                            <svg
                                className="about-intro__corner-l about-intro__corner-l--values"
                                viewBox="0 0 40 40"
                                fill="none"
                                aria-hidden="true"
                            >
                                <path d="M 40 0 L 10 0 Q 0 0 0 10 L 0 40" stroke="var(--color-navy-light)" strokeWidth="2" />
                            </svg>
                            <div className="about-intro__values-grid">
                                {VALUES.map((value) => (
                                    <div className="about-intro__value" key={value.title}>
                                        <h4>{value.title}</h4>
                                        <p>{value.text}</p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}