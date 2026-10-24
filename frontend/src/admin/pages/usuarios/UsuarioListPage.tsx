import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarUsuarios, excluirUsuario } from '../../api/usuarios'
import { useAuth } from '../../context/AuthContext'
import type { Usuario } from '../../types'
import './UsuarioListPage.css'

export default function UsuarioListPage() {
    const { usuario: usuarioLogado } = useAuth()
    const [usuarios, setUsuarios] = useState<Usuario[]>([])
    const [carregando, setCarregando] = useState(true)
    const [erro, setErro] = useState<string | null>(null)

    const carregar = () => {
        setCarregando(true)
        listarUsuarios()
            .then(setUsuarios)
            .catch(() => setErro('Não foi possível carregar os usuários.'))
            .finally(() => setCarregando(false))
    }

    useEffect(() => {
        carregar()
    }, [])

    const handleExcluir = async (id: number) => {
        if (!confirm('Excluir este usuário?')) return
        try {
            await excluirUsuario(id)
            carregar()
        } catch {
            alert('Não foi possível excluir o usuário.')
        }
    }

    if (carregando) return <p>Carregando...</p>
    if (erro) return <p>{erro}</p>

    return (
        <div className="usuario-list">
            <div className="usuario-list__header">
                <h1>Usuários</h1>
                <Link to="/admin/usuarios/novo" className="btn btn--primary">
                    Novo usuário
                </Link>
            </div>

            <table className="usuario-list__table">
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Papel</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {usuarios.map((item) => (
                    <tr key={item.id}>
                        <td>{item.nome}</td>
                        <td>{item.email}</td>
                        <td>{item.administrador ? 'Administrador' : 'Design'}</td>
                        <td className="usuario-list__actions">
                            <Link to={`/admin/usuarios/${item.id}`}>Editar</Link>
                            {item.id !== usuarioLogado?.id && (
                                <button onClick={() => handleExcluir(item.id)}>Excluir</button>
                            )}
                        </td>
                    </tr>
                ))}
                {usuarios.length === 0 && (
                    <tr>
                        <td colSpan={4}>Nenhum usuário cadastrado.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}