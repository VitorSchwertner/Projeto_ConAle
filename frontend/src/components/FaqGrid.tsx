import './FaqGrid.css'

const QUESTIONS = [
    {
        question: 'Como acessar a área do cliente?',
        answer:
            'O acesso à área do cliente é feito por meio dos dados de login enviados para o seu e-mail. Insira o usuário e a senha e, no primeiro uso, altere a senha dentro da plataforma.',
    },
    {
        question: 'Como funciona o serviço de BPO Financeiro?',
        answer:
            'O BPO Financeiro é a terceirização da gestão financeira do seu negócio. Na prática, nossa equipe assume rotinas como contas a pagar e a receber, conciliação bancária e organização dos fluxos financeiros.',
    },
    {
        question: 'O que preciso para abrir um CNPJ?',
        answer:
            'Para abrir um CNPJ, reúna seus documentos pessoais, a definição da sua atividade profissional e o endereço onde a empresa será registrada. Na abertura, é importante avaliar o enquadramento tributário mais adequado para o seu caso. Nossa equipe orienta cada etapa, cuidando da parte burocrática e garantindo que tudo seja feito de forma correta e estratégica.',
    },
    {
        question: 'Como sei se estou pagando impostos corretamente?',
        answer:
            'No diagnóstico inicial avaliamos sua estrutura e identificamos possíveis erros, riscos ou oportunidades de melhoria na sua tributação.',
    },
    {
        question: 'Posso trocar de contador facilmente?',
        answer:
            'Sim. A troca de contador é um processo simples quando feito com organização e acompanhamento profissional. Podemos cuidar da transição junto ao seu contador atual, garantindo que suas obrigações contábeis continuem em dia, sem interrupções ou complicações.',
    },
    {
        question: 'Como funciona o atendimento da ConAle?',
        answer:
            'Nosso atendimento é 100% digital. Você fala com nossa equipe por WhatsApp, e-mail ou reunião online, com suporte próximo e organizado para facilitar sua rotina.',
    },
]

export default function FaqGrid() {
    return (
        <section className="faq-grid" id="faq">
            <div className="container faq-grid__grid">
                {QUESTIONS.map((item) => (
                    <div className="faq-grid__card" key={item.question}>
                        <h3>{item.question}</h3>
                        <p>{item.answer}</p>
                    </div>
                ))}
            </div>
        </section>
    )
}