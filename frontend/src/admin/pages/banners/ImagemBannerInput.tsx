import { useEffect, useState, type ChangeEvent } from 'react'
import Cropper, { type Area } from 'react-easy-crop'
import type { Formato } from './bannerFormatos'
import './ImagemBannerInput.css'

const TIPOS_ACEITOS = ['image/jpeg', 'image/png']
const TAMANHO_MAXIMO = 5 * 1024 * 1024

type Props = {
    rotulo: string
    formato: Formato
    imagemAtual: string | null // URL completa da imagem já salva
    arquivo: File | null // imagem nova já recortada
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
    rotulo, formato, imagemAtual, arquivo, obrigatorio, onChange, onRemoverAtual, onErro,
}: Props) {
    const [origem, setOrigem] = useState<string | null>(null) // imagem original no recorte
    const [posicao, setPosicao] = useState({ x: 0, y: 0 })
    const [zoom, setZoom] = useState(1)
    const [area, setArea] = useState<Area | null>(null)
    const [preview, setPreview] = useState<string | null>(null)

    useEffect(() => {
        if (!arquivo) {
            setPreview(null)
            return
        }
        const url = URL.createObjectURL(arquivo)
        setPreview(url)
        return () => URL.revokeObjectURL(url)
    }, [arquivo])

    useEffect(() => {
        return () => {
            if (origem) URL.revokeObjectURL(origem)
        }
    }, [origem])

    const fecharRecorte = () => {
        setOrigem(null)
        setZoom(1)
        setPosicao({ x: 0, y: 0 })
        setArea(null)
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
        if (!origem || !area) return
        try {
            onChange(await recortar(origem, area, formato))
            fecharRecorte()
        } catch (e) {
            onErro(e instanceof Error ? e.message : 'Erro ao recortar a imagem.')
        }
    }

    const imagemExibida = preview ?? imagemAtual
    const imagemPequena = area !== null && area.width < formato.largura * 0.7

    return (
        <div className="imagem-banner">
            <span className="imagem-banner__rotulo">{rotulo}</span>
            <span className="imagem-banner__dica">
                Tamanho ideal: {formato.largura}x{formato.altura} px (JPG ou PNG, até 5 MB)
            </span>

            <input
                type="file"
                accept="image/png, image/jpeg"
                onChange={handleEscolha}
                required={Boolean(obrigatorio) && !imagemExibida}
            />

            {imagemExibida && (
                <>
                    <img
                        className="imagem-banner__preview"
                        style={{ aspectRatio: `${formato.largura} / ${formato.altura}` }}
                        src={imagemExibida}
                        alt={`Pré-visualização: ${rotulo}`}
                    />
                    {!obrigatorio && (
                        <button
                            type="button"
                            className="imagem-banner__remover"
                            onClick={() => (arquivo ? onChange(null) : onRemoverAtual?.())}
                        >
                            Remover imagem
                        </button>
                    )}
                </>
            )}

            {origem && (
                <div className="imagem-banner__modal">
                    <div className="imagem-banner__caixa">
                        <h2>Ajuste o enquadramento</h2>
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
                                onChange={(e) => setZoom(Number(e.target.value))}
                            />
                        </label>

                        {imagemPequena && (
                            <p className="imagem-banner__aviso">
                                Esta área é pequena para o tamanho do banner e pode ficar borrada.
                                Tente diminuir o zoom ou usar uma imagem maior.
                            </p>
                        )}

                        <div className="imagem-banner__botoes">
                            <button type="button" className="btn" onClick={fecharRecorte}>
                                Cancelar
                            </button>
                            <button type="button" className="btn btn--primary" onClick={confirmar}>
                                Usar este recorte
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}