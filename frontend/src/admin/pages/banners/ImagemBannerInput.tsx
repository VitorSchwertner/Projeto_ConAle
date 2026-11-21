import { useEffect, useId, useRef, useState, type ChangeEvent } from 'react'
import Cropper, { type Area } from 'react-easy-crop'
import { FiImage, FiMonitor, FiSmartphone, FiUpload } from 'react-icons/fi'
import type { Formato } from './bannerFormatos'
import './ImagemBannerInput.css'

const TIPOS_ACEITOS = ['image/jpeg', 'image/png']
const TAMANHO_MAXIMO = 5 * 1024 * 1024

type Props = {
    dispositivo: 'desktop' | 'mobile'
    formato: Formato
    imagemAtual: string | null // URL completa da imagem já salva
    arquivo: File | null // imagem nova já recortada
    reserva?: { imagem: string | null; arquivo: File | null }
    obrigatorio?: boolean
    onChange: (arquivo: File | null) => void
    onRemoverAtual?: () => void
    onErro: (mensagem: string | null) => void
}

function carregarImagem(src: string): Promise<HTMLImageElement> {
    return new Promise((resolve, reject) => {
        const img = new Image()
        img.onload = () => resolve(img)
        img.onerror = () => reject(new Error('Não foi possível abrir a imagem.'))
        img.src = src
    })
}

// Recorta a área escolhida e já devolve no tamanho final da posição.
async function recortar(src: string, area: Area, formato: Formato): Promise<File> {
    const img = await carregarImagem(src)
    const canvas = document.createElement('canvas')
    canvas.width = formato.largura
    canvas.height = formato.altura

    const ctx = canvas.getContext('2d')
    if (!ctx) throw new Error('Não foi possível processar a imagem.')

    ctx.fillStyle = '#fff'
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    ctx.drawImage(
        img, area.x, area.y, area.width, area.height,
        0, 0, formato.largura, formato.altura,
    )

    const blob = await new Promise<Blob | null>((r) => canvas.toBlob(r, 'image/jpeg', 0.92))
    if (!blob) throw new Error('Não foi possível processar a imagem.')
    return new File([blob], 'banner.jpg', { type: 'image/jpeg' })
}

