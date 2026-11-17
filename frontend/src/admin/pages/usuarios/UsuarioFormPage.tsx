import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import BackButton from '../../components/BackButton'
import { buscarUsuario, criarUsuario, atualizarUsuario } from '../../api/usuarios'
import { useAuth } from '../../context/AuthContext'
import './UsuarioFormPage.css'

export default function UsuarioFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const { usuario: usuarioLogado } = useAuth()
    const editando = Boolean(id)

    const [nome, setNome] = useState('')
    const [email, setEmail] = useState('')
    const [senha, setSenha] = useState('')
    const [administrador, setAdministrador] = useState(false)
    const [usuarioEditado, setUsuarioEditado] = useState<{ id: number; administradorOriginal: boolean } | null>(null)
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)
    const podeEditarPapel = Boolean(
        usuarioLogado?.administradorOriginal && (!editando || !usuarioEditado?.administradorOriginal),
    )

    useEffect(() => {
        if (!id) return
        let ativo = true
        buscarUsuario(Number(id))
            .then((usuario) => {
                if (!ativo) return
                setNome(usuario.nome)
                setEmail(usuario.email)
                setAdministrador(usuario.administrador)
                setUsuarioEditado(usuario)
            })
            .catch(() => {
                if (ativo) setErro('Não foi possível carregar este usuário.')
            })
            .finally(() => {
                if (ativo) setCarregando(false)
            })
        return () => { ativo = false }
    }, [id])

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setSalvando(true)
        try {
            if (editando) {
                await atualizarUsuario(Number(id), {
                    nome,
                    email,
                    ...(podeEditarPapel ? { administrador } : {}),
                    ...(senha ? { senha } : {}),
                })
            } else {
                await criarUsuario({ nome, email, senha, administrador: podeEditarPapel && administrador })
            }
            navigate('/admin/usuarios')
        } catch {
            setErro('Não foi possível salvar o usuário.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>
    if (editando && usuarioEditado?.id !== Number(id)) {
        return <p>{erro ?? 'Carregando...'}</p>
    }
    if (editando && usuarioEditado?.administradorOriginal && usuarioEditado.id !== usuarioLogado?.id) {
        return <p>Somente o administrador original pode editar os dados da própria conta.</p>
    }

    return (
        <div className="usuario-form">
            <BackButton to="/admin/usuarios" />
            <h1>{editando ? 'Editar usuário' : 'Novo usuário'}</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Nome
                    <input value={nome} onChange={(e) => setNome(e.target.value)} required />
                </label>

                <label>
                    E-mail
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </label>

                <label>
                    Papel
                    <select
                        value={(editando || podeEditarPapel) && administrador ? 'administrador' : 'design'}
                        disabled={!podeEditarPapel}
                        onChange={(e) => setAdministrador(e.target.value === 'administrador')}
                    >
                        <option value="administrador">
                            {editando && usuarioEditado?.administradorOriginal ? 'Administrador original' : 'Administrador'}
                        </option>
                        <option value="design">Design</option>
                    </select>
                </label>

                <label>
                    {editando ? 'Nova senha (deixe em branco para manter)' : 'Senha'}
                    <input
                        type="password"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required={!editando}
                    />
                </label>

                {erro && <p className="usuario-form__error">{erro}</p>}

                <div className="usuario-form__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}