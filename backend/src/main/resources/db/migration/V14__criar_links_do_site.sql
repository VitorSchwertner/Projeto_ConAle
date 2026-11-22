CREATE TABLE link_site (
    codigo VARCHAR(50) NOT NULL PRIMARY KEY,
    url VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO link_site (codigo) VALUES
    ('acao-diagnostico'),
    ('acao-reuniao'),
    ('acao-abrir-consultorio'),
    ('acao-pagar-menos-imposto'),
    ('acao-evitar-burocracia'),
    ('destaque-inicio-de-carreira'),
    ('destaque-gestao-financeira'),
    ('destaque-assessoria-estrategica'),
    ('servico-contabilidade'),
    ('servico-bpo'),
    ('servico-mentoria'),
    ('servico-solucoes');
