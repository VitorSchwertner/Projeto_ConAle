import { Navigate } from 'react-router-dom'
import type { ReactNode } from 'react'
import { useAuth } from '../context/AuthContext'

export default function AdminOnlyRoute({ children }: { children: ReactNode }) {
    const { usuario } = useAuth()

    if (!usuario?.administrador) {
        return <Navigate to="/admin" replace />
    }

    return <>{children}</>
}