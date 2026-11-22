import type { LinksSite } from '../siteLinks'
import ServicesHero from '../components/ServicesHero'
import Noticias from '../components/Noticias'
import Services from '../components/Services'

export default function FaqPage({ links }: { links: LinksSite }) {
    return (
        <>
            <ServicesHero destinoPadrao="#servicos" />
            <Noticias layout="grid" />
            <Services links={links} />
        </>
    )
}
