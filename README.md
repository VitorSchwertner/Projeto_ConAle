# Projeto ConAle

Projeto da disciplina de Laboratório de Programação para a Internet (2026B), desenvolvido para a ConAle, especializada em contabilidade para a área da saúde.

A solução reúne um site institucional e um sistema administrativo de gestão de conteúdo (CMS). O portal externo dos clientes não é substituído pelo projeto.

## Estado atual

- **Frontend:** páginas Home, Serviços, Sobre e Dúvidas Frequentes, com React e conteúdo definido nos componentes. Alguns botões ainda não têm ação e há imagens provisórias.
- **Backend:** CRUDs de notícias e usuários, autenticação administrativa por sessão e rota de saúde.
- **Notícias:** apenas `id`, `titulo` e `sub_titulo`. Não incluem imagens, corpo de artigo, comentários ou agendamento.
- **Banco:** tabelas `usuario`, `noticia`, `banner` e `rodape`, criadas e atualizadas pelo Flyway.
- **Ainda pendente:** integração do frontend com a API, telas de login e administração, exibição das notícias da API no site, upload e gestão dos demais conteúdos editáveis. Banners e rodapé possuem estrutura de persistência, mas não um CRUD completo acessível pela API.

O frontend pode ser executado sozinho para visualizar o site. Iniciar frontend e backend ao mesmo tempo não cria a integração entre eles. O Compose atual inicia apenas backend e MySQL.

## Stack

| Parte | Tecnologias |
| --- | --- |
| Frontend | React 19, TypeScript 6, Vite 8, React Router, React Icons e CSS |
| Backend | Java 24, Spring Boot 3.5.16, Spring Web, Validation e Spring Data JPA |
| Segurança | Spring Security, BCrypt, sessão por cookie e proteção CSRF |
| Banco e migrations | MySQL 8.4.11 e Flyway |
| Build e ambiente | Maven 3.9.9 no Dockerfile; Docker Compose |
| Verificação | Testes Java/Spring, MySQL isolado para testes, Postman e ESLint |

As dependências do frontend estão em [package.json](frontend/package.json) e [package-lock.json](frontend/package-lock.json); as do backend estão em [pom.xml](backend/pom.xml).

## Estrutura

```text
backend/             API, regras de negócio, segurança, migrations e testes
frontend/            Site em React
docs/prd/            Requisitos e diagramas
docs/postman/        Coleção de requisições
compose.yaml         Backend e banco de desenvolvimento
compose.test.yaml    Ambiente isolado de testes
.env.example         Modelo de configuração local, sem credenciais pessoais
```

## Pré-requisitos

- Git, para obter e versionar o projeto.
- Docker Desktop iniciado, com containers Linux e Docker Compose v2, para backend e banco.
- Node.js 24.x e npm para o frontend. Essa linha atende aos requisitos das dependências registradas no lockfile.

Usando Docker, não é necessário instalar Java, Maven ou MySQL na máquina. A execução do backend pela IDE é descrita adiante.

Abra o terminal na raiz do projeto, onde estão os arquivos `compose.yaml` e `.env.example`. Os exemplos abaixo usam PowerShell.

## 1. Configurar e iniciar backend e banco

Crie a configuração sem sobrescrever um `.env` existente:

```powershell
if (-not (Test-Path .env)) { Copy-Item .env.example .env }
```

Edite o `.env` localmente antes da primeira inicialização:

| Variável | Finalidade |
| --- | --- |
| `DB_NAME` | Nome do banco; exemplo: `conale` |
| `DB_USERNAME` | Usuário MySQL utilizado pelo backend |
| `DB_PASSWORD` | Senha desse usuário MySQL |
| `DB_ROOT_PASSWORD` | Senha do administrador do MySQL |
| `DB_PORT` | Porta do banco na máquina; padrão `3306` |
| `API_PORT` | Porta da API na máquina; padrão `8080` |
| `ADMIN_INITIAL_NAME` | Nome do primeiro administrador do sistema |
| `ADMIN_INITIAL_EMAIL` | E-mail novo para criar o primeiro administrador |
| `ADMIN_INITIAL_PASSWORD` | Senha inicial da conta administrativa |
| `SESSION_COOKIE_SECURE` | `false` no desenvolvimento HTTP local; `true` na implantação com HTTPS |
| `DB_URL` | Conexão para execução direta pela IDE; o Compose monta sua própria URL usando o serviço `mysql` |

