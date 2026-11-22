const SECOES_POR_PAGINA = new Map<string, readonly string[]>([
    ['/', ['home', 'sobre', 'area-cliente', 'servicos']],
    ['/servicos', ['lista-servicos']],
    ['/sobre', ['sobre', 'area-cliente']],
    ['/duvidas', ['faq', 'servicos']],
])

function normalizarPagina(pagina: string) {
    return pagina.toLowerCase().replace(/\/$/, '') || '/'
}

export function resolverLinkPublico(valor: string | null | undefined, pagina = '/'): string | null {
    const link = valor?.trim()
    if (!link || /[\s\\]/.test(link)) return null

    if (link.startsWith('#')) {
        const secao = link.slice(1)
        if (SECOES_POR_PAGINA.get(normalizarPagina(pagina))?.includes(secao)) return link

        for (const [destino, secoes] of SECOES_POR_PAGINA) {
            if (secoes.includes(secao)) return `${destino}${link}`
        }
        return null
    }

    if (link.startsWith('/') && !link.startsWith('//')) {
        const caminho = link.split(/[?#]/, 1)[0]
        const secoes = SECOES_POR_PAGINA.get(normalizarPagina(caminho))
        const indiceHash = link.indexOf('#')
        return secoes && (indiceHash === -1 || secoes.includes(link.slice(indiceHash + 1))) ? link : null
    }

    if (!/^https?:\/\//i.test(link)) return null
    try {
        return new URL(link).hostname ? link : null
    } catch {
        return null
    }
}
