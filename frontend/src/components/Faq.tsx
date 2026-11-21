import { useRef, useState } from 'react'
import type { Noticia } from '../admin/types'
import './Faq.css'

interface CardsProps {
  cards: Noticia[]
  label: string
}

function FaqCard({ card }: { card: Noticia }) {
  return (
    <article className="faq__card">
      <h3>{card.titulo}</h3>
      {card.subTitulo && <p>{card.subTitulo}</p>}
    </article>
  )
}

function getCardStep(track: HTMLDivElement) {
  const first = track.firstElementChild
  const second = first?.nextElementSibling
  if (!first || !second) return 0

  return second.getBoundingClientRect().left - first.getBoundingClientRect().left
}

export function CardCarousel({ cards, label }: CardsProps) {
  const trackRef = useRef<HTMLDivElement>(null)
  const [activeIndex, setActiveIndex] = useState(0)

  const goToCard = (index: number) => {
    const track = trackRef.current
    if (!track) return
    track.scrollTo({ left: index * getCardStep(track), behavior: 'smooth' })
  }

  const scrollByCard = (direction: 1 | -1) => {
    const track = trackRef.current
    if (!track) return
    const step = getCardStep(track)
    if (!step) return

    const maxIndex = Math.round((track.scrollWidth - track.clientWidth) / step)
    const currentIndex = Math.round(track.scrollLeft / step)
    const positions = maxIndex + 1
    goToCard((currentIndex + direction + positions) % positions)
  }

  const handleScroll = () => {
    const track = trackRef.current
    if (!track) return
    const step = getCardStep(track)
    if (step) setActiveIndex(Math.round(track.scrollLeft / step))
  }

  const hasControls = cards.length > 1
  const desktopFits = cards.length <= 3

  return (
    <section className="faq" aria-label={label}>
      <div className={`container faq__carousel${desktopFits ? ' faq__carousel--fits-desktop' : ''}`}>
        {hasControls && (
          <button
            type="button"
            className="faq__arrow faq__arrow--prev"
            aria-label="Card anterior"
            onClick={() => scrollByCard(-1)}
          >
            ‹
          </button>
        )}

        <div className="faq__track" ref={trackRef} onScroll={handleScroll}>
          {cards.map((card) => (
            <FaqCard key={card.id} card={card} />
          ))}
        </div>

        {hasControls && (
          <>
            <button
              type="button"
              className="faq__arrow faq__arrow--next"
              aria-label="Próximo card"
              onClick={() => scrollByCard(1)}
            >
              ›
            </button>

            <div className="faq__dots">
              {cards.map((card, index) => (
                <button
                  type="button"
                  key={card.id}
                  className={`faq__dot${index === activeIndex ? ' is-active' : ''}`}
                  aria-label={`Ir para o card ${index + 1}`}
                  aria-current={index === activeIndex ? 'true' : undefined}
                  onClick={() => goToCard(index)}
                />
              ))}
            </div>
          </>
        )}
      </div>
    </section>
  )
}

export function CardGrid({ cards, label }: CardsProps) {
  return (
    <section className="faq faq--grid" id="faq" aria-label={label}>
      <div className="container faq__grid">
        {cards.map((card) => (
          <FaqCard key={card.id} card={card} />
        ))}
      </div>
    </section>
  )
}
