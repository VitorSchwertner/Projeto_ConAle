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
      .catch(() => setErro('Não foi possível carregar as notícias.'))
      .finally(() => setCarregando(false))
  }

  useEffect(() => {
    carregar()
  }, [])

  const handleExcluir = async (id: number) => {
    if (!confirm('Excluir esta notícia?')) return
    try {
      await excluirNoticia(id)
      carregar()
    } catch {
      alert('Não foi possível excluir a notícia.')
    }
  }

  if (carregando) return <p>Carregando...</p>
  if (erro) return <p>{erro}</p>

  return (
    <div className="noticia-list">
      <div className="noticia-list__header">
        <h1>Notícias</h1>
        <Link to="/admin/noticias/nova" className="btn btn--primary">
          Nova notícia
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
              <td className="noticia-list__actions">
                <Link to={`/admin/noticias/${noticia.id}`}>Editar</Link>
                <button onClick={() => handleExcluir(noticia.id)}>Excluir</button>
              </td>
            </tr>
          ))}
          {noticias.length === 0 && (
            <tr>
              <td colSpan={3}>Nenhuma notícia cadastrada.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}