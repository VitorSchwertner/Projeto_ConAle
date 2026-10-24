import './Highlights.css'

const CARDS = [
    {
        title: 'O início da sua carreira merece decisões seguras.',
        highlight: 'Conte com a ConAle!',
        foto: '/highlght-1.jpg',
    },
    {
        title: 'Crescer na área da saúde exige mais do que atender mais pacientes.',
        highlight: 'Tenha uma gestão financeira estruturada.',
        foto: '/highlight-2.jpg',
    },
    {
        title: 'Quando a carreira evolui, a contabilidade também precisa evoluir.',
        highlight: 'Saiba mais sobre a nossa assessoria estratégica.',
        foto: '/highlight-3.jpg',
    },
]

export default function Highlights() {
    return (
        <section className="highlights">
            <div className="container highlights__grid">
                {CARDS.map((card) => (
                    <div
                        className="highlights__card"
                        key={card.highlight}
                        style={{ backgroundImage: `url(${card.foto})` }}
                    >
                        <div className="highlights__stripes" aria-hidden="true" />
                        <div className="highlights__overlay" aria-hidden="true" />
                        <div className="highlights__content">
                            <p>{card.title}</p>
                            <strong>{card.highlight}</strong>
                            <a href="#servicos" className="btn btn--primary highlights__cta">
                                Saiba mais
                            </a>
                        </div>
                    </div>
                ))}
            </div>
        </section>
    )
}