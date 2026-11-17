import { Link } from 'react-router-dom'
import { FiArrowLeft } from 'react-icons/fi'
import './BackButton.css'

interface BackButtonProps {
    to: string
    label?: string
}

export default function BackButton({ to, label = 'Voltar' }: BackButtonProps) {
    return (
        <Link to={to} className="back-button">
            <FiArrowLeft size={18} />
            {label}
        </Link>
    )
}