-- Cadastra as artes do ConAle nas posições ainda sem um banner configurado.
INSERT INTO banner (titulo, imagem, imagem_mobile, posicao, ativo)
SELECT arte.titulo, arte.imagem, arte.imagem_mobile, arte.posicao, TRUE
FROM (
    SELECT 'Topo da Home' AS titulo, '/banners/padrao/home-desktop.png' AS imagem,
           '/banners/padrao/home-mobile.png' AS imagem_mobile, 'HOME_TOPO' AS posicao
    UNION ALL
    SELECT 'Topo de Serviços e Dúvidas Frequentes', '/banners/padrao/servicos-desktop.png',
           '/banners/padrao/servicos-mobile.png', 'SERVICOS_TOPO'
    UNION ALL
    SELECT 'Sobre Nós', '/banners/padrao/sobre-nos-desktop.png',
           '/banners/padrao/sobre-nos-mobile.png', 'SOBRE_NOS'
    UNION ALL
    SELECT 'Área do Cliente', '/banners/padrao/area-do-cliente.png', NULL, 'AREA_CLIENTE'
) AS arte
LEFT JOIN banner AS existente ON existente.posicao = arte.posicao
WHERE existente.id IS NULL;
