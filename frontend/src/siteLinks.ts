import { publicFetch } from './api/http'

export const ACOES_RAPIDAS = [
    { codigo: 'acao-diagnostico', titulo: 'Solicite seu diagnóstico gratuito' },
    { codigo: 'acao-reuniao', titulo: 'Agende uma reunião com a ConAle' },
    { codigo: 'acao-abrir-consultorio', titulo: 'Quero abrir um consultório' },
    { codigo: 'acao-pagar-menos-imposto', titulo: 'Quero pagar menos imposto' },
    { codigo: 'acao-evitar-burocracia', titulo: 'Quero evitar burocracia' },
] as const

export const DESTAQUES = [
    {
        codigo: 'destaque-inicio-de-carreira',
        titulo: 'Início de carreira',
        chamada: 'Saiba mais sobre início de carreira',
        imagem: '/destaques/inicio-de-carreira.png',
        descricao: 'O início da sua carreira merece decisões seguras. Conte com a ConAle!',
    },
    {
        codigo: 'destaque-gestao-financeira',
        titulo: 'Gestão financeira',
        chamada: 'Saiba como estruturar sua gestão financeira',
        imagem: '/destaques/gestao-financeira.png',
        descricao: 'Crescer na área da saúde exige mais do que atender mais pacientes. Tenha uma gestão financeira estruturada.',
    },
    {
        codigo: 'destaque-assessoria-estrategica',
        titulo: 'Assessoria estratégica',
        chamada: 'Conheça nossa assessoria estratégica',
        imagem: '/destaques/assessoria-estrategica.png',
        descricao: 'Quando a carreira evolui, a contabilidade também precisa evoluir. Saiba mais sobre a nossa assessoria estratégica.',
    },
] as const

export const SERVICOS = {
    contabilidade: { codigo: 'servico-contabilidade', titulo: 'Contabilidade' },
    bpo: { codigo: 'servico-bpo', titulo: 'Bpo Financeiro' },
    mentoria: { codigo: 'servico-mentoria', titulo: 'Mentoria Financeira' },
    solucoes: { codigo: 'servico-solucoes', titulo: 'Soluções Complementares' },
} as const

type CodigoLink =
    | (typeof ACOES_RAPIDAS)[number]['codigo']
    | (typeof DESTAQUES)[number]['codigo']
    | (typeof SERVICOS)[keyof typeof SERVICOS]['codigo']

export type LinksSite = Partial<Record<CodigoLink, string>>

export const ENDPOINT_LINKS_SITE = '/api/links-site'

export function buscarLinksSite() {
    return publicFetch<LinksSite>(ENDPOINT_LINKS_SITE)
}
