import './Differentials.css'

const ITEMS = [
    'Gestão à vista / Controle de tarefas',
    'Atendimento e serviço personalizado',
    'Entregas e guias antecipadas',
    'Rastreabilidade de comunicação',
    'Atendemos empresas de qualquer porte',
    'Diagnóstico inicial',
    'Guias de impostos na palma da mão',
    'Atendimento online a qualquer lugar do país',
]

export default function Differentials() {
    return (
        <section className="differentials">
            <div className="container">
                <h2>Nossos Diferenciais</h2>
                <div className="differentials__grid">
                    {ITEMS.map((item) => (
                        <div className="differentials__item" key={item}>
                            {item}
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}