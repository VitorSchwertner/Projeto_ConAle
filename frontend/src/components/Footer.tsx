import { FaWhatsapp } from 'react-icons/fa'
import { FiInstagram, FiMail, FiClock, FiMapPin } from 'react-icons/fi'
import { useEffect, useState } from 'react'
import { buscarRodape } from '../api/content'
import type { Rodape } from '../types/content'
import './Footer.css'

const ENDERECO_PADRAO = ['R. Guanabara, 353 - Sala 1', 'São Cristóvão, Lajeado - RS, 95913-122']

const PADRAO: Rodape = {
    id: 0,
    telefone: '+55 (51) 99661-9470',
    instagram: '@conale.consultoria',
    email: 'atendimento.conale@gmail.com',
    diasSemana: 'Segunda à Sexta-feira',
    horasTrabalhadas: '08:00h - 12:00h | 13:00h - 17:15h',
    endereco: ENDERECO_PADRAO.join(', '),
}

export default function Footer() {
    const [rodape, setRodape] = useState<Rodape>(PADRAO)
    const endereco = rodape.endereco?.trim()
    const enderecoPadrao = endereco === PADRAO.endereco || endereco === ENDERECO_PADRAO.join('\n')
    const enderecoExibido = enderecoPadrao ? ENDERECO_PADRAO.join('\n') : endereco
    const consultaMapa = enderecoPadrao ? PADRAO.endereco : endereco?.replace(/\s+/g, ' ')
    const mapa = consultaMapa ? `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(consultaMapa)}` : null

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
                        <div className="footer__row">
                            <FiMail size={24} strokeWidth={1.5} />
                            <a href={`mailto:${rodape.email}`} className="footer__email">
                                {rodape.email}
                            </a>
                        </div>
                        <div className="footer__row">
                            <FiClock size={24} strokeWidth={1.5} />
                            <span>
                {rodape.diasSemana}
                                <br />
                                {rodape.horasTrabalhadas}
              </span>
                        </div>
                        <div className="footer__row">
                            <FiMapPin size={24} strokeWidth={1.5} />
                            {mapa ? (
                                <a href={mapa} target="_blank" rel="noreferrer" className="footer__endereco">
                                    {enderecoExibido}
                                </a>
                            ) : null}
                        </div>
                    </div>

                    {mapa && enderecoPadrao && (
                        <a href={mapa} target="_blank" rel="noreferrer" className="footer__map">
                            <img src="/mapa.png" alt="Mapa de localização da ConAle" />
                        </a>
                    )}
                </div>
            </div>

            <div className="footer__bar">
        <span>
          © {new Date().getFullYear()} ConAle Consultoria Contábil e
          Financeira - Todos os direitos reservados.
        </span>
            </div>
        </footer>
    )
}
