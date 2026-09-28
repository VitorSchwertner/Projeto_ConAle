import './Stats.css'

const STATS = [
    { value: '+0', label: 'anos de atuação' },
    { value: '+0', label: 'empresas abertas' },
]

export default function Stats() {
    return (
        <section className="stats">
            <div className="container stats__inner">
                <p className="stats__intro">
                    Nós ajudamos seu negócio a <strong>crescer</strong>
                </p>
                <div className="stats__row">
                    {STATS.map((stat) => (
                        <div className="stats__item" key={stat.label}>
                            <span className="stats__value">{stat.value}</span>
                            <span className="stats__label">{stat.label}</span>
                        </div>
                    ))}
                    <div className="stats__item stats__item--text">
                        Atendimento em todo Brasil!
                    </div>
                </div>
            </div>
        </section>
    )
}