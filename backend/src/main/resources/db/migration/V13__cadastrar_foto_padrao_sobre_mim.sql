-- Preenche a posição da foto de Sobre Mim quando ela ainda não está configurada.
INSERT INTO banner (titulo, imagem, imagem_mobile, posicao, ativo)
SELECT 'Foto da página Sobre Mim', '/banners/padrao/sobre-mim-desktop.png',
       '/banners/padrao/sobre-mim-mobile.png', arte.posicao, TRUE
FROM (SELECT 'SOBRE_MIM_FOTO' AS posicao) AS arte
LEFT JOIN banner AS existente ON existente.posicao = arte.posicao
WHERE existente.id IS NULL;
