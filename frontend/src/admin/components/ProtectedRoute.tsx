import { Navigate } from 'react-router-dom'
import type { ReactNode } from 'react'
import { useAuth } from '../context/AuthContext'

export default function ProtectedRoute({ children }: { children: ReactNode }) {
    const { usuario, carregando } = useAuth()

    if (carregando) {
        return <div style={{ padding: 40 }}>Carregando...</div>
    }

    if (!usuario) {
        return <Navigate to="/admin/login" replace />
    }

    return <>{children}</>
}