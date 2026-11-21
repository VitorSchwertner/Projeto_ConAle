-- Transfere para o CMS os cards que antes eram fixos na Home.
-- A API exibe os IDs em ordem decrescente, por isso a inserção é feita na ordem inversa.
INSERT INTO noticia (titulo, sub_titulo)
SELECT card.titulo, card.sub_titulo
FROM (
    SELECT 1 AS ordem,
           'Como acessar a área do cliente?' AS titulo,
           'O acesso à área do cliente é feito por meio dos dados de login enviados para o seu e-mail. Insira o usuário e a senha e, no primeiro uso, altere a senha dentro da plataforma.' AS sub_titulo
    UNION ALL
    SELECT 2,
           'Como funciona o serviço de BPO Financeiro?',
           'O BPO Financeiro é a terceirização da gestão financeira do seu negócio. Na prática, nossa equipe assume rotinas como contas a pagar e a receber, conciliação bancária e organização dos fluxos financeiros.'
    UNION ALL
    SELECT 3,
           'O que preciso para abrir um CNPJ?',
           'Para abrir um CNPJ, reúna seus documentos pessoais, a definição da sua atividade profissional e o endereço onde a empresa será registrada. Na abertura, é importante avaliar o enquadramento tributário mais adequado para o seu caso. Nossa equipe orienta cada etapa, cuidando da parte burocrática e garantindo que tudo seja feito de forma correta e estratégica.'
    UNION ALL
    SELECT 4,
           'Como sei se estou pagando impostos corretamente?',
           'No diagnóstico inicial, avaliamos sua estrutura e identificamos possíveis erros, riscos ou oportunidades de melhoria na sua tributação.'
    UNION ALL
    SELECT 5,
           'Posso trocar de contador facilmente?',
           'Sim. A troca de contador é um processo simples quando feito com organização e acompanhamento profissional. Podemos cuidar da transição junto ao seu contador atual, garantindo que suas obrigações contábeis continuem em dia, sem interrupções ou complicações.'
    UNION ALL
    SELECT 6,
           'Como funciona o atendimento da ConAle?',
           'Nosso atendimento é 100% digital. Você fala com nossa equipe por WhatsApp, e-mail ou reunião online, com suporte próximo e organizado para facilitar sua rotina.'
) AS card
LEFT JOIN noticia AS existente ON existente.titulo = card.titulo
WHERE existente.id IS NULL
ORDER BY card.ordem DESC;
