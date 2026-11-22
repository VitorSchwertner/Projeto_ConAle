import { FaWhatsapp } from 'react-icons/fa'
import { FiInstagram, FiMail, FiClock, FiMapPin } from 'react-icons/fi'
import { useEffect, useState } from 'react'
import { buscarRodape } from '../api/content'
import type { Rodape } from '../types/content'
import FloatingContacts from './FloatingContacts'
import './Footer.css'

const NUMERO_ENDERECO = /^(?:(?:n[º°.]?\s*)?\d+[a-z]?(?:\s|-|$)|s\/n\b)/i
const COMPLEMENTO_ENDERECO = /^(?:sala|apto|apartamento|bloco|andar|conjunto|loja|casa|fundos)\b/i

const PADRAO: Rodape = {
    id: 0,
    telefone: '+55 (51) 99661-9470',
    instagram: '@conale.consultoria',
    email: 'atendimento.conale@gmail.com',
    diasSemana: 'Segunda à Sexta-feira',
    horasTrabalhadas: '08:00h - 12:00h | 13:00h - 17:15h',
    endereco: 'R. Guanabara, 353 - Sala 1, São Cristóvão, Lajeado - RS, 95913-122',
}

function formatarEndereco(endereco: string) {
    if (endereco.includes('\n')) return endereco

    const partes = endereco.split(',').map((parte) => parte.trim()).filter(Boolean)
    let quebra = NUMERO_ENDERECO.test(partes[1] ?? '') ? 2 : 1

    while (quebra < partes.length && COMPLEMENTO_ENDERECO.test(partes[quebra])) {
        quebra++
    }

    return [partes.slice(0, quebra), partes.slice(quebra)]
        .filter((linha) => linha.length > 0)
        .map((linha) => linha.join(', '))
        .join('\n')
}

export default function Footer() {
    const [rodape, setRodape] = useState<Rodape>(PADRAO)
    const email = rodape.email?.trim() ?? ''
    const separadorEmail = email.lastIndexOf('@')
    const linkEmail = separadorEmail > 0
        ? `mailto:${encodeURIComponent(email.slice(0, separadorEmail))}@${encodeURIComponent(email.slice(separadorEmail + 1))}`
        : null
    const endereco = rodape.endereco?.trim()
    const enderecoExibido = endereco ? formatarEndereco(endereco) : ''
    const consultaMapa = endereco
        ? encodeURIComponent(endereco.replace(/,?\s*\r?\n\s*/g, ', ').replace(/\s+/g, ' '))
        : null

    useEffect(() => {
        buscarRodape()
            .then(setRodape)
            .catch(() => {
                // mantém os valores de reserva
            })
    }, [])

    return (
        <footer className="footer">
            <div className="footer__main">
                <div className="container footer__grid">
                    <div className="footer__col">
                        <img src="/logo-branco.png" alt="ConAle" className="footer__logo" />

                        <div className="footer__row">
                            <FaWhatsapp size={24} />
                            <span>{rodape.telefone}</span>
                        </div>
                        <div className="footer__row">
                            <FiInstagram size={24} strokeWidth={1.5} />
                            <span>{rodape.instagram}</span>
                        </div>
                    </div>

                    <div className="footer__col footer__col--info">
                        {email && (
                            <div className="footer__row">
                                <FiMail size={24} strokeWidth={1.5} />
                                <span>{email}</span>
                            </div>
                        )}
                        <div className="footer__row">
                            <FiClock size={24} strokeWidth={1.5} />
                            <span>
                {rodape.diasSemana}
                                <br />
                                {rodape.horasTrabalhadas}
              </span>
                        </div>
                        {enderecoExibido && (
                            <div className="footer__row">
                                <FiMapPin size={24} strokeWidth={1.5} />
                                <span className="footer__endereco">{enderecoExibido}</span>
                            </div>
                        )}
                    </div>

                    {consultaMapa && (
                        <div className="footer__map">
                            <iframe
                                className="footer__map-preview"
                                src={`https://www.google.com/maps?q=${consultaMapa}&z=16&output=embed&hl=pt-BR`}
                                title="Mapa de localização da ConAle"
                                loading="lazy"
                                referrerPolicy="strict-origin-when-cross-origin"
                                tabIndex={-1}
                            />
                            <a
                                className="footer__map-link"
                                href={`https://www.google.com/maps/search/?api=1&query=${consultaMapa}`}
                                target="_blank"
                                rel="noopener noreferrer"
                                aria-label="Abrir localização da ConAle no Google Maps (nova aba)"
                            />
                        </div>
                    )}
                </div>
            </div>

            <div className="footer__bar">
        <span>
          © {new Date().getFullYear()} ConAle Consultoria Contábil e
          Financeira - Todos os direitos reservados.
        </span>
            </div>
            <FloatingContacts telefone={rodape.telefone} linkEmail={linkEmail} />
        </footer>
    )
}