Senhas de banco não são senhas de login do sistema. Substitua os valores de exemplo por valores locais e não envie `.env` ao Git. O arquivo contém somente variáveis e comentários, não comandos de terminal.

```powershell
docker compose config --quiet
docker compose up -d --build backend
docker compose ps -a
docker compose logs --tail=100 backend
```

O Compose aguarda o MySQL responder; o backend aplica as migrations pendentes. A primeira execução pode demorar devido ao download das imagens e dependências.

### Primeiro administrador

Preencha as três variáveis `ADMIN_INITIAL_*` antes de iniciar. A senha deve ter entre 8 e 72 caracteres e no máximo 72 bytes em UTF-8. Use credenciais próprias, não senhas de demonstração.

- A conta inicial só é criada se ainda não existir um administrador.
- Se e-mail e senha estiverem vazios, nenhuma conta inicial será criada.
- Configuração incompleta/inválida ou e-mail já cadastrado pode impedir a inicialização quando o bootstrap for necessário.
- Contas anteriores à migration V2 não são promovidas automaticamente.
- Alterar essas variáveis não redefine a senha de uma conta existente.

Após confirmar o primeiro login, remova o valor de `ADMIN_INITIAL_PASSWORD` do `.env` e recrie somente o backend para retirar a senha inicial do ambiente do container:

```powershell
docker compose up -d --force-recreate backend
```

A senha armazenada como hash no banco permanece válida. Sessões em andamento serão perdidas com a reinicialização.

### Confirmar a inicialização

