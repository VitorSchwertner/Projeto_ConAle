-- O bootstrap vincula a conta pelo e-mail configurado, inclusive em bancos existentes.
ALTER TABLE usuario
    ADD COLUMN administrador_original BOOLEAN NOT NULL DEFAULT FALSE,
    ADD CONSTRAINT ck_usuario_administrador_original CHECK (
        administrador_original = 0 OR (administrador_original = 1 AND administrador = 1)
    );

-- O MySQL permite vários NULL no índice, mas apenas um administrador original.
CREATE UNIQUE INDEX uk_usuario_administrador_original
    ON usuario ((NULLIF(administrador_original, 0)));
