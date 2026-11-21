import { useSyncExternalStore } from 'react'
import type { Banner } from '../types/content'

const consultaMobile = '(max-width: 860px)'

function observarLargura(onChange: () => void) {
    const consulta = window.matchMedia(consultaMobile)
    consulta.addEventListener('change', onChange)
    return () => consulta.removeEventListener('change', onChange)
}

function larguraMobile() {
    return window.matchMedia(consultaMobile).matches
}

export function useImagemBanner(banner: Banner | null) {
    const mobile = useSyncExternalStore(observarLargura, larguraMobile)
    if (!banner) return null
    return mobile ? banner.imagemMobile || banner.imagem : banner.imagem
}
