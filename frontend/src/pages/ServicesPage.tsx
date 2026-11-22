import type { LinksSite } from '../siteLinks'
import ServicesHero from '../components/ServicesHero'
import ServicesList from '../components/ServicesList'
import QuickActions from '../components/QuickActions'
import ClientAreaBar from '../components/ClientAreaBar'
import Noticias from '../components/Noticias'

export default function ServicesPage({ links }: { links: LinksSite }) {
    return (
        <>
            <ServicesHero />
            <ServicesList links={links} />
            <QuickActions links={links} />
            <ClientAreaBar />
            <Noticias />
        </>
    )
}
