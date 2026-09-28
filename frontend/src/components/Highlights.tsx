import './Highlights.css'

const CARDS = [
    {
        title: 'O início da sua carreira merece decisões seguras.',
        highlight: 'Conte com a ConAle!',
    },
    {
        title: 'Crescer na área da saúde exige mais do que atender mais pacientes.',
        highlight: 'Tenha uma gestão financeira estruturada.',
    },
    {
        title: 'Quando a carreira evolui, a contabilidade também precisa evoluir.',
        highlight: 'Saiba mais sobre a nossa assessoria estratégica.',
    },
]

export default function Highlights() {
    return (
        <section className="highlights">
            <div className="container highlights__grid">
                {CARDS.map((card) => (
                    <div className="highlights__card" key={card.highlight}>
                        <p>{card.title}</p>
                        <strong>{card.highlight}</strong>
                        <a href="#servicos" className="btn btn--primary highlights__cta">
                            Saiba mais
                        </a>
                    </div>
                ))}
            </div>
        </section>
    )
}