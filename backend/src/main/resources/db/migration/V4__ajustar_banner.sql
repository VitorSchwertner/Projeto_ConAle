-- Modo estrito impede cortar textos existentes que excedam os novos limites.
-- Se houver erro, revisar os dados antes de tentar aplicar novamente.
SET @conale_sql_mode_anterior = @@SESSION.sql_mode;
SET SESSION sql_mode = CONCAT_WS(',', @@SESSION.sql_mode, 'STRICT_ALL_TABLES');

ALTER TABLE banner
    MODIFY titulo VARCHAR(150) NOT NULL,
    MODIFY imagem VARCHAR(500) NOT NULL,
    MODIFY link VARCHAR(500) NULL,
    ALTER COLUMN ativo SET DEFAULT TRUE;

-- O novo padrao nao altera banners ja cadastrados como inativos.
SET SESSION sql_mode = @conale_sql_mode_anterior;