export default function ImagemBannerInput({
    dispositivo, formato, imagemAtual, arquivo, reserva, obrigatorio, onChange, onRemoverAtual, onErro,
}: Props) {
    const [origem, setOrigem] = useState<string | null>(null) // imagem original no recorte
    const [posicao, setPosicao] = useState({ x: 0, y: 0 })
    const [zoom, setZoom] = useState(1)
    const [area, setArea] = useState<Area | null>(null)
    const [recortando, setRecortando] = useState(false)
    const [erroRecorte, setErroRecorte] = useState<string | null>(null)
    const inputRef = useRef<HTMLInputElement>(null)
    const previewRef = useRef<HTMLImageElement>(null)
    const modalRef = useRef<HTMLDialogElement>(null)
    const idDica = useId()
    const idTituloModal = useId()

    const rotulo = dispositivo === 'desktop' ? 'PC' : 'Celular'
    const Icone = dispositivo === 'desktop' ? FiMonitor : FiSmartphone
    const temImagemPropria = Boolean(arquivo || imagemAtual)
    const arquivoPreview = temImagemPropria ? arquivo : reserva?.arquivo
    const imagemPreview = temImagemPropria ? imagemAtual : reserva?.imagem
    const temImagem = Boolean(arquivoPreview || imagemPreview)

    useEffect(() => {
        const imagem = previewRef.current
        if (!arquivoPreview || !imagem) return

        const url = URL.createObjectURL(arquivoPreview)
        imagem.src = url
        return () => URL.revokeObjectURL(url)
    }, [arquivoPreview])

    useEffect(() => {
        const modal = modalRef.current
        if (!origem || !modal) return

        modal.showModal()
        return () => {
            modal.close()
            URL.revokeObjectURL(origem)
        }
    }, [origem])

    const fecharRecorte = () => {
        setOrigem(null)
        setZoom(1)
        setPosicao({ x: 0, y: 0 })
        setArea(null)
        setErroRecorte(null)
    }

    const handleEscolha = (event: ChangeEvent<HTMLInputElement>) => {
        const escolhido = event.target.files?.[0]
        event.target.value = '' // permite escolher o mesmo arquivo de novo
        onErro(null)
        if (!escolhido) return

        if (!TIPOS_ACEITOS.includes(escolhido.type)) {
            onErro('Envie uma imagem JPG ou PNG.')
            return
        }
        if (escolhido.size > TAMANHO_MAXIMO) {
            onErro('A imagem deve ter no máximo 5 MB.')
            return
        }
        setOrigem(URL.createObjectURL(escolhido))
    }

    const confirmar = async () => {
        if (!origem || !area || recortando) return
        setRecortando(true)
        setErroRecorte(null)
        try {
            onChange(await recortar(origem, area, formato))
            fecharRecorte()
        } catch (e) {
            setErroRecorte(e instanceof Error ? e.message : 'Erro ao recortar a imagem.')
        } finally {
            setRecortando(false)
        }
    }

    const imagemPequena = area !== null && area.width < formato.largura * 0.7

    return (
        <div className="imagem-banner">
            <div className="imagem-banner__cabecalho">
                <span className="imagem-banner__icone"><Icone aria-hidden="true" /></span>
                <h3>{rotulo}</h3>
                <span className="imagem-banner__opcional">{obrigatorio ? 'Obrigatória' : 'Opcional'}</span>
            </div>
            <p className="imagem-banner__dica" id={idDica}>
                {formato.largura} × {formato.altura} px · JPG ou PNG · até 5 MB
            </p>

            <input
                ref={inputRef}
                type="file"
                accept="image/png, image/jpeg"
                onChange={handleEscolha}
                hidden
            />

            <div className="imagem-banner__palco">
                {temImagem ? (
                    <img
                        className="imagem-banner__preview"
                        ref={previewRef}
                        src={arquivoPreview ? undefined : imagemPreview ?? undefined}
                        alt={`Prévia da imagem para ${rotulo.toLowerCase()}`}
                    />
                ) : (
                    <div className="imagem-banner__vazio">
                        <FiImage aria-hidden="true" />
                        <span>Nenhuma imagem selecionada</span>
                    </div>
                )}
            </div>

            {temImagem && !temImagemPropria && (
                <p className="imagem-banner__dica">Usando a imagem de PC.</p>
            )}

            <div className="imagem-banner__controles">
                <button type="button" className="btn imagem-banner__escolher" aria-describedby={idDica} onClick={() => inputRef.current?.click()}>
                    <FiUpload aria-hidden="true" />
                    {temImagemPropria ? 'Trocar imagem' : 'Selecionar imagem'}
                </button>
                {temImagemPropria && !obrigatorio && (
                    <button
                        type="button"
                        className="imagem-banner__remover"
                        onClick={() => (arquivo ? onChange(null) : onRemoverAtual?.())}
                    >
                        {arquivo ? 'Descartar recorte' : 'Usar imagem de PC'}
                    </button>
                )}
            </div>

            {origem && (
                <dialog
                    ref={modalRef}
                    className="imagem-banner__modal"
                    aria-labelledby={idTituloModal}
                    onCancel={(event) => {
                        event.preventDefault()
                        if (!recortando) fecharRecorte()
                    }}
                >
                    <div className="imagem-banner__caixa">
                        <h2 id={idTituloModal}>Ajuste o enquadramento</h2>
                        <div className="imagem-banner__area">
                            <Cropper
                                image={origem}
                                crop={posicao}
                                zoom={zoom}
                                aspect={formato.largura / formato.altura}
                                onCropChange={setPosicao}
                                onZoomChange={setZoom}
                                onCropComplete={(_, pixels) => setArea(pixels)}
                            />
                        </div>

                        <label className="imagem-banner__zoom">
                            Zoom
                            <input
                                type="range"
                                min={1}
                                max={3}
                                step={0.05}
                                value={zoom}
                                disabled={recortando}
                                onChange={(e) => setZoom(Number(e.target.value))}
                            />
                        </label>

                        {imagemPequena && (
                            <p className="imagem-banner__aviso">
                                Esta área é pequena para o tamanho do banner e pode ficar borrada.
                                Tente diminuir o zoom ou usar uma imagem maior.
                            </p>
                        )}

                        {erroRecorte && <p className="imagem-banner__erro" role="alert">{erroRecorte}</p>}

                        <div className="imagem-banner__botoes">
                            <button type="button" className="btn imagem-banner__escolher" onClick={fecharRecorte} disabled={recortando}>
                                Cancelar
                            </button>
                            <button type="button" className="btn btn--primary" onClick={confirmar} disabled={!area || recortando}>
                                {recortando ? 'Recortando...' : 'Usar este recorte'}
                            </button>
                        </div>
                    </div>
                </dialog>
            )}
        </div>
    )
}
