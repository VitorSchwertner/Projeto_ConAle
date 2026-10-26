import Hero from '../components/Hero'
import QuickActions from '../components/QuickActions'
import Stats from '../components/Stats'
import Highlights from '../components/Highlights'
import About from '../components/About'
import ClientArea from '../components/ClientArea'
import Differentials from '../components/Differentials'
import Services from '../components/Services'
import Faq from '../components/Faq'
import Noticias from '../components/Noticias'

export default function Home() {
    return (
        <>
            <Hero />
            <QuickActions />
            <Stats />
            <Highlights />
            <About />
            <ClientArea />
            <Differentials />
            <Services />
            <Noticias />
            <Faq />
        </>
    )
}
