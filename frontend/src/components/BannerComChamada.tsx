import { urlImagem } from '../api/http'
import type { Banner } from '../types/content'
import './BannerComChamada.css'

interface Props {
    id?: string
    variante: 'home' | 'servicos'
    banner: Banner | null
    chamada: string
    destino: string
}

export default function BannerComChamada({ id, variante, banner, chamada, destino }: Props) {
    const imagemMobile = banner?.imagemMobile
    const classeMobile = imagemMobile ? ' banner-com-chamada--com-imagem-mobile' : ''

    return (
        <section className={`banner-com-chamada banner-com-chamada--${variante}${classeMobile}`} id={id}>
            {banner?.imagem && (
                <picture className="banner-com-chamada__arte">
                    {imagemMobile && (
                        <source media="(max-width: 860px)" srcSet={urlImagem(imagemMobile)} />
                    )}
                    <img src={urlImagem(banner.imagem)} alt={banner.titulo} width={1923} height={699} />
                </picture>
            )}
            <a className="banner-com-chamada__link" href={destino}>{chamada}</a>
        </section>
    )
}
