import { useEffect, useState, type FormEvent } from 'react'
import { buscarRodape, atualizarRodape } from '../../api/rodape'
import './RodapePage.css'

export default function RodapePage() {
    const [telefone, setTelefone] = useState('')
    const [instagram, setInstagram] = useState('')
    const [email, setEmail] = useState('')
    const [diasSemana, setDiasSemana] = useState('')
    const [horasTrabalhadas, setHorasTrabalhadas] = useState('')
    const [endereco, setEndereco] = useState('')

    const [carregando, setCarregando] = useState(true)
    const [salvando, setSalvando] = useState(false)
    const [erro, setErro] = useState<string | null>(null)
    const [sucesso, setSucesso] = useState(false)

    useEffect(() => {
        buscarRodape()
            .then((rodape) => {
                setTelefone(rodape.telefone ?? '')
                setInstagram(rodape.instagram ?? '')
                setEmail(rodape.email ?? '')
                setDiasSemana(rodape.diasSemana ?? '')
                setHorasTrabalhadas(rodape.horasTrabalhadas ?? '')
                setEndereco(rodape.endereco ?? '')
            })
            .catch(() => setErro('Não foi possível carregar os dados do rodapé.'))
            .finally(() => setCarregando(false))
    }, [])

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault()
        setErro(null)
        setSucesso(false)
        setSalvando(true)
        try {
            await atualizarRodape({
                telefone,
                instagram: instagram || null,
                email,
                diasSemana,
                horasTrabalhadas,
                endereco,
            })
            setSucesso(true)
        } catch {
            setErro('Não foi possível salvar as informações do rodapé.')
        } finally {
            setSalvando(false)
        }
    }

    if (carregando) return <p>Carregando...</p>

    return (
        <div className="rodape-page">
            <h1>Informações do Rodapé</h1>

            <form onSubmit={handleSubmit}>
                <label>
                    Telefone
                    <input value={telefone} onChange={(e) => setTelefone(e.target.value)} required />
                </label>

                <label>
                    Instagram (opcional)
                    <input value={instagram} onChange={(e) => setInstagram(e.target.value)} />
                </label>

                <label>
                    E-mail
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </label>

                <label>
                    Dias de funcionamento
                    <input
                        value={diasSemana}
                        onChange={(e) => setDiasSemana(e.target.value)}
                        placeholder="Ex: Segunda à Sexta-feira"
                        required
                    />
                </label>

                <label>
                    Horário de funcionamento
                    <input
                        value={horasTrabalhadas}
                        onChange={(e) => setHorasTrabalhadas(e.target.value)}
                        placeholder="Ex: 08:00h - 12:00h | 13:00h - 17:15h"
                        required
                    />
                </label>

                <label>
                    Endereço
                    <textarea
                        value={endereco}
                        onChange={(e) => setEndereco(e.target.value)}
                        rows={2}
                        required
                    />
                </label>

                {erro && <p className="rodape-page__error">{erro}</p>}
                {sucesso && <p className="rodape-page__success">Informações salvas com sucesso!</p>}

                <div className="rodape-page__actions">
                    <button type="submit" className="btn btn--primary" disabled={salvando}>
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </div>
    )
}