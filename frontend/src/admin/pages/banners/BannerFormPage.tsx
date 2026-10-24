import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { buscarBanner, criarBanner, atualizarBanner } from '../../api/banners'
import './BannerFormPage.css'

const POSICOES = [
    { value: 'HOME_TOPO', label: 'Topo da Home' },
    { value: 'SERVICOS_TOPO', label: 'Topo de Serviços / Dúvidas' },
    { value: 'SOBRE_NOS', label: 'Sobre Nós' },
    { value: 'AREA_CLIENTE', label: 'Área do Cliente' },
]

export default function BannerFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const editando = Boolean(id)

    const [titulo, setTitulo] = useState('')
    const [imagem, setImagem] = useState('')
    const [link, setLink] = useState('')
    const [ativo, setAtivo] = useState(true)
    const [posicao, setPosicao] = useState('HOME_TOPO')
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)

    useEffect(() => {
        if (!id) return
        buscarBanner(Number(id))
            .then((banner) => {
                setTitulo(banner.titulo)
                setImagem(banner.imagem)
                setLink(banner.link ?? '')
                setAtivo(banner.ativo)
                setPosicao(banner.posicao ?? 'HOME_TOPO')
            })
            .catch(() => setErro('Não foi possível carregar este banner.'))
            .finally(() => setCarregando(false))
    }, [id])

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setSalvando(true)
        try {
            const dados = { titulo, imagem, link: link || null, ativo, posicao }
            if (editando) {
                await atualizarBanner(Number(id), dados)
            } else {
                await criarBanner(dados)
            }
            navigate('/admin/banners')
        } catch {
            setErro('Não foi possível salvar o banner.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    return (
        <div className="banner-form">
            <h1>{editando ? 'Editar banner' : 'Novo banner'}</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Posição no site
                    <select value={posicao} onChange={(e) => setPosicao(e.target.value)}>
                        {POSICOES.map((p) => (
                            <option key={p.value} value={p.value}>
                                {p.label}
                            </option>
                        ))}
                    </select>
                </label>

                <label>
                    Título
                    <input value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
                </label>

                <label>
                    Imagem (caminho ou URL)
                    <input value={imagem} onChange={(e) => setImagem(e.target.value)} required />
                </label>

                <label>
                    Link (opcional)
                    <input value={link} onChange={(e) => setLink(e.target.value)} />
                </label>

                <label className="banner-form__checkbox">
                    <input
                        type="checkbox"
                        checked={ativo}
                        onChange={(e) => setAtivo(e.target.checked)}
                    />
                    Banner ativo (exibido no site)
                </label>

                {erro && <p className="banner-form__error">{erro}</p>}

                <div className="banner-form__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}