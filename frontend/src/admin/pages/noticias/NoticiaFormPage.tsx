import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { buscarNoticia, criarNoticia, atualizarNoticia } from '../../api/noticias'
import './NoticiaFormPage.css'

export default function NoticiaFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const editando = Boolean(id)

    const [titulo, setTitulo] = useState('')
    const [subTitulo, setSubTitulo] = useState('')
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)

    useEffect(() => {
        if (!id) return
        buscarNoticia(Number(id))
            .then((noticia) => {
                setTitulo(noticia.titulo)
                setSubTitulo(noticia.subTitulo ?? '')
            })
            .catch(() => setErro('Não foi possível carregar esta notícia.'))
            .finally(() => setCarregando(false))
    }, [id])

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setSalvando(true)
        try {
            const dados = { titulo, subTitulo: subTitulo || null }
            if (editando) {
                await atualizarNoticia(Number(id), dados)
            } else {
                await criarNoticia(dados)
            }
            navigate('/admin/noticias')
        } catch {
            setErro('Não foi possível salvar a notícia.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    return (
        <div className="noticia-form">
            <h1>{editando ? 'Editar notícia' : 'Nova notícia'}</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Título
                    <input value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
                </label>

                <label>
                    Subtítulo (opcional)
                    <textarea
                        value={subTitulo}
                        onChange={(e) => setSubTitulo(e.target.value)}
                        rows={3}
                    />
                </label>

                {erro && <p className="noticia-form__error">{erro}</p>}

                <div className="noticia-form__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}