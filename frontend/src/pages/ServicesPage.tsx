import ServicesHero from '../components/ServicesHero'
import ServicesList from '../components/ServicesList'
import QuickActions from '../components/QuickActions'
import ClientAreaBar from '../components/ClientAreaBar'
import Noticias from '../components/Noticias'

export default function ServicesPage() {
    return (
        <>
            <ServicesHero />
            <ServicesList />
            <QuickActions />
            <ClientAreaBar />
            <Noticias />
        </>
    )
}
