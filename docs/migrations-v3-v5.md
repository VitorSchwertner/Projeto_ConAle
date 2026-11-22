# Registro histórico — Migrations V3 a V5

Este documento descreve a transição do banco entre as versões V3 e V5. A entrega final usa as migrations até a V17. O [README](../README.md) descreve a inicialização e o [PRD](prd/conale.md) mostra o esquema atual.

## Mudanças registradas

- **V3** criou as tabelas `indicador_institucional` e `mensagens_clientes`.
- **V4** ajustou os limites do banner e mudou o valor padrão de `ativo` para `TRUE`. O estado dos banners existentes foi preservado.
- **V5** renomeou `rodape.local` para `endereco`, ajustou limites e preservou o registro `id = 1` e a coluna legada `link`.

Na V5, o rodapé ainda era um registro único. Na versão final, a V9 introduziu os modelos de rodapé e a seleção do modelo ativo em `rodape_ativo`.

## Consultas de diagnóstico para bancos anteriores à V5

As consultas abaixo documentam as verificações de tamanho usadas antes dessa transição. A segunda usa o nome de coluna anterior à V5, `local`.

```sql
SELECT id FROM banner
WHERE CHAR_LENGTH(titulo) > 150 OR CHAR_LENGTH(imagem) > 500 OR CHAR_LENGTH(link) > 500;

SELECT id FROM rodape
WHERE CHAR_LENGTH(telefone) > 20 OR CHAR_LENGTH(instagram) > 100
   OR CHAR_LENGTH(dias_semana) > 100 OR CHAR_LENGTH(horas_trabalhadas) > 100
   OR CHAR_LENGTH(local) > 255;
```

As migrations ficam em [db/migration](../backend/src/main/resources/db/migration) e são aplicadas pelo Flyway. O comando para verificar as migrations em um MySQL temporário está no [README](../README.md#verificar-a-entrega).