Com a porta padrão, acesse [saúde da API](http://localhost:8080/actuator/health) ou execute:

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
Invoke-RestMethod http://localhost:8080/api/noticias
```

O resultado de saúde esperado é `status: UP`; a consulta de notícias retorna os registros ou uma lista vazia. Ajuste a porta dos exemplos se alterou `API_PORT`. O MySQL deve aparecer como `healthy` e o log do backend deve conter `Started ConaleApplication`.

## 2. Iniciar o frontend

Em outro terminal, partindo da raiz:

```powershell
cd frontend
node --version
npm --version
npm ci
npm run dev
```

Abra o endereço exibido pelo Vite, normalmente [http://localhost:5173](http://localhost:5173). Se a porta estiver ocupada, ele poderá selecionar outra. Encerre com `Ctrl+C`.

Rotas atuais: `/`, `/servicos`, `/sobre` e `/duvidas`. Não há rota de login ou painel administrativo. O frontend ainda não faz chamadas à API, e não há proxy de API configurado no Vite. A integração futura precisará definir encaminhamento da API ou CORS e envio dos cookies de sessão.

Verificações disponíveis, dentro de `frontend`:

```powershell
npm run lint
npm run build
npm run preview
```

O build verifica TypeScript e gera `dist/`. O preview permite visualizar esse build localmente; não é uma configuração de hospedagem de produção. Não há script de testes automatizados do frontend no `package.json` atual.

## 3. Autenticação e Postman

Consulte o [guia de autenticação](docs/autenticacao-postman.md) e importe a [coleção do Postman](docs/postman/ConAle-Auth.postman_collection.json).

Na coleção, ajuste `baseUrl` para a porta do seu backend. A coleção de demonstração utiliza `http://localhost:18081`; o Compose padrão utiliza `http://localhost:8080`. Preencha `adminEmail` e `adminPassword` apenas localmente. Não exporte nem compartilhe a coleção com credenciais reais.

Use sempre o mesmo host e mantenha os cookies habilitados; não misture `localhost` com `127.0.0.1` durante a sessão.

| Etapa | Requisição | Resultado esperado |
| --- | --- | --- |
| Obter CSRF | `GET /api/auth/csrf` | Token e nome do cabeçalho; preservar o cookie recebido |
| Entrar | `POST /api/auth/login` | Enviar JSON com `email` e `senha`, além do cabeçalho CSRF; sucesso `200` |
| Renovar CSRF | `GET /api/auth/csrf` | Obter novo token após o login |
| Consultar sessão | `GET /api/auth/me` | `200` com ID, nome e e-mail do administrador |
| Sair | `POST /api/auth/logout` | Enviar CSRF; sucesso `204` |
| Conferir logout | `GET /api/auth/me` | `401` após encerramento da sessão |

O login troca o ID da sessão e invalida o CSRF anterior. Requisições de alteração usam o cookie `JSESSIONID` e o cabeçalho `X-CSRF-TOKEN` atualizado. Não se utiliza Bearer Token/JWT. A senha é armazenada como hash BCrypt e não é devolvida nas respostas.

Sessões expiram após 30 minutos de inatividade e são perdidas ao reiniciar o backend. O cookie é HttpOnly e SameSite=Strict. O backend não oferece cadastro público.

## 4. APIs disponíveis

### Notícias

GETs são públicos; criação, edição e exclusão exigem administrador autenticado e CSRF válido.

| Método | Rota | Resultado de sucesso |
| --- | --- | --- |
| POST | `/api/noticias` | `201`, registro criado e cabeçalho Location |
| GET | `/api/noticias` | `200`, lista por ID decrescente |
| GET | `/api/noticias/{id}` | `200`, registro consultado |
| PUT | `/api/noticias/{id}` | `200`, registro atualizado |
| DELETE | `/api/noticias/{id}` | `204`, exclusão definitiva |

Corpo de POST/PUT, com `Content-Type: application/json`:

```json
{
  "titulo": "Novidades da ConAle",
  "sub_titulo": "Confira as informações mais recentes."
}
```

O título é obrigatório, não aceita somente espaços e tem limite de 200 caracteres. O subtítulo é opcional, com limite de 500 caracteres. Omitir `sub_titulo` ou enviar `null` no PUT remove o valor anterior. O ID é gerado pelo banco. O registro fica disponível na API pública após o salvamento, sem etapa adicional de publicação.

### Usuários

Todas as rotas exigem administrador autenticado. POST, PUT e DELETE também exigem CSRF válido.

| Método | Rota | Resultado de sucesso |
| --- | --- | --- |
| POST | `/api/usuarios` | `201`, nova conta administrativa |
| GET | `/api/usuarios` | `200`, lista por ID decrescente |
| GET | `/api/usuarios/{id}` | `200`, usuário consultado |
| PUT | `/api/usuarios/{id}` | `200`, usuário atualizado |
| DELETE | `/api/usuarios/{id}` | `204`, exclusão definitiva |

No cadastro, envie `nome`, `email` e `senha`. Nome é obrigatório (até 150 caracteres), e-mail é obrigatório, válido e único (até 254 caracteres). A senha segue os limites informados na criação do primeiro administrador. E-mails são normalizados para minúsculas e sem espaços nas extremidades.

No PUT, nome e e-mail continuam obrigatórios; omitir a senha ou enviar `null` mantém a senha atual. As respostas contêm ID, nome e e-mail, nunca senha ou hash. Novas contas criadas pela API recebem acesso administrativo; não há seleção de perfil editorial. A exclusão do último administrador é bloqueada.

### Erros e persistência

- `400`: entrada inválida; erros de validação podem incluir mensagens por campo em `campos`.
- `401`: credenciais inválidas ou acesso sem sessão autenticada.
- `403`: acesso negado ou CSRF ausente/inválido. Na escrita, o CSRF pode ser recusado antes da verificação do login.
- `404`: registro inexistente.
- `409`: conflito, como e-mail duplicado ou tentativa de excluir o último administrador.

As operações no Postman alteram o banco configurado no backend; não são simulações. Utilize registros de teste. Alterações de e-mail/senha, revogação do acesso e exclusão de conta invalidam sessões anteriores no próximo acesso; ao alterar suas credenciais, faça novo login.

## Banco e migrations

- **V1 — estrutura inicial:** cria `usuario`, `banner`, `noticia` e `rodape`; insere o rodapé único com `id = 1`.
- **V2 — acesso administrativo:** adiciona `administrador` a `usuario`, inicialmente falso para contas anteriores.

O Hibernate valida os mapeamentos; o Flyway controla a estrutura. Não altere migrations já aplicadas, nem seus comentários: isso pode gerar erro de checksum. Uma próxima evolução deve usar nova versão, como `V3__descricao.sql`, respeitando as versões existentes na branch.

Para consultar o banco com o usuário e nome do exemplo:

```powershell
docker compose exec mysql mysql --user=conale --password conale
```

O último `conale` é o nome do banco. Digite a senha de `DB_PASSWORD` quando solicitada; ajuste usuário e banco se mudou as variáveis. Exemplos de consultas:

```sql
SHOW TABLES;
SELECT version, description, success FROM flyway_schema_history;
SELECT id, titulo, sub_titulo FROM noticia;
SELECT id, nome, email, administrador FROM usuario;
exit
```

## Testes automatizados do backend

O ambiente de testes usa o projeto Compose `conale-tests`, MySQL separado, armazenamento temporário e nenhuma porta publicada. Não utiliza o volume do banco de desenvolvimento.

Na raiz, execute:

```powershell
docker compose -f compose.test.yaml up --build --abort-on-container-exit --exit-code-from backend-test
$resultadoTestes = $LASTEXITCODE
docker compose -f compose.test.yaml down
if ($resultadoTestes -ne 0) { throw 'Os testes falharam. Confira os logs acima.' }
```

O sucesso deve ser confirmado pelo código de saída zero e pelo relatório do Maven sem falhas ou erros. O comando de limpeza não deve ser usado como evidência de sucesso dos testes.

As classes existentes são:

- `DatabaseMigrationTest`: estrutura e restrições do banco.
- `NoticiaApiTest`: CRUD de notícias e validações.
- `AuthApiTest`: autenticação, autorização, sessão, CSRF e cenários administrativos.
- `PrimeiroAdminTest`: criação e validação do administrador inicial.

Nunca aponte testes de banco para dados reais. A presença dos testes no repositório não substitui sua execução após alterações. O build da imagem de execução pula testes; use o comando acima para verificá-los.

## Execução do backend pela IDE

Use JDK 24 e Maven compatível com o projeto (3.9.9 no Dockerfile). Importe `backend/pom.xml` e inicie somente o MySQL:

```powershell
docker compose up -d mysql
```

Configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` no ambiente da execução Java. Use a URL local de `.env.example`, ajustando porta e nome do banco. O Spring Boot não carrega o `.env` automaticamente. Configure também `ADMIN_INITIAL_*` se precisar criar o primeiro administrador.

Inicie `ConaleApplication` pela IDE ou, dentro de `backend`, execute `mvn spring-boot:run`. A porta direta do Spring Boot é controlada por `SERVER_PORT`; `API_PORT` controla somente a publicação da porta pelo Compose. Para limitar a execução pela IDE à máquina local, configure `SERVER_ADDRESS=127.0.0.1`.

Não execute backend pela IDE e Docker na mesma porta.

## Parar, atualizar e solucionar problemas

Na raiz:

```powershell
docker compose down
docker compose up -d --build backend
```

O banco permanece no volume `conale_mysql_data` com o nome de projeto padrão. **Não use `docker compose down -v` no fluxo normal: ele remove volumes e pode apagar seus dados.** Encerre o frontend separadamente com `Ctrl+C`.

- **Variáveis obrigatórias ausentes:** confira se `.env` está na raiz e se as variáveis do banco têm valor. Valide com `docker compose config --quiet`, sem imprimir segredos.
- **Conexão recusada na API:** Docker Desktop aberto não significa backend iniciado. Confira `docker compose ps -a` e `docker compose logs --tail=100 backend mysql`.
- **Porta ocupada:** altere `API_PORT` ou `DB_PORT` e recrie os serviços. Dentro do Docker, o backend continua acessando `mysql:3306`.
- **Mudança de senha no `.env`:** não altera automaticamente credenciais já gravadas no MySQL nem a senha do administrador do sistema.
- **Erro CSRF no Postman:** preserve cookies, use o mesmo host e obtenha novo token após cada login.
- **Node incompatível:** confira `node --version`; utilize a versão indicada nos pré-requisitos antes de instalar as dependências.
- **PowerShell bloqueia `npm.ps1`:** use `npm.cmd` nos mesmos comandos, sem alterar a política de execução da máquina.
- **Duas cópias locais:** por padrão, ambas usam o projeto Docker chamado `conale`; não representam ambientes independentes.
- **Aviso de compatibilidade Flyway/MySQL:** confira o resultado das migrations e avalie a compatibilidade antes de produção; um aviso não comprova que a execução falhou nem que existe suporte completo.

Antes de disponibilizar na internet, preparar HTTPS, cookies seguros, proteção contra tentativas repetidas de login, recuperação de acesso, backups e a configuração de integração do frontend. Esses controles não estão todos implementados nesta etapa.

## Documentação e contribuição

- [PRD revisado e diagramas](docs/prd/conale.md): escopo, conteúdos fixos/editáveis e decisões pendentes.
- [Autenticação e Postman](docs/autenticacao-postman.md): configuração e testes manuais.
- [Coleção Postman](docs/postman/ConAle-Auth.postman_collection.json): requisições da API.

Antes de um commit, revise os arquivos com `git status` e `git diff`, execute as verificações correspondentes e confira `git diff --cached --check` após selecionar as alterações. Não versione `.env`, credenciais, dependências ou arquivos gerados (`node_modules`, `target` e `dist`).
