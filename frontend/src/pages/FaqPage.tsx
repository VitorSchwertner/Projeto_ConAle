import ServicesHero from '../components/ServicesHero'
import Noticias from '../components/Noticias'
import Services from '../components/Services'

export default function FaqPage() {
    return (
        <>
            <ServicesHero />
            <Noticias layout="grid" />
            <Services />
        </>
    )
}
