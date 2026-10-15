-- Novos conteudos administraveis. V1 e V2 permanecem inalteradas.
CREATE TABLE indicador_institucional (
    id INT AUTO_INCREMENT PRIMARY KEY,
    anos_atuacao INT NOT NULL,
    empresas_abertas INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE mensagens_clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    area_atuacao VARCHAR(150) NOT NULL,
    nota DOUBLE NOT NULL,
    mensagem VARCHAR(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
