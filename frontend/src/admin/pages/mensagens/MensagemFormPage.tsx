import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { buscarMensagem, criarMensagem, atualizarMensagem } from '../../api/mensagens'
import './MensagemFormPage.css'

export default function MensagemFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const editando = Boolean(id)

    const [titulo, setTitulo] = useState('')
    const [areaAtuacao, setAreaAtuacao] = useState('')
    const [nota, setNota] = useState('5')
    const [mensagem, setMensagem] = useState('')
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)

    useEffect(() => {
        if (!id) return
        buscarMensagem(Number(id))
            .then((item) => {
                setTitulo(item.titulo)
                setAreaAtuacao(item.areaAtuacao)
                setNota(String(item.nota))
                setMensagem(item.mensagem)
            })
            .catch(() => setErro('Não foi possível carregar esta mensagem.'))
            .finally(() => setCarregando(false))
    }, [id])

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setSalvando(true)
        try {
            const dados = {
                titulo,
                areaAtuacao,
                nota: Number(nota),
                mensagem,
            }
            if (editando) {
                await atualizarMensagem(Number(id), dados)
            } else {
                await criarMensagem(dados)
            }
            navigate('/admin/mensagens')
        } catch {
            setErro('Não foi possível salvar a mensagem.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    return (
        <div className="mensagem-form">
            <h1>{editando ? 'Editar mensagem' : 'Nova mensagem'}</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Título (nome do cliente)
                    <input value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
                </label>

                <label>
                    Área de atuação
                    <input
                        value={areaAtuacao}
                        onChange={(e) => setAreaAtuacao(e.target.value)}
                        placeholder="Ex: Fisioterapeuta"
                        required
                    />
                </label>

                <label>
                    Nota (0 a 5)
                    <input
                        type="number"
                        min={0}
                        max={5}
                        step={0.1}
                        value={nota}
                        onChange={(e) => setNota(e.target.value)}
                        required
                    />
                </label>

                <label>
                    Mensagem
                    <textarea
                        value={mensagem}
                        onChange={(e) => setMensagem(e.target.value)}
                        rows={5}
                        required
                    />
                </label>

                {erro && <p className="mensagem-form__error">{erro}</p>}

                <div className="mensagem-form__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}