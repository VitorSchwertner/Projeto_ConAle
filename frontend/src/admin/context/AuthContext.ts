import { createContext, useContext } from 'react'
import type { Usuario } from '../types'

export interface AuthContextValue {
    usuario: Usuario | null
    carregando: boolean
    login: (email: string, senha: string) => Promise<void>
    logout: () => Promise<void>
}

export const AuthContext = createContext<AuthContextValue | null>(null)

export function useAuth() {
    const context = useContext(AuthContext)
    if (!context) throw new Error('useAuth precisa estar dentro de AuthProvider')
    return context
}
