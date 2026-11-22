import { DESTAQUES, type LinksSite } from '../siteLinks'
import './Highlights.css'

export default function Highlights({ links }: { links: LinksSite }) {
    return (
        <section className="highlights">
            <div className="container highlights__grid">
                {DESTAQUES.map((card) => (
                    <article className="highlights__card" key={card.codigo}>
                        <img
                            src={card.imagem}
                            alt={card.descricao}
                            width={489}
                            height={266}
                            loading="lazy"
                        />
                        <a
                            className="highlights__link"
                            href={links[card.codigo] ?? '#servicos'}
                            aria-label={card.chamada}
                        />
                    </article>
                ))}
            </div>
        </section>
    )
}
