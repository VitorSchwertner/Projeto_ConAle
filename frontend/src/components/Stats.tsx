import { useEffect, useState } from 'react'
import { listarIndicadores } from '../api/content'
import { useCountUp } from '../hooks/useCountUp'
import type { IndicadorInstitucional } from '../types/content'
import './Stats.css'

// Valores de reserva, usados se o backend ainda nao responder
const PADRAO: IndicadorInstitucional = { id: 0, anosAtuacao: 10, empresasAbertas: 15 }

export default function Stats() {
    const [indicador, setIndicador] = useState<IndicadorInstitucional>(PADRAO)

    useEffect(() => {
        listarIndicadores()
            .then((lista) => {
                if (lista[0]) setIndicador(lista[0])
            })
            .catch(() => {
                // mantém os valores de reserva
            })
    }, [])

    const anos = useCountUp(indicador.anosAtuacao)
    const empresas = useCountUp(indicador.empresasAbertas)

    return (
        <section className="stats">
            <div className="container stats__inner">
                <p className="stats__intro">
                    Nós ajudamos seu negócio a <strong>crescer</strong>
                </p>
                <div className="stats__row">
                    <div className="stats__item">
            <span className="stats__value" ref={anos.ref}>
              +{anos.value}
            </span>
                        <span className="stats__label">anos de atuação</span>
                    </div>

                    <span className="stats__divider" aria-hidden="true" />

                    <div className="stats__item">
            <span className="stats__value" ref={empresas.ref}>
              +{empresas.value}
            </span>
                        <span className="stats__label">empresas abertas</span>
                    </div>

                    <span className="stats__divider" aria-hidden="true" />

                    <div className="stats__item stats__item--text">
                        Atendimento em todo Brasil!
                    </div>
                </div>
            </div>
        </section>
    )
}