import { Link } from 'react-router-dom'
import { FiArrowLeft } from 'react-icons/fi'
import './BackButton.css'

type BackButtonProps = { label?: string } & (
    { to: string; onClick?: never } | { to?: never; onClick: () => void }
)

export default function BackButton({ to, onClick, label = 'Voltar' }: BackButtonProps) {
    const conteudo = <><FiArrowLeft size={18} aria-hidden="true" />{label}</>

    return to === undefined ? (
        <button type="button" className="back-button" onClick={onClick}>{conteudo}</button>
    ) : (
        <Link to={to} className="back-button">{conteudo}</Link>
    )
}