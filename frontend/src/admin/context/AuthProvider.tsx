import { useEffect, useState, type ReactNode } from 'react'
import { login as apiLogin, logout as apiLogout, me as apiMe } from '../api/auth'
import type { Usuario } from '../types'
import { AuthContext } from './AuthContext'

export function AuthProvider({ children }: { children: ReactNode }) {
    const [usuario, setUsuario] = useState<Usuario | null>(null)
    const [carregando, setCarregando] = useState(true)

    useEffect(() => {
        let ativo = true

        apiMe()
            .then((usuarioAtual) => {
                if (ativo) setUsuario(usuarioAtual)
            })
            .catch(() => {
                if (ativo) setUsuario(null)
            })
            .finally(() => {
                if (ativo) setCarregando(false)
            })

        return () => {
            ativo = false
        }
    }, [])

    const login = async (email: string, senha: string) => {
        const usuarioLogado = await apiLogin(email, senha)
        setUsuario(usuarioLogado)
    }

    const logout = async () => {
        await apiLogout()
        setUsuario(null)
    }

    return (
        <AuthContext.Provider value={{ usuario, carregando, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}
