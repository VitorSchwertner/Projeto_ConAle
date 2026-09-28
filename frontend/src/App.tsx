import { Routes, Route } from 'react-router-dom'
import Header from './components/Header'
import Footer from './components/Footer'
import Home from './pages/Home'
import ServicesPage from './pages/ServicesPage'
import AboutPage from './pages/AboutPage'
import FaqPage from './pages/FaqPage'

export default function App() {
    return (
        <>
            <Header />
            <main>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/servicos" element={<ServicesPage />} />
                    <Route path="/sobre" element={<AboutPage />} />
                    <Route path="/duvidas" element={<FaqPage />} />
                </Routes>
            </main>
            <Footer />
        </>
    )
}