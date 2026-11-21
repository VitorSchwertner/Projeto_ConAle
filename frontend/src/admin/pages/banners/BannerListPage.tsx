import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarBanners, excluirBanner } from '../../api/banners'
import type { Banner } from '../../types'
import './BannerListPage.css'

function destinoNoPainel(banner: Banner) {
    const link = banner.link?.trim()
    if (!link || banner.posicao === 'SOBRE_MIM_FOTO') return null
    if (!link.startsWith('#')) return link

    const pagina = banner.posicao === 'SERVICOS_TOPO' && link !== '#servicos' ? '/servicos' : '/'
    return `${pagina}${link}`
}

export default function BannerListPage() {
    const [banners, setBanners] = useState<Banner[]>([])
    const [carregando, setCarregando] = useState(true)
    const [erro, setErro] = useState<string | null>(null)

    const carregar = () => {
        setCarregando(true)
        listarBanners()
            .then(setBanners)
            .catch(() => setErro('Não foi possível carregar os banners.'))
            .finally(() => setCarregando(false))
    }

    useEffect(() => {
        let ativo = true
        listarBanners()
            .then((dados) => {
                if (ativo) setBanners(dados)
            })
            .catch(() => {
                if (ativo) setErro('Não foi possível carregar os banners.')
            })
            .finally(() => {
                if (ativo) setCarregando(false)
            })

        return () => {
            ativo = false
        }
    }, [])

    const handleExcluir = async (id: number) => {
        if (!confirm('Excluir este banner?')) return
        try {
            await excluirBanner(id)
            carregar()
        } catch {
            alert('Não foi possível excluir o banner.')
        }
    }

    if (carregando) return <p>Carregando...</p>
    if (erro) return <p>{erro}</p>

    return (
        <div className="banner-list">
            <div className="banner-list__header">
                <h1>Banners</h1>
                <Link to="/admin/banners/novo" className="btn btn--primary">
                    Novo banner
                </Link>
            </div>

            <table className="banner-list__table">
                <thead>
                <tr>
                    <th>Título</th>
                    <th>Link</th>
                    <th>Ativo</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {banners.map((banner) => {
                    const destino = destinoNoPainel(banner)
                    return (
                        <tr key={banner.id}>
                            <td>{banner.titulo}</td>
                            <td>
                                {destino ? (
                                    <a className="banner-list__link" href={destino} target="_blank" rel="noopener noreferrer">
                                        {banner.link}
                                    </a>
                                ) : '—'}
                            </td>
                            <td>{banner.ativo ? 'Sim' : 'Não'}</td>
                            <td className="banner-list__actions">
                                <Link to={`/admin/banners/${banner.id}`}>Editar</Link>
                                <button onClick={() => handleExcluir(banner.id)}>Excluir</button>
                            </td>
                        </tr>
                    )
                })}
                {banners.length === 0 && (
                    <tr>
                        <td colSpan={4}>Nenhum banner cadastrado.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}
