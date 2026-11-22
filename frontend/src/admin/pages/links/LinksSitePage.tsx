import { useEffect, useState, type FormEvent } from 'react'
import {
    ACOES_RAPIDAS, DESTAQUES, SERVICOS, ENDPOINT_LINKS_SITE, buscarLinksSite, type LinksSite,
} from '../../../siteLinks'
import { apiFetch, mensagemDeErroApi } from '../../api/http'
import BackButton from '../../components/BackButton'
import './LinksSitePage.css'

const GRUPOS = [
    { titulo: 'Atalhos da Home e de Serviços', campos: ACOES_RAPIDAS },
    { titulo: 'Destaques da Home', campos: DESTAQUES },
    { titulo: 'Nossos Serviços', campos: Object.values(SERVICOS) },
]

export default function LinksSitePage() {
    const [salvos, setSalvos] = useState<LinksSite | null>(null)
    const [links, setLinks] = useState<LinksSite>({})
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState('')
    const [feedback, setFeedback] = useState('')

    useEffect(() => {
        let ativo = true
        buscarLinksSite()
            .then((dados) => {
                if (!ativo) return
                setLinks(dados)
                setSalvos(dados)
            })
            .catch(() => {
                if (ativo) setErro('Não foi possível carregar os links do site. Recarregue a página para tentar novamente.')
            })
        return () => { ativo = false }
    }, [])

    const alteracoes = Object.fromEntries(GRUPOS.flatMap(({ campos }) =>
        campos.flatMap(({ codigo }) => {
            const url = (links[codigo] ?? '').trim()
            return url === (salvos?.[codigo] ?? '') ? [] : [[codigo, url]]
        }),
    ))
    const temAlteracoes = Object.keys(alteracoes).length > 0

    async function salvar(event: FormEvent) {
        event.preventDefault()
        if (!salvos || salvando || !temAlteracoes) return
        setSalvando(true)
        setErro('')
        setFeedback('')
        try {
            await apiFetch<void>(ENDPOINT_LINKS_SITE, { method: 'PATCH', body: alteracoes })
            const atualizados = { ...links, ...alteracoes }
            setLinks(atualizados)
            setSalvos(atualizados)
            setFeedback('Links salvos.')
        } catch (erro) {
            setErro(mensagemDeErroApi(erro, 'Não foi possível salvar os links. Tente novamente.'))
        } finally {
            setSalvando(false)
        }
    }

    return (
        <section className="links-site-form">
            <BackButton to="/admin" />
            <h1>Links do site</h1>
            <p className="links-site-form__ajuda" id="links-site-ajuda">
                Use um endereço completo, um caminho como /servicos ou uma âncora como #servicos.
                Deixe o campo vazio para voltar ao comportamento padrão.
            </p>
            {salvos ? (
                <form onSubmit={salvar} aria-describedby="links-site-ajuda">
                    {GRUPOS.map(({ titulo, campos }) => (
                        <fieldset className="links-site-form__grupo" key={titulo} disabled={salvando}>
                            <legend>{titulo}</legend>
                            <div className="links-site-form__campos">
                                {campos.map(({ codigo, titulo: rotulo }) => (
                                    <label key={codigo}>
                                        {rotulo}
                                        <input
                                            name={codigo}
                                            type="text"
                                            inputMode="url"
                                            maxLength={500}
                                            autoCapitalize="none"
                                            spellCheck={false}
                                            value={links[codigo] ?? ''}
                                            onChange={(event) => {
                                                setLinks({ ...links, [codigo]: event.target.value })
                                                setFeedback('')
                                            }}
                                        />
                                    </label>
                                ))}
                            </div>
                        </fieldset>
                    ))}
                    <div className="links-site-form__acoes">
                        <button className="btn btn--primary" type="submit" disabled={salvando || !temAlteracoes}>
                            {salvando ? 'Salvando...' : 'Salvar links'}
                        </button>
                        {feedback && <p role="status">{feedback}</p>}
                    </div>
                </form>
            ) : !erro && <p role="status">Carregando links...</p>}
            {erro && <p className="links-site-form__erro" role="alert">{erro}</p>}
        </section>
    )
}
