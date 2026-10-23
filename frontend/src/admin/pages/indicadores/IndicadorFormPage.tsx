import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { buscarIndicador, criarIndicador, atualizarIndicador } from '../../api/indicadores'
import './IndicadorFormPage.css'

export default function IndicadorFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const editando = Boolean(id)

    const [anosAtuacao, setAnosAtuacao] = useState('')
    const [empresasAbertas, setEmpresasAbertas] = useState('')
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)

    useEffect(() => {
        if (!id) return
        buscarIndicador(Number(id))
            .then((indicador) => {
                setAnosAtuacao(String(indicador.anosAtuacao))
                setEmpresasAbertas(String(indicador.empresasAbertas))
            })
            .catch(() => setErro('Não foi possível carregar este indicador.'))
            .finally(() => setCarregando(false))
    }, [id])

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setSalvando(true)
        try {
            const dados = {
                anosAtuacao: Number(anosAtuacao),
                empresasAbertas: Number(empresasAbertas),
            }
            if (editando) {
                await atualizarIndicador(Number(id), dados)
            } else {
                await criarIndicador(dados)
            }
            navigate('/admin/indicadores')
        } catch {
            setErro('Não foi possível salvar o indicador.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    return (
        <div className="indicador-form">
            <h1>{editando ? 'Editar indicador' : 'Novo indicador'}</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Anos de atuação
                    <input
                        type="number"
                        min={0}
                        value={anosAtuacao}
                        onChange={(e) => setAnosAtuacao(e.target.value)}
                        required
                    />
                </label>

                <label>
                    Empresas abertas
                    <input
                        type="number"
                        min={0}
                        value={empresasAbertas}
                        onChange={(e) => setEmpresasAbertas(e.target.value)}
                        required
                    />
                </label>

                {erro && <p className="indicador-form__error">{erro}</p>}

                <div className="indicador-form__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}