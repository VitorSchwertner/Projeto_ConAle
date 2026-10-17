export interface Usuario {
    id: number
    nome: string
    email: string
    administrador: boolean
}

export interface Banner {
    id: number
    titulo: string
    imagem: string
    link: string | null
    ativo: boolean
}

export interface Noticia {
    id: number
    titulo: string
    subTitulo: string | null
}

export interface Rodape {
    id: number
    telefone: string | null
    instagram: string | null
    email: string | null
    diasSemana: string | null
    horasTrabalhadas: string | null
    endereco: string | null
}

export interface IndicadorInstitucional {
    id: number
    anosAtuacao: number
    empresasAbertas: number
}

export interface MensagensClientes {
    id: number
    titulo: string
    areaAtuacao: string
    nota: number
    mensagem: string
}