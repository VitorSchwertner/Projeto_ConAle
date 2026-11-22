import type { LinksSite } from '../siteLinks'
import Hero from '../components/Hero'
import QuickActions from '../components/QuickActions'
import Stats from '../components/Stats'
import Highlights from '../components/Highlights'
import About from '../components/About'
import ClientArea from '../components/ClientArea'
import Differentials from '../components/Differentials'
import Services from '../components/Services'
import Noticias from '../components/Noticias'

export default function Home({ links }: { links: LinksSite }) {
    return (
        <>
            <Hero />
            <QuickActions links={links} />
            <Stats />
            <Highlights links={links} />
            <About />
            <ClientArea />
            <Differentials />
            <Services links={links} />
            <Noticias />
        </>
    )
}
