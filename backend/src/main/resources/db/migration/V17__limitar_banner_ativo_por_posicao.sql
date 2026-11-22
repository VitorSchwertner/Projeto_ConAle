-- Preserva como ativo o banner de maior ID, que já era o escolhido pelo site.
UPDATE banner AS antigo
JOIN banner AS mais_recente ON mais_recente.posicao = antigo.posicao
    AND mais_recente.ativo = TRUE
    AND mais_recente.id > antigo.id
SET antigo.ativo = FALSE
WHERE antigo.ativo = TRUE;

-- O índice permite várias linhas inativas ou sem posição.
ALTER TABLE banner
    ADD COLUMN posicao_ativa VARCHAR(30)
        GENERATED ALWAYS AS (CASE WHEN ativo = TRUE THEN posicao ELSE NULL END) VIRTUAL,
    ADD CONSTRAINT uk_banner_posicao_ativa UNIQUE (posicao_ativa);
