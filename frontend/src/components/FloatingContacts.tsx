import { FaWhatsapp } from 'react-icons/fa'
import { FiMail } from 'react-icons/fi'
import './FloatingContacts.css'

interface Props {
    telefone: string | null
    linkEmail: string | null
}

function criarLinkWhatsApp(telefone: string | null) {
    const valor = telefone?.trim() ?? ''
    let numero = valor.replace(/\D/g, '')

    if (numero.startsWith('00')) {
        numero = numero.slice(2)
    } else if (!valor.startsWith('+') && numero.length <= 11) {
        if (numero.length < 10) return null
        numero = `55${numero}`
    }

    return /^[1-9]\d{7,14}$/.test(numero) ? `https://wa.me/${numero}` : null
}

export default function FloatingContacts({ telefone, linkEmail }: Props) {
    const linkWhatsApp = criarLinkWhatsApp(telefone)
    if (!linkWhatsApp && !linkEmail) return null

    return (
        <nav className="floating-contacts" aria-label="Contatos rápidos">
            {linkWhatsApp && (
                <a href={linkWhatsApp} target="_blank" rel="noreferrer" aria-label="Conversar no WhatsApp">
                    <FaWhatsapp size={23} aria-hidden="true" />
                </a>
            )}
            {linkEmail && (
                <a href={linkEmail} aria-label="Enviar e-mail">
                    <FiMail size={22} aria-hidden="true" />
                </a>
            )}
        </nav>
    )
}
