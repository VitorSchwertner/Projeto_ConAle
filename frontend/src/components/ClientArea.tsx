import { useBanners } from '../hooks/useBanners'
import './ClientArea.css'

export default function ClientArea() {
  const { areaClienteBanner } = useBanners()

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

          {areaClienteBanner ? (
              <img
                  src={areaClienteBanner.imagem}
                  alt={areaClienteBanner.titulo}
                  className="client-area__banner-image"
              />
          ) : (
              <div className="client-area__banner">
                <svg
                    className="client-area__waves"
                    viewBox="0 0 600 300"
                    preserveAspectRatio="none"
                    aria-hidden="true"
                >
                  <path
                      d="M0,120 C100,60 180,180 280,110 C380,40 450,160 600,90"
                      fill="none"
                      stroke="rgba(64,178,135,0.5)"
                      strokeWidth="1.5"
                  />
                  <path
                      d="M0,160 C100,100 180,220 280,150 C380,80 450,200 600,130"
                      fill="none"
                      stroke="rgba(64,178,135,0.35)"
                      strokeWidth="1.5"
                  />
                  <path
                      d="M0,200 C100,140 180,260 280,190 C380,120 450,240 600,170"
                      fill="none"
                      stroke="rgba(64,178,135,0.2)"
                      strokeWidth="1.5"
                  />
                </svg>
                <div className="client-area__stripe" aria-hidden="true" />
              </div>
          )}
        </div>
      </section>
  )
}