import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarIndicadores, excluirIndicador } from '../../api/indicadores'
import type { IndicadorInstitucional } from '../../types'
import './IndicadorListPage.css'

export default function IndicadorListPage() {
    const [indicadores, setIndicadores] = useState<IndicadorInstitucional[]>([])
    const [carregando, setCarregando] = useState(true)
    const [erro, setErro] = useState<string | null>(null)

    const carregar = () => {
        setCarregando(true)
        listarIndicadores()
            .then(setIndicadores)
            .catch(() => setErro('Não foi possível carregar os indicadores.'))
            .finally(() => setCarregando(false))
    }

    useEffect(() => {
        carregar()
    }, [])

    const handleExcluir = async (id: number) => {
        if (!confirm('Excluir este indicador?')) return
        try {
            await excluirIndicador(id)
            carregar()
        } catch {
            alert('Não foi possível excluir o indicador.')
        }
    }

    if (carregando) return <p>Carregando...</p>
    if (erro) return <p>{erro}</p>

    return (
        <div className="indicador-list">
            <div className="indicador-list__header">
                <h1>Indicadores Institucionais</h1>
                <Link to="/admin/indicadores/novo" className="btn btn--primary">
                    Novo indicador
                </Link>
            </div>

            <table className="indicador-list__table">
                <thead>
                <tr>
                    <th>Anos de atuação</th>
                    <th>Empresas abertas</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {indicadores.map((indicador) => (
                    <tr key={indicador.id}>
                        <td>{indicador.anosAtuacao}</td>
                        <td>{indicador.empresasAbertas}</td>
                        <td className="indicador-list__actions">
                            <Link to={`/admin/indicadores/${indicador.id}`}>Editar</Link>
                            <button onClick={() => handleExcluir(indicador.id)}>Excluir</button>
                        </td>
                    </tr>
                ))}
                {indicadores.length === 0 && (
                    <tr>
                        <td colSpan={3}>Nenhum indicador cadastrado.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}