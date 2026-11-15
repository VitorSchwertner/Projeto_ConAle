-- Preenche os campos vazios do rodapé, preservando os valores já cadastrados.
UPDATE rodape
SET telefone = COALESCE(NULLIF(telefone, ''), '+55 (51) 99661-9470'),
    instagram = COALESCE(NULLIF(instagram, ''), '@conale.consultoria'),
    email = COALESCE(NULLIF(email, ''), 'atendimento.conale@gmail.com'),
    dias_semana = COALESCE(NULLIF(dias_semana, ''), 'Segunda à Sexta-feira'),
    horas_trabalhadas = COALESCE(NULLIF(horas_trabalhadas, ''), '08:00h - 12:00h | 13:00h - 17:15h'),
    endereco = COALESCE(NULLIF(endereco, ''), 'R. Guanabara, 353 - Sala 1, São Cristóvão, Lajeado - RS, 95913-122')
WHERE id = 1;
