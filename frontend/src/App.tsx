import { useEffect, useState } from 'react'
import { Link, Routes, Route, useLocation } from 'react-router-dom'
import { resolverLinkPublico } from './linkPublico'
import { buscarLinksSite, type LinksSite } from './siteLinks'
import Header from './components/Header'
import Footer from './components/Footer'
import ScrollToTop from './components/ScrollToTop'
import Home from './pages/Home'
import ServicesPage from './pages/ServicesPage'
import AboutPage from './pages/AboutPage'
import FaqPage from './pages/FaqPage'
import LoginPage from './admin/pages/LoginPage'
import DashboardPage from './admin/pages/DashboardPage'
import LinksSitePage from './admin/pages/links/LinksSitePage'
import ProtectedRoute from './admin/components/ProtectedRoute'
import AdminOnlyRoute from './admin/components/AdminOnlyRoute'
import AdminLayout from './admin/components/AdminLayout'
import BannerListPage from './admin/pages/banners/BannerListPage'
import BannerFormPage from './admin/pages/banners/BannerFormPage'
import NoticiaListPage from './admin/pages/noticias/NoticiaListPage'
import NoticiaFormPage from './admin/pages/noticias/NoticiaFormPage'
import RodapePage from './admin/pages/rodape/RodapePage'
import IndicadorListPage from './admin/pages/indicadores/IndicadorListPage'
import IndicadorFormPage from './admin/pages/indicadores/IndicadorFormPage'
import MensagemListPage from './admin/pages/mensagens/MensagemListPage'
import MensagemFormPage from './admin/pages/mensagens/MensagemFormPage'
import UsuarioListPage from './admin/pages/usuarios/UsuarioListPage'
import UsuarioFormPage from './admin/pages/usuarios/UsuarioFormPage'

function SiteLayout() {
    const { pathname } = useLocation()
    const [links, setLinks] = useState<LinksSite>({})
    const linksPublicos = Object.fromEntries(Object.entries(links).flatMap(([codigo, valor]) => {
        const destino = resolverLinkPublico(valor, pathname)
        return destino ? [[codigo, destino]] : []
    }))

    useEffect(() => {
        let ativo = true
        buscarLinksSite()
            .then((dados) => {
                if (ativo) setLinks(dados)
            })
            .catch(() => {
                // Mantém os destinos padrão quando a API está indisponível.
            })
        return () => { ativo = false }
    }, [])

    return (
        <>
            <Header />
            <main>
                <Routes>
                    <Route path="/" element={<Home links={linksPublicos} />} />
                    <Route path="/servicos" element={<ServicesPage links={linksPublicos} />} />
                    <Route path="/sobre" element={<AboutPage />} />
                    <Route path="/duvidas" element={<FaqPage links={linksPublicos} />} />
                    <Route path="*" element={
                        <section className="container pagina-nao-encontrada">
                            <h1>Página não encontrada</h1>
                            <p>Confira o endereço ou volte à página inicial.</p>
                            <Link to="/" className="btn btn--primary">Voltar à Home</Link>
                        </section>
                    } />
                </Routes>
            </main>
            <Footer />
        </>
    )
}

export default function App() {
    return (
        <>
            <ScrollToTop />
            <Routes>
                <Route path="/admin/login" element={<LoginPage />} />

                <Route
                    path="/admin"
                    element={
                        <ProtectedRoute>
                            <AdminLayout />
                        </ProtectedRoute>
                    }
                >
                    <Route index element={<DashboardPage />} />

                    <Route path="banners" element={<BannerListPage />} />
                    <Route path="banners/novo" element={<BannerFormPage />} />
                    <Route path="banners/:id" element={<BannerFormPage />} />
                    <Route path="links" element={<LinksSitePage />} />

                    <Route path="noticias" element={<NoticiaListPage />} />
                    <Route path="noticias/nova" element={<NoticiaFormPage />} />
                    <Route path="noticias/:id" element={<NoticiaFormPage />} />

                    <Route path="rodape" element={<RodapePage />} />

                    <Route path="indicadores" element={<IndicadorListPage />} />
                    <Route path="indicadores/novo" element={<IndicadorFormPage />} />
                    <Route path="indicadores/:id" element={<IndicadorFormPage />} />

                    <Route path="mensagens" element={<MensagemListPage />} />
                    <Route path="mensagens/nova" element={<MensagemFormPage />} />
                    <Route path="mensagens/:id" element={<MensagemFormPage />} />

                    <Route
                        path="usuarios"
                        element={
                            <AdminOnlyRoute>
                                <UsuarioListPage />
                            </AdminOnlyRoute>
                        }
                    />
                    <Route
                        path="usuarios/novo"
                        element={
                            <AdminOnlyRoute>
                                <UsuarioFormPage />
                            </AdminOnlyRoute>
                        }
                    />
                    <Route
                        path="usuarios/:id"
                        element={
                            <AdminOnlyRoute>
                                <UsuarioFormPage />
                            </AdminOnlyRoute>
                        }
                    />
                </Route>

                <Route path="/*" element={<SiteLayout />} />
            </Routes>
        </>
    )
}