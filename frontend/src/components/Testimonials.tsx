import './Testimonials.css'

const TESTIMONIALS = [
    {
        name: 'Ana',
        role: 'Fisioterapeuta',
        quote:
            'Sempre retirando as dúvidas com carinho e atenção, eu recomendo hoje a ConAle por toda a parceria que temos e pela transparência no trabalho.',
    },
    {
        name: 'Vanessa e Thaís',
        role: 'Fisioterapeutas',
        quote:
            'Queremos agradecer o trabalho e a dedicação da empresa ConAle conosco, sempre nos respondem da forma rápida e esclarecem todas as dúvidas que surgem.',
    },
    {
        name: 'Janaína',
        role: 'Psicóloga',
        quote:
            'A minha experiência com os serviços oferecidos pela ConAle é positiva. A Alexandra está sempre disposta a resolver todas as dúvidas, trazendo as informações com clareza nos proporcionando segurança.',
    },
    {
        name: 'Flávia',
        role: 'Terapeuta',
        quote:
            'A ConAle conta com profissionais dedicados, que fazem muito mais do que números: oferecem segurança, clareza e soluções inteligentes para cada cliente. Recomendo de olhos fechados!',
    },
    {
        name: 'Gabriela',
        role: 'Médica',
        quote:
            'Trabalhar com a ConAle Contabilidade é ter a certeza de que estamos em boas mãos! Profissionais capacitados, humanos e competentes. São parceiros que estão dispostos a ajudar e entender qual o melhor cenário para a empresa!',
    },
]

export default function Testimonials() {
    return (
        <section className="testimonials">
            <div className="container">
                <h2>Nossos Clientes</h2>
                <div className="testimonials__grid">
                    {TESTIMONIALS.map((item) => (
                        <div className="testimonials__card" key={item.name}>
                            <div className="testimonials__header">
                                <div>
                                    <strong>{item.name}</strong>
                                    <span>{item.role}</span>
                                </div>
                                <span className="testimonials__stars">★★★★★</span>
                            </div>
                            <p>"{item.quote}"</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}