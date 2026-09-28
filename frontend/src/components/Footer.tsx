import { FaWhatsapp } from 'react-icons/fa'
import { FiInstagram, FiMail, FiClock, FiMapPin } from 'react-icons/fi'
import './Footer.css'

const MAPS_LINK =
    'https://www.google.com/maps/search/?api=1&query=R.+Guanabara,+353,+Lajeado+-+RS'

export default function Footer() {
    return (
        <footer className="footer">
            <div className="footer__main">
                <div className="container footer__grid">
                    <div className="footer__col">
                        <img src="/logo-branco.png" alt="ConAle" className="footer__logo" />

                        <div className="footer__row">
                            <FaWhatsapp size={40} />
                            <span>+55 (51) 9 9661 - 9470</span>
                        </div>
                        <div className="footer__row">
                            <FiInstagram size={40} strokeWidth={1.5} />
                            <span>@conale.consultoria</span>
                        </div>
                    </div>

                    <div className="footer__col footer__col--info">
                        <div className="footer__row">
                            <FiMail size={40} strokeWidth={1.5} />
                            <a href="mailto:atendimento.conale@gmail.com" className="footer__email">
                                atendimento.conale@gmail.com
                            </a>
                        </div>
                        <div className="footer__row">
                            <FiClock size={40} strokeWidth={1.5} />
                            <span>
                Segunda à Sexta-feira
                <br />
                08:00h - 12:00h | 13:00h - 17:15h
              </span>
                        </div>
                        <div className="footer__row">
                            <FiMapPin size={40} strokeWidth={1.5} />
                            <span>
                R. Guanabara, 353 - Sala 1
                <br />
                São Cristóvão, Lajeado - RS, 95913-122
              </span>
                        </div>
                    </div>

                    <a href={MAPS_LINK} target="_blank" rel="noreferrer" className="footer__map">
                        <img src="/mapa.png" alt="Mapa de localização da ConAle" />
                    </a>
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