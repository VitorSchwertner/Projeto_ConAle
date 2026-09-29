import { useRef, useState } from 'react'
import './Faq.css'

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
        'No diagnóstico inicial, avaliamos sua estrutura e identificamos possíveis erros, riscos ou oportunidades de melhoria na sua tributação.',
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

export default function Faq() {
  const trackRef = useRef<HTMLDivElement>(null)
  const [activeIndex, setActiveIndex] = useState(0)

  // largura de um card + o espaço entre eles
  const getStep = () => {
    const track = trackRef.current
    const card = track?.querySelector<HTMLElement>('.faq__card')
    return card ? card.offsetWidth + 20 : 0
  }

  const scrollByCard = (direction: 1 | -1) => {
    trackRef.current?.scrollBy({
      left: direction * getStep(),
      behavior: 'smooth',
    })
  }

  const goToCard = (index: number) => {
    trackRef.current?.scrollTo({
      left: index * getStep(),
      behavior: 'smooth',
    })
  }

  const handleScroll = () => {
    const track = trackRef.current
    const step = getStep()
    if (!track || !step) return
    setActiveIndex(Math.round(track.scrollLeft / step))
  }

  return (
      <section className="faq" id="faq">
        <div className="container faq__carousel">
          <button
              className="faq__arrow faq__arrow--prev"
              aria-label="Pergunta anterior"
              onClick={() => scrollByCard(-1)}
          >
            ‹
          </button>

          <div className="faq__track" ref={trackRef} onScroll={handleScroll}>
            {QUESTIONS.map((item) => (
                <article className="faq__card" key={item.question}>
                  <h3>{item.question}</h3>
                  <p>{item.answer}</p>
                </article>
            ))}
          </div>

          <button
              className="faq__arrow faq__arrow--next"
              aria-label="Próxima pergunta"
              onClick={() => scrollByCard(1)}
          >
            ›
          </button>

          <div className="faq__dots">
            {QUESTIONS.map((item, index) => (
                <button
                    key={item.question}
                    className={`faq__dot ${index === activeIndex ? 'is-active' : ''}`}
                    aria-label={`Ir para a pergunta ${index + 1}`}
                    onClick={() => goToCard(index)}
                />
            ))}
          </div>
        </div>
      </section>
  )
}