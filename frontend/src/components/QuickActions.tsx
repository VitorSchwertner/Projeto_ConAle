import './QuickActions.css'

const ACTIONS = [
    'Solicite seu diagnóstico gratuito',
    'Agende uma reunião com a ConAle',
    'Quero abrir um consultório',
    'Quero pagar menos imposto',
    'Quero evitar burocracia',
]

export default function QuickActions() {
    return (
        <section className="quick-actions">
            <div className="container quick-actions__grid">
                {ACTIONS.map((action) => (
                    <button className="quick-actions__item" key={action}>
                        {action}
                    </button>
                ))}
            </div>
        </section>
    )
}