import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { urlImagem } from '../../../api/http'
import { resolverLinkPublico } from '../../../linkPublico'
import { mensagemDeErroApi } from '../../api/http'
import {
    buscarBanner,
    criarBanner,
    atualizarBanner,
    enviarImagem,
} from '../../api/banners'
import type { BannerPosition } from '../../types'
import BackButton from '../../components/BackButton'
import ImagemBannerInput from './ImagemBannerInput'
import { FORMATOS } from './bannerFormatos'
import './BannerFormPage.css'

const POSICOES: { value: BannerPosition; label: string }[] = [
    { value: 'HOME_TOPO', label: 'Topo da Home' },
    { value: 'SERVICOS_TOPO', label: 'Topo de Serviços / Dúvidas' },
    { value: 'SOBRE_NOS', label: 'Sobre Nós' },
    { value: 'AREA_CLIENTE', label: 'Área do Cliente' },
    { value: 'SOBRE_MIM_FOTO', label: 'Foto da página Sobre Mim' },
]

export default function BannerFormPage() {
    const { id } = useParams()
    const navigate = useNavigate()
    const editando = Boolean(id)

    const [titulo, setTitulo] = useState('')
    const [imagemAtual, setImagemAtual] = useState('')
    const [imagemMobileAtual, setImagemMobileAtual] = useState('')
    const [arquivo, setArquivo] = useState<File | null>(null)
    const [arquivoMobile, setArquivoMobile] = useState<File | null>(null)
    const [link, setLink] = useState('')
    const [ativo, setAtivo] = useState(true)
    const [posicao, setPosicao] = useState<BannerPosition>('HOME_TOPO')
    const [carregando, setCarregando] = useState(editando)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)
    const aceitaLink = posicao !== 'SOBRE_MIM_FOTO'

    useEffect(() => {
        if (!id) return
        buscarBanner(Number(id))
            .then((banner) => {
                setTitulo(banner.titulo)
                setImagemAtual(banner.imagem)
                setImagemMobileAtual(banner.imagemMobile ?? '')
                setLink(banner.link ?? '')
                setAtivo(banner.ativo)
                setPosicao(banner.posicao ?? 'HOME_TOPO')
            })
            .catch(() => setErro('Não foi possível carregar este banner.'))
            .finally(() => setCarregando(false))
    }, [id])

    // Cada posição tem uma proporção diferente, então trocar a posição
    // descarta as imagens e pede novas.
    const handlePosicao = (nova: BannerPosition) => {
        setPosicao(nova)
        setArquivo(null)
        setArquivoMobile(null)
        setImagemAtual('')
        setImagemMobileAtual('')
    }

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        if (aceitaLink && link.trim() && !resolverLinkPublico(link)) {
            setErro('Confira o link. Use um endereço com https://, uma página do site ou uma seção como #faq.')
            return
        }
        setSalvando(true)
        try {
            const imagem = arquivo ? await enviarImagem(arquivo, posicao, false) : imagemAtual
            if (!imagem) {
                setErro('Selecione uma imagem para o banner.')
                return
            }

            const imagemMobile = arquivoMobile
                ? await enviarImagem(arquivoMobile, posicao, true)
                : imagemMobileAtual || null

            const dados = {
                titulo,
                imagem,
                imagemMobile,
                link: aceitaLink ? link.trim() || null : null,
                ativo,
                posicao,
            }
            if (editando) {
                await atualizarBanner(Number(id), dados)
            } else {
                await criarBanner(dados)
            }
            navigate('/admin/banners')
        } catch (e) {
            setErro(mensagemDeErroApi(e, 'Não foi possível salvar o banner.'))
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    const formato = FORMATOS[posicao]
    const urlDesktop = imagemAtual ? urlImagem(imagemAtual) : null

    return (
        <div className="banner-form">
            <header className="banner-form__header">
                <BackButton to="/admin/banners" label="Voltar para banners" />
                <h1>{editando ? 'Editar banner' : 'Novo banner'}</h1>
            </header>

            <form onSubmit={handleSubmit}>
                <section className="banner-form__section" aria-labelledby="banner-dados">
                    <h2 id="banner-dados">Configuração</h2>
                    <div className="banner-form__fields">
                        <label className="banner-form__field">
                            Posição no site
                            <select
                                value={posicao}
                                onChange={(e) => handlePosicao(e.target.value as BannerPosition)}
                            >
                                {POSICOES.map((p) => (
                                    <option key={p.value} value={p.value}>
                                        {p.label}
                                    </option>
                                ))}
                            </select>
                        </label>

                        <label className="banner-form__field">
                            Título
                            <input value={titulo} maxLength={150} onChange={(e) => setTitulo(e.target.value)} required />
                        </label>

                        {aceitaLink && (
                            <label className="banner-form__field banner-form__field--full">
                                <span>Link do botão <span className="banner-form__optional">(opcional)</span></span>
                                <input value={link} maxLength={500} onChange={(e) => setLink(e.target.value)} />
                            </label>
                        )}
                    </div>

                    <label className="banner-form__checkbox">
                        <input
                            type="checkbox"
                            checked={ativo}
                            onChange={(e) => setAtivo(e.target.checked)}
                        />
                        Exibir banner no site
                    </label>
                </section>

                <section className="banner-form__section" aria-labelledby="banner-imagens">
                    <h2 id="banner-imagens">Imagens</h2>
                    <div className="banner-form__images">
                        <ImagemBannerInput
                            key={`desktop-${posicao}`}
                            dispositivo="desktop"
                            formato={formato.desktop}
                            imagemAtual={urlDesktop}
                            arquivo={arquivo}
                            obrigatorio
                            onChange={setArquivo}
                            onErro={setErro}
                        />

                        <ImagemBannerInput
                            key={`mobile-${posicao}`}
                            dispositivo="mobile"
                            formato={formato.mobile}
                            imagemAtual={imagemMobileAtual ? urlImagem(imagemMobileAtual) : null}
                            arquivo={arquivoMobile}
                            reserva={{ imagem: urlDesktop, arquivo }}
                            onChange={setArquivoMobile}
                            onRemoverAtual={() => setImagemMobileAtual('')}
                            onErro={setErro}
                        />
                    </div>
                </section>

                {erro && <p className="banner-form__error" role="alert">{erro}</p>}

                <div className="banner-form__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}
