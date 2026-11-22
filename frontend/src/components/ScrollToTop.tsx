import { useEffect } from 'react'
import { useLocation } from 'react-router-dom'

export default function ScrollToTop() {
    const { pathname, hash } = useLocation()

    useEffect(() => {
        if (!hash) {
            window.scrollTo(0, 0)
            return
        }

        let id: string
        try {
            id = decodeURIComponent(hash.slice(1))
        } catch {
            return
        }

        const conteudo = document.querySelector('main')
        if (!conteudo) return

        let frame = 0
        function alinharSecao() {
            cancelAnimationFrame(frame)
            frame = requestAnimationFrame(() => {
                document.getElementById(id)?.scrollIntoView({ block: 'start' })
            })
        }

        // Cards e banners podem alterar a posição da seção depois da navegação.
        const mudancas = new MutationObserver(alinharSecao)
        const dimensoes = new ResizeObserver(alinharSecao)
        mudancas.observe(conteudo, { childList: true, subtree: true })
        dimensoes.observe(conteudo)
        alinharSecao()

        const interacoes = ['pointerdown', 'touchstart', 'wheel', 'keydown'] as const
        function parar() {
            cancelAnimationFrame(frame)
            mudancas.disconnect()
            dimensoes.disconnect()
            for (const evento of interacoes) window.removeEventListener(evento, parar)
        }

        for (const evento of interacoes) window.addEventListener(evento, parar, { passive: true })
        return parar
    }, [pathname, hash])

    return null
}
