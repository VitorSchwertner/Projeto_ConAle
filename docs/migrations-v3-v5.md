# Migrations V3 a V5

V1 e V2 nao foram alteradas. O Flyway aplica somente as novas versoes ao iniciar o backend.

- **V3:** cria `indicador_institucional` e `mensagens_clientes`.
- **V4:** ajusta os limites do banner e o padrao SQL de `ativo` para `TRUE`. Nao ativa banners existentes. Valores enviados explicitamente pela API prevalecem sobre o padrao SQL.
- **V5:** renomeia `rodape.local` para `endereco` e ajusta os limites. Preserva o registro `id = 1`, a restricao de unicidade e a coluna legada `link`.

O rodape nao tem AUTO_INCREMENT. O controller oferece apenas `GET /api/rodape` e `PUT /api/rodape`, sem ID na URL; GET retorna um objeto, nao uma lista. POST e DELETE foram removidos.

O cadastro inicial pode continuar com campos NULL. A atualizacao pela API exige os campos obrigatorios do DTO. Nao sao inventados dados para preencher registros antigos.

## Antes de aplicar no banco de desenvolvimento

Faca backup. Nao exclua volumes do Docker, nao altere V1/V2 e nao execute os CREATE TABLE manualmente. As migrations devem ser aplicadas pelo Flyway.

Confira se existem valores que ultrapassam os novos limites:

```sql
SELECT id FROM banner
WHERE CHAR_LENGTH(titulo) > 150 OR CHAR_LENGTH(imagem) > 500 OR CHAR_LENGTH(link) > 500;

-- Executar antes da V5: o nome antigo ainda e local.
SELECT id FROM rodape
WHERE CHAR_LENGTH(telefone) > 20 OR CHAR_LENGTH(instagram) > 100
   OR CHAR_LENGTH(dias_semana) > 100 OR CHAR_LENGTH(horas_trabalhadas) > 100
   OR CHAR_LENGTH(local) > 255;
```

Se houver resultados, revise esses dados antes de iniciar a atualizacao. O modo SQL estrito impede truncamento automatico. Em MySQL, DDL nao e transacional: se uma migration falhar, inspecione o schema e o historico antes de qualquer reparo do Flyway; nao use repair sem avaliar a causa.

## Testes isolados

Na raiz do projeto, com Docker iniciado:

```powershell
docker compose -p conale-migrations-test -f compose.test.yaml up --build --abort-on-container-exit --exit-code-from backend-test
docker compose -p conale-migrations-test -f compose.test.yaml down
```

Esse ambiente utiliza banco temporario separado, sem dados de desenvolvimento.

## Permissoes ainda pendentes

As regras de seguranca das novas rotas nao foram alteradas nesta etapa. O rodape e os demais novos conteudos continuam bloqueados pela configuracao atual. Antes de testar essas rotas no Postman, e necessario definir e implementar as permissoes de leitura e escrita. Os testes de servico/banco nao substituem essa validacao HTTP.
