import { useEffect, useState, type FormEvent } from 'react'
import BackButton from '../../components/BackButton'
import {
    ativarRodape, atualizarRodape, criarRodape, excluirRodape, listarRodapes,
    type RodapeModelo, type RodapeModeloInput,
} from '../../api/rodape'
import './RodapePage.css'

const CAMPOS = [
    { chave: 'telefone', rotulo: 'Telefone', tipo: 'tel', limite: 20 },
    { chave: 'instagram', rotulo: 'Instagram (opcional)', tipo: 'text', limite: 100 },
    { chave: 'email', rotulo: 'E-mail', tipo: 'email', limite: 254 },
    { chave: 'diasSemana', rotulo: 'Dias de funcionamento', tipo: 'text', limite: 100 },
    { chave: 'horasTrabalhadas', rotulo: 'Horário de funcionamento', tipo: 'text', limite: 100 },
] as const

const NOVO: RodapeModeloInput = {
    nome: '',
    dados: { telefone: '', instagram: null, email: '', diasSemana: '', horasTrabalhadas: '', endereco: '' },
}

export default function RodapePage() {
    const [modelos, setModelos] = useState<RodapeModelo[] | null>(null)
    const [edicao, setEdicao] = useState<{ id: number | null; valores: RodapeModeloInput } | null>(null)
    const [ocupado, setOcupado] = useState(false)
    const [erro, setErro] = useState<string | null>(null)
    const [sucesso, setSucesso] = useState<string | null>(null)

    useEffect(() => {
        let ativo = true
        listarRodapes()
            .then((dados) => { if (ativo) setModelos(dados) })
            .catch(() => { if (ativo) setErro('Não foi possível carregar os rodapés.') })
        return () => { ativo = false }
    }, [])

    async function executar(operacao: () => Promise<void>, mensagem: string) {
        setOcupado(true)
        setErro(null)
        setSucesso(null)
        try {
            await operacao()
            setSucesso(mensagem)
            try {
                setModelos(await listarRodapes())
            } catch {
                setModelos(null)
                setErro('Não foi possível atualizar a lista de rodapés.')
            }
        } catch {
            setErro('Não foi possível concluir a operação. Verifique qual rodapé está ativo e tente novamente.')
            try {
                setModelos(await listarRodapes())
            } catch {
                // Mantém o erro da operação e preserva os dados do formulário.
            }
        } finally {
            setOcupado(false)
        }
    }

    function abrirFormulario(modelo?: RodapeModelo) {
        setErro(null)
        setSucesso(null)
        if (modelo) {
            const { id, ...dados } = modelo.dados
            setEdicao({ id, valores: { nome: modelo.nome, dados } })
        } else {
            setEdicao({ id: null, valores: NOVO })
        }
    }

    function alterarCampo(campo: keyof RodapeModeloInput['dados'], valor: string) {
        setEdicao((atual) => atual && ({
            ...atual,
            valores: { ...atual.valores, dados: { ...atual.valores.dados, [campo]: valor } },
        }))
    }

    function salvar(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        if (!edicao) return
        const { id, valores } = edicao
        void executar(async () => {
            if (id === null) await criarRodape(valores)
            else await atualizarRodape(id, valores)
            setEdicao(null)
        }, 'Rodapé salvo.')
    }

    function fecharFormulario() {
        setEdicao(null)
        setErro(null)
    }

    function excluir(modelo: RodapeModelo) {
        if (confirm(`Excluir o rodapé "${modelo.nome}"?`)) {
            void executar(() => excluirRodape(modelo.dados.id), 'Rodapé excluído.')
        }
    }

    async function recarregar() {
        setErro(null)
        try {
            setModelos(await listarRodapes())
        } catch {
            setErro('Não foi possível carregar os rodapés.')
        }
    }

    return (
        <div className="rodape-page">
            <div className="rodape-page__header">
                {edicao && <BackButton onClick={fecharFormulario} />}
                <h1>{edicao ? (edicao.id === null ? 'Novo rodapé' : 'Editar rodapé') : 'Rodapé'}</h1>
                {!edicao && modelos && (
                    <button className="btn btn--primary" disabled={ocupado} onClick={() => abrirFormulario()}>
                        Novo rodapé
                    </button>
                )}
            </div>
            {erro && <p className="rodape-page__error" role="alert">{erro}</p>}
            {sucesso && <p className="rodape-page__success" role="status">{sucesso}</p>}

            {edicao ? (
                <form onSubmit={salvar}>
                    <label>
                        Nome interno
                        <input
                            value={edicao.valores.nome}
                            onChange={(e) => {
                                const nome = e.target.value
                                setEdicao((atual) => atual && ({ ...atual, valores: { ...atual.valores, nome } }))
                            }}
                            maxLength={100}
                            required
                        />
                    </label>
                    {CAMPOS.map(({ chave, rotulo, tipo, limite }) => (
                        <label key={chave}>
                            {rotulo}
                            <input
                                type={tipo}
                                value={edicao.valores.dados[chave] ?? ''}
                                onChange={(e) => alterarCampo(chave, e.target.value)}
                                maxLength={limite}
                                required={chave !== 'instagram'}
                            />
                        </label>
                    ))}
                    <label>
                        Endereço
                        <textarea
                            value={edicao.valores.dados.endereco ?? ''}
                            onChange={(e) => alterarCampo('endereco', e.target.value)}
                            maxLength={255}
                            rows={2}
                            placeholder={'Rua, número e complemento\nBairro, cidade - UF, CEP'}
                            required
                        />
                    </label>
                    <div className="rodape-page__actions">
                        <button type="submit" className="btn btn--primary" disabled={ocupado}>Salvar</button>
                        <button type="button" disabled={ocupado} onClick={fecharFormulario}>
                            Cancelar
                        </button>
                    </div>
                </form>
            ) : modelos ? (
                <>
                    <p>O rodapé ativo aparece em todas as páginas do site.</p>
                    <ul className="rodape-page__modelos">
                        {modelos.map((modelo) => (
                            <li key={modelo.dados.id}>
                                <div>
                                    <strong>{modelo.nome}</strong>
                                    <span className="rodape-page__estado">{modelo.ativo ? 'Ativo no site' : 'Inativo'}</span>
                                </div>
                                <div className="rodape-page__actions">
                                    <button disabled={ocupado} onClick={() => abrirFormulario(modelo)}>Editar</button>
                                    {!modelo.ativo && (
                                        <>
                                            <button disabled={ocupado} onClick={() => {
                                                void executar(() => ativarRodape(modelo.dados.id), 'Rodapé ativado.')
                                            }}>Ativar</button>
                                            <button disabled={ocupado} onClick={() => excluir(modelo)}>Excluir</button>
                                        </>
                                    )}
                                </div>
                            </li>
                        ))}
                    </ul>
                </>
            ) : erro ? (
                <button className="btn btn--primary" onClick={() => void recarregar()}>Tentar novamente</button>
            ) : (
                <p>Carregando...</p>
            )}
        </div>
    )
}
