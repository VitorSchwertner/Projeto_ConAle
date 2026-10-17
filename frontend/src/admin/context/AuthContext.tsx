import { createContext, useContext, useEffect, useState, type ReactNode } from 'react'
import type { Usuario } from '../types'
import { login as apiLogin, me as apiMe } from '../api/auth'

interface AuthContextValue {
    usuario: Usuario | null
    carregando: boolean
    login: (email: string, senha: string) => Promise<void>
    logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
    const [usuario, setUsuario] = useState<Usuario | null>(null)
    const [carregando, setCarregando] = useState(true)

    useEffect(() => {
        apiMe()
            .then(setUsuario)
            .catch(() => setUsuario(null))
            .finally(() => setCarregando(false))
    }, [])

    const login = async (email: string, senha: string) => {
        const usuarioLogado = await apiLogin(email, senha)
        setUsuario(usuarioLogado)
    }

    const logout = () => {
        setUsuario(null)
    }

    return (
        <AuthContext.Provider value={{ usuario, carregando, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const context = useContext(AuthContext)
    if (!context) throw new Error('useAuth precisa estar dentro de AuthProvider')
    return context
}