import { NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import './AdminLayout.css'

const NAV_ITEMS = [
    { to: '/admin', label: 'Dashboard', end: true, adminOnly: false },
    { to: '/admin/banners', label: 'Banners', adminOnly: false },
    { to: '/admin/indicadores', label: 'Indicadores', adminOnly: false },
    { to: '/admin/noticias', label: 'Notícias', adminOnly: false },
    { to: '/admin/rodape', label: 'Rodapé', adminOnly: false },
    { to: '/admin/mensagens', label: 'Mensagens de Clientes', adminOnly: false },
    { to: '/admin/usuarios', label: 'Usuários', adminOnly: true },
]

export default function AdminLayout() {
    const { usuario, logout } = useAuth()

    return (
        <div className="admin-layout">
            <aside className="admin-layout__sidebar">
                <h2>ConAle Admin</h2>
                <nav>
                    {NAV_ITEMS.filter((item) => !item.adminOnly || usuario?.administrador).map(
                        (item) => (
                            <NavLink
                                key={item.to}
                                to={item.to}
                                end={item.end}
                                className={({ isActive }) => (isActive ? 'is-active' : '')}
                            >
                                {item.label}
                            </NavLink>
                        ),
                    )}
                </nav>
            </aside>

            <div className="admin-layout__main">
                <header className="admin-layout__header">
                    <span>{usuario?.nome}</span>
                    <button className="btn btn--outline" onClick={logout}>
                        Sair
                    </button>
                </header>
                <div className="admin-layout__content">
                    <Outlet />
                </div>
            </div>
        </div>
    )
}