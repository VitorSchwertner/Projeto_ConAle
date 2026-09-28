-- Estrutura aprovada para as quatro classes iniciais do ConAle.
-- Mudancas posteriores devem usar novas migrations (V2, V3, etc).

CREATE TABLE usuario (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(254) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uk_usuario_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE banner (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    imagem VARCHAR(2048) NOT NULL,
    link VARCHAR(2048) NULL,
    ativo BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_banner PRIMARY KEY (id),
    CONSTRAINT ck_banner_ativo CHECK (ativo IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE noticia (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    sub_titulo VARCHAR(500) NULL,
    CONSTRAINT pk_noticia PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE rodape (
    id INT NOT NULL DEFAULT 1,
    telefone VARCHAR(30) NULL,
    instagram VARCHAR(2048) NULL,
    email VARCHAR(254) NULL,
    link VARCHAR(2048) NULL,
    dias_semana VARCHAR(150) NULL,
    horas_trabalhadas VARCHAR(150) NULL,
    local VARCHAR(500) NULL,
    CONSTRAINT pk_rodape PRIMARY KEY (id),
    CONSTRAINT ck_rodape_unico CHECK (id = 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Configuracao unica, inicialmente sem informacoes de contato.
INSERT INTO rodape (id) VALUES (1);