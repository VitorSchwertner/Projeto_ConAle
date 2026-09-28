import './ClientArea.css'

export default function ClientArea() {
  return (
    <section className="client-area" id="area-cliente">
      <div className="container client-area__grid">
        <div className="client-area__card">
          <h3>
            Área
            <br />
            do Cliente
          </h3>
          <button className="btn btn--primary">Acessar</button>
        </div>

        <div className="client-area__banner" role="img" aria-label="Consultora ConAle">
          <span>Foto aqui</span>
        </div>
      </div>
    </section>
  )
}