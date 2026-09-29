import { useState } from 'react'
import { Link } from 'react-router-dom'
import './Header.css'

const NAV_LINKS = [
    { label: 'Home', href: '/' },
    { label: 'Serviços', href: '/servicos' },
    { label: 'Sobre Mim', href: '/sobre' },
    { label: 'Dúvidas Frequentes', href: '/duvidas' },
]

export default function Header() {
    const [menuOpen, setMenuOpen] = useState(false)

    return (
        <header className="header">
            <div className="container header__bar">
                <Link to="/" className="header__logo">
                    <img
                        src="/logo.png"
                        alt="ConAle Consultoria Contábil e Financeira"
                    />
                </Link>

                <nav className={`header__nav ${menuOpen ? 'is-open' : ''}`}>
                    <ul className="header__links">
                        {NAV_LINKS.map((link) => (
                            <li key={link.href}>
                                <Link to={link.href} onClick={() => setMenuOpen(false)}>
                                    {link.label}
                                </Link>
                            </li>
                        ))}
                    </ul>
                    <Link to="/#area-cliente" className="btn btn--primary header__cta">
                        Área do Cliente
                    </Link>
                </nav>

                <button
                    className={`header__toggle ${menuOpen ? 'is-open' : ''}`}
                    aria-label="Abrir menu"
                    aria-expanded={menuOpen}
                    onClick={() => setMenuOpen((open) => !open)}
                >
                    <span />
                    <span />
                    <span />
                </button>
            </div>
        </header>
    )
}