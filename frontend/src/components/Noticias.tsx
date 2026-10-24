import { useEffect, useState } from 'react'
import { listarNoticias } from '../admin/api/noticias'
import type { Noticia } from '../admin/types'
import './Noticias.css'

export default function Noticias() {
    const [noticias, setNoticias] = useState<Noticia[]>([])
    const [carregando, setCarregando] = useState(true)
    const [erro, setErro] = useState(false)

    useEffect(() => {
        let ativo = true

        listarNoticias()
            .then((dados) => {
                if (ativo) setNoticias(dados)
            })
            .catch(() => {
                if (ativo) setErro(true)
            })
            .finally(() => {
                if (ativo) setCarregando(false)
            })

        return () => {
            ativo = false
        }
    }, [])

    return (
        <section className="noticias" aria-labelledby="noticias-titulo">
            <div className="container">
                <div className="noticias__cabecalho">
                    <span>Conteúdo atualizado pela ConAle</span>
                    <h2 id="noticias-titulo">Notícias</h2>
                </div>

                {carregando && <p className="noticias__estado">Carregando notícias...</p>}
                {!carregando && erro && (
                    <p className="noticias__estado" role="status">
                        As notícias estão temporariamente indisponíveis.
                    </p>
                )}
                {!carregando && !erro && noticias.length === 0 && (
                    <p className="noticias__estado">Nenhuma notícia publicada no momento.</p>
                )}

                {!carregando && !erro && noticias.length > 0 && (
                    <div className="noticias__grid">
                        {noticias.map((noticia) => (
                            <article className="noticias__card" key={noticia.id}>
                                <h3>{noticia.titulo}</h3>
                                {noticia.subTitulo && <p>{noticia.subTitulo}</p>}
                            </article>
                        ))}
                    </div>
                )}
            </div>
        </section>
    )
}
