import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { buscarUsuario, criarUsuario, atualizarUsuario } from '../../api/usuarios'
import './UsuarioFormPage.css'

export default function UsuarioFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const editando = Boolean(id)

    const [nome, setNome] = useState('')
    const [email, setEmail] = useState('')
    const [senha, setSenha] = useState('')
    const [administrador, setAdministrador] = useState(false)
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)

    useEffect(() => {
        if (!id) return
        buscarUsuario(Number(id))
            .then((usuario) => {
                setNome(usuario.nome)
                setEmail(usuario.email)
                setAdministrador(usuario.administrador)
            })
            .catch(() => setErro('Não foi possível carregar este usuário.'))
            .finally(() => setCarregando(false))
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
                    administrador,
                    ...(senha ? { senha } : {}),
                })
            } else {
                await criarUsuario({ nome, email, senha, administrador })
            }
            navigate('/admin/usuarios')
        } catch {
            setErro('Não foi possível salvar o usuário.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    return (
        <div className="usuario-form">
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
                        value={administrador ? 'administrador' : 'design'}
                        onChange={(e) => setAdministrador(e.target.value === 'administrador')}
                    >
                        <option value="administrador">Administrador</option>
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