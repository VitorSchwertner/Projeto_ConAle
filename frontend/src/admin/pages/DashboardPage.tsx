import { useAuth } from '../context/AuthContext'

export default function DashboardPage() {
    const { usuario } = useAuth()

    return (
        <div style={{ padding: 40 }}>
            <h1>Painel Administrativo</h1>
            <p>Bem-vindo(a), {usuario?.nome}!</p>
        </div>
    )
}
