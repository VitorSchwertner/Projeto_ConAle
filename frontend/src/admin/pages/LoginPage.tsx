import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './LoginPage.css'

export default function LoginPage() {
    const { login } = useAuth()
    const navigate = useNavigate()
    const [email, setEmail] = useState('')
    const [senha, setSenha] = useState('')
    const [erro, setErro] = useState<string | null>(null)
    const [enviando, setEnviando] = useState(false)

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setEnviando(true)
        try {
            await login(email, senha)
            navigate('/admin')
        } catch {
            setErro('E-mail ou senha inválidos.')
        } finally {
            setEnviando(false)
        }
    }

    return (
        <div className="admin-login">
            <form className="admin-login__card" onSubmit={handleSubmit}>
                <h1>Painel Administrativo</h1>
                <p>ConAle Consultoria Contábil e Financeira</p>

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
                    Senha
                    <input
                        type="password"
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required
                    />
                </label>

                {erro && <p className="admin-login__error">{erro}</p>}

                <button type="submit" className="btn btn--primary" disabled={enviando}>
                    {enviando ? 'Entrando...' : 'Entrar'}
                </button>
            </form>
        </div>
    )
}