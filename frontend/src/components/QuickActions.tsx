import { ACOES_RAPIDAS, type LinksSite } from '../siteLinks'
import './QuickActions.css'

export default function QuickActions({ links }: { links: LinksSite }) {
    return (
        <section className="quick-actions">
            <div className="container quick-actions__grid">
                {ACOES_RAPIDAS.map(({ codigo, titulo }) => {
                    const link = links[codigo]
                    return link ? (
                        <a className="quick-actions__item" href={link} key={codigo}>{titulo}</a>
                    ) : (
                        <button className="quick-actions__item" type="button" disabled key={codigo}>{titulo}</button>
                    )
                })}
            </div>
        </section>
    )
}
