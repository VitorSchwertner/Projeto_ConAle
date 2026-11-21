import { useEffect, useState } from 'react'
import { listarNoticias } from '../admin/api/noticias'
import type { Noticia } from '../admin/types'
import { CardCarousel, CardGrid } from './Faq'

export default function Noticias({ layout = 'carousel' }: { layout?: 'carousel' | 'grid' }) {
    const [noticias, setNoticias] = useState<Noticia[]>([])

    useEffect(() => {
        let ativo = true

        listarNoticias()
            .then((dados) => {
                if (ativo) setNoticias(dados)
            })
            .catch(() => {
                if (ativo) setNoticias([])
            })

        return () => {
            ativo = false
        }
    }, [])

    if (noticias.length === 0) return null

    const Cards = layout === 'grid' ? CardGrid : CardCarousel
    return <Cards cards={noticias} label="Dúvidas frequentes" />
}
