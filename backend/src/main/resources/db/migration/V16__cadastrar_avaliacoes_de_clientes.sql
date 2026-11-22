-- A API lista por ID decrescente, por isso a inserção segue a ordem inversa.
INSERT INTO mensagens_clientes (titulo, area_atuacao, nota, mensagem)
SELECT avaliacao.titulo, avaliacao.area_atuacao, 5, avaliacao.mensagem
FROM (
    SELECT 1 AS ordem, 'Ana' AS titulo, 'Fisioterapeuta' AS area_atuacao,
           'Sempre retirando as dúvidas com carinho e atenção, eu recomendo hoje a ConAle por toda a parceria que temos e pela transparência no trabalho.' AS mensagem
    UNION ALL
    SELECT 2, 'Vanessa e Thais', 'Fisioterapeutas',
           'Queremos agradecer o trabalho e dedicação da empresa ConAle conosco, sempre nos respondem de forma rápida e esclarecem todas as dúvidas que surgem.'
    UNION ALL
    SELECT 3, 'Janaína', 'Psicóloga',
           'A minha experiência com os serviços oferecidos pela ConAle é positiva. A Alexandra está sempre disposta a resolver todas as dúvidas, trazendo as informações com clareza nos proporcionando segurança.'
    UNION ALL
    SELECT 4, 'Flávia', 'Terapeuta',
           'A ConAle conta com profissionais dedicados, que fazem muito mais do que números: oferecem segurança, clareza e soluções inteligentes para cada cliente. Contar com eles é ter a tranquilidade de que tudo está em dia, e com excelência. Se você também está procurando um escritório de contabilidade de confiança, com atendimento humanizado, competência técnica e total compromisso com o sucesso do seu negócio, eu o recomendo de olhos fechados.'
    UNION ALL
    SELECT 5, 'Gabriela', 'Médica',
           'Trabalhar com a ConAle Contabilidade é ter a certeza de que estamos em boa mãos! Profissionais capacitados, humanos e competentes nos trazem a tranquilidade e a organização necessária que a contabilidade exige! São parceiros que estão dispostos a ajudar e entender qual melhor cenário para a empresa! Indico de olhos fechados! Além de parceiros comerciais, amigos!'
) AS avaliacao
LEFT JOIN mensagens_clientes AS existente
    ON existente.titulo = avaliacao.titulo AND existente.area_atuacao = avaliacao.area_atuacao
WHERE existente.id IS NULL
ORDER BY avaliacao.ordem DESC;
