import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listarNoticias, excluirNoticia } from '../../api/noticias'
import type { Noticia } from '../../types'
import './NoticiaListPage.css'

export default function NoticiaListPage() {
  const [noticias, setNoticias] = useState<Noticia[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState<string | null>(null)

  const carregar = () => {
    setCarregando(true)
    listarNoticias()
      .then(setNoticias)
      .catch(() => setErro('Não foi possível carregar os cards informativos.'))
      .finally(() => setCarregando(false))
  }

  useEffect(() => {
    let ativo = true
    listarNoticias()
      .then((dados) => {
        if (ativo) setNoticias(dados)
      })
      .catch(() => {
        if (ativo) setErro('Não foi possível carregar os cards informativos.')
      })
      .finally(() => {
        if (ativo) setCarregando(false)
      })

    return () => {
      ativo = false
    }
  }, [])

  const handleExcluir = async (id: number) => {
    if (!confirm('Excluir este card?')) return
    try {
      await excluirNoticia(id)
      carregar()
    } catch {
      alert('Não foi possível excluir o card.')
    }
  }

  if (carregando) return <p>Carregando...</p>
  if (erro) return <p>{erro}</p>

  return (
    <div className="noticia-list">
      <div className="noticia-list__header">
        <h1>Cards informativos</h1>
        <Link to="/admin/noticias/nova" className="btn btn--primary">
          Novo card
        </Link>
      </div>

      <table className="noticia-list__table">
        <thead>
          <tr>
            <th>Título</th>
            <th>Subtítulo</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {noticias.map((noticia) => (
            <tr key={noticia.id}>
              <td>{noticia.titulo}</td>
              <td>{noticia.subTitulo ?? '—'}</td>
              <td>
                <div className="noticia-list__actions">
                  <Link to={`/admin/noticias/${noticia.id}`}>Editar</Link>
                  <button onClick={() => handleExcluir(noticia.id)}>Excluir</button>
                </div>
              </td>
            </tr>
          ))}
          {noticias.length === 0 && (
            <tr>
              <td colSpan={3}>Nenhum card cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}
