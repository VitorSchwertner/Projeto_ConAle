-- Preserva os usuários existentes sem liberar acesso administrativo para eles.
ALTER TABLE usuario ADD COLUMN administrador BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE usuario ADD CONSTRAINT ck_usuario_administrador CHECK (administrador IN (0, 1));
