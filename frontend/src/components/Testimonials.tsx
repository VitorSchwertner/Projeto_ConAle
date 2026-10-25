import { useEffect, useState } from 'react'
import { listarMensagensClientes } from '../api/content'
import type { MensagensClientes } from '../types/content'
import './Testimonials.css'

export default function Testimonials() {
    const [mensagens, setMensagens] = useState<MensagensClientes[]>([])
    const [carregando, setCarregando] = useState(true)

    useEffect(() => {
        listarMensagensClientes()
            .then(setMensagens)
            .catch(() => setMensagens([]))
            .finally(() => setCarregando(false))
    }, [])

    return (
        <section className="testimonials">
            <div className="container">
                <h2>Nossos Clientes</h2>

                {carregando && <p className="testimonials__status">Carregando depoimentos...</p>}

                {!carregando && mensagens.length === 0 && (
                    <p className="testimonials__status">
                        Nenhum depoimento cadastrado ainda. Cadastre pelo painel administrativo.
                    </p>
                )}

                {!carregando && mensagens.length > 0 && (
                    <div className="testimonials__grid">
                        {mensagens.map((item) => (
                            <div className="testimonials__card" key={item.id}>
                                <div className="testimonials__header">
                                    <div>
                                        <strong>{item.titulo}</strong>
                                        <span>{item.areaAtuacao}</span>
                                    </div>
                                    <span className="testimonials__stars">
                    {'★'.repeat(Math.round(item.nota))}
                                        {'☆'.repeat(5 - Math.round(item.nota))}
                  </span>
                                </div>
                                <p>"{item.mensagem}"</p>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </section>
    )
}