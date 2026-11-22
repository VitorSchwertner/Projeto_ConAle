INSERT INTO indicador_institucional (anos_atuacao, empresas_abertas)
SELECT 0, 0
WHERE NOT EXISTS (SELECT 1 FROM indicador_institucional);
