# Autenticação administrativa no Postman

A API usa sessão no servidor e cookie `JSESSIONID`.
As únicas contas que podem entrar são as que possuem `administrador=true`.
A migration V2 preserva os dados e marca contas anteriores como não administrativas.
Não edite a V1 nem apague o volume do banco para aplicar esta mudança.

## Primeiro administrador

Na cópia local de `.env`, configure `ADMIN_INITIAL_NAME`, `ADMIN_INITIAL_EMAIL` e
`ADMIN_INITIAL_PASSWORD`. Use e-mail ainda não cadastrado e senha própria com pelo
menos 8 caracteres e no máximo 72 bytes em UTF-8. Nunca versione `.env`.

Na raiz desta branch, execute `docker compose up -d --build backend`.
O primeiro início aplica a V2 e cria o administrador apenas se ainda não existir um.
Configuração inválida ou e-mail já utilizado causa falha sem sobrescrever contas.
Após conferir o login, apague o valor de `ADMIN_INITIAL_PASSWORD` do `.env` e execute
`docker compose up -d --force-recreate backend` para removê-lo do ambiente do container.
Isso não altera a senha salva no banco. A inicialização nunca redefine uma senha existente.

## Configuração do Postman

Importe `docs/postman/ConAle-Auth.postman_collection.json`.
Nas variáveis da coleção, preencha `adminEmail` e `adminPassword` apenas localmente.
Não compartilhe/exporte a coleção preenchida com credenciais reais.
`baseUrl` usa `http://localhost:18081` para o ambiente temporário desta demonstração.
No ambiente habitual, use `http://localhost:8080` ou a porta definida no `.env`.
Use sempre o mesmo host: não misture `localhost` com `127.0.0.1`.
Mantenha o gerenciamento de cookies habilitado. Não é necessário Bearer Token.

## Sequência

1. `GET /api/auth/csrf`: retorna `token` e `headerName` e inicia uma sessão anônima.
   O script da coleção salva o token em `csrfToken`. Manualmente, copie o token.
2. `POST /api/auth/login`: Body → raw → JSON, com `email` e `senha`.
   Envie o cabeçalho `X-CSRF-TOKEN: {{csrfToken}}`. Esperado: 200, sem senha/hash.
   O Postman guarda o novo cookie automaticamente.
3. Faça novamente `GET /api/auth/csrf`. O login troca o ID da sessão e invalida o
   token anterior. Repita esta etapa após cada login.
4. `GET /api/auth/me`: deve retornar 200 com id, nome e e-mail do administrador.
5. Teste notícias e usuários. GET não precisa de CSRF. POST, PUT e DELETE precisam
   do cookie da sessão e do cabeçalho `X-CSRF-TOKEN` atualizado.
6. `POST /api/auth/logout` com CSRF: retorna 204, encerra a sessão e apaga o cookie.
7. `GET /api/auth/me`: deve retornar 401. Para entrar novamente, recomece pelo CSRF.

## Permissões e resultados esperados

| Operação | Sem login | Administrador com CSRF válido |
|---|---|---|
| Consultar notícias | 200 | 200 |
| Consultar usuários ou /auth/me | 401 | 200 |
| Criar notícias/usuários | 401 com CSRF válido | 201 |
| Editar notícias/usuários | 401 com CSRF válido | 200 |
| Excluir notícia/usuário de teste | 401 com CSRF válido | 204 |

Sem CSRF válido, operações de escrita retornam 403 mesmo antes da verificação do login.
Senha incorreta, conta inexistente e conta não administrativa retornam o mesmo 401.
E-mail duplicado retorna 409. Não é permitido excluir o último administrador (409).
Cadastros feitos por um administrador criam novas contas administrativas.
Não há cadastro público. Alterações de senha/e-mail, revogação e exclusão invalidam
as sessões antigas no próximo acesso. Trocar a própria senha exige novo login.
A senha é opcional no PUT e, quando omitida, permanece igual.

## Sessão e implantação

Sessões expiram após 30 minutos de inatividade e são perdidas ao reiniciar o backend.
Cookie HttpOnly e SameSite=Strict. A configuração local permite HTTP apenas para
desenvolvimento. Em produção, use HTTPS e `SESSION_COOKIE_SECURE=true`.
Esta etapa não configura frontend/CORS, recuperação de senha, MFA ou limitação de
tentativas de login. Antes de publicar na internet, preparar esses controles,
principalmente proteção contra tentativas repetidas, HTTPS e recuperação de acesso.

## Testes automatizados

`docker compose -p conale-auth-tests -f compose.test.yaml up --build --abort-on-container-exit --exit-code-from backend-test`

Esse comando usa um MySQL temporário separado e não lê os dados do banco habitual.
Testes cobrem autenticação, autorização, CSRF, troca de ID de sessão, logout,
revogação de acesso, bootstrap administrativo e os testes anteriores de notícias/banco.

Referências: [sessão no Spring Security](https://docs.spring.io/spring-security/reference/6.5/servlet/authentication/session-management.html)
e [proteção CSRF](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html).
