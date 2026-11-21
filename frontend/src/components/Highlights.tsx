import './Highlights.css'

const CARDS = [
    {
        imagem: '/destaques/inicio-de-carreira.png',
        descricao: 'O início da sua carreira merece decisões seguras. Conte com a ConAle! Saiba mais.',
    },
    {
        imagem: '/destaques/gestao-financeira.png',
        descricao: 'Crescer na área da saúde exige mais do que atender mais pacientes. Tenha uma gestão financeira estruturada. Saiba como.',
    },
    {
        imagem: '/destaques/assessoria-estrategica.png',
        descricao: 'Quando a carreira evolui, a contabilidade também precisa evoluir. Saiba mais sobre a nossa assessoria estratégica. Conheça.',
    },
]

export default function Highlights() {
    return (
        <section className="highlights">
            <div className="container highlights__grid">
                {CARDS.map((card) => (
                    <a className="highlights__card" href="#servicos" key={card.imagem}>
                        <img
                            src={card.imagem}
                            alt={card.descricao}
                            width={489}
                            height={266}
                            loading="lazy"
                        />
                    </a>
                ))}
            </div>
        </section>
    )
}
