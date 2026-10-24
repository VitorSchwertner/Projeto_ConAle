import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarMensagens, excluirMensagem } from '../../api/mensagens'
import type { MensagensClientes } from '../../types'
import './MensagemListPage.css'

export default function MensagemListPage() {
    const [mensagens, setMensagens] = useState<MensagensClientes[]>([])
    const [carregando, setCarregando] = useState(true)
    const [erro, setErro] = useState<string | null>(null)

    const carregar = () => {
        setCarregando(true)
        listarMensagens()
            .then(setMensagens)
            .catch(() => setErro('Não foi possível carregar as mensagens.'))
            .finally(() => setCarregando(false))
    }

    useEffect(() => {
        carregar()
    }, [])

    const handleExcluir = async (id: number) => {
        if (!confirm('Excluir esta mensagem?')) return
        try {
            await excluirMensagem(id)
            carregar()
        } catch {
            alert('Não foi possível excluir a mensagem.')
        }
    }

    if (carregando) return <p>Carregando...</p>
    if (erro) return <p>{erro}</p>

    return (
        <div className="mensagem-list">
            <div className="mensagem-list__header">
                <h1>Mensagens de Clientes</h1>
                <Link to="/admin/mensagens/nova" className="btn btn--primary">
                    Nova mensagem
                </Link>
            </div>

            <table className="mensagem-list__table">
                <thead>
                <tr>
                    <th>Título</th>
                    <th>Área de atuação</th>
                    <th>Nota</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {mensagens.map((mensagem) => (
                    <tr key={mensagem.id}>
                        <td>{mensagem.titulo}</td>
                        <td>{mensagem.areaAtuacao}</td>
                        <td>{mensagem.nota.toFixed(1)}</td>
                        <td className="mensagem-list__actions">
                            <Link to={`/admin/mensagens/${mensagem.id}`}>Editar</Link>
                            <button onClick={() => handleExcluir(mensagem.id)}>Excluir</button>
                        </td>
                    </tr>
                ))}
                {mensagens.length === 0 && (
                    <tr>
                        <td colSpan={4}>Nenhuma mensagem cadastrada.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}