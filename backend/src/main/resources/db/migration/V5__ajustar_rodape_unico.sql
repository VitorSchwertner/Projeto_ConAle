-- Mantem a chave fixa e a restricao ck_rodape_unico criadas na V1.
-- Renomeia local sem apagar enderecos existentes; link legado e preservado.
-- NULL continua permitido para o cadastro inicial ainda nao preenchido.
-- A API exige os campos obrigatorios quando o administrador atualiza os dados.
SET @conale_sql_mode_anterior = @@SESSION.sql_mode;
SET SESSION sql_mode = CONCAT_WS(',', @@SESSION.sql_mode, 'STRICT_ALL_TABLES');

ALTER TABLE rodape
    MODIFY telefone VARCHAR(20) NULL,
    MODIFY instagram VARCHAR(100) NULL,
    MODIFY dias_semana VARCHAR(100) NULL,
    MODIFY horas_trabalhadas VARCHAR(100) NULL,
    CHANGE COLUMN local endereco VARCHAR(255) NULL;

SET SESSION sql_mode = @conale_sql_mode_anterior;
