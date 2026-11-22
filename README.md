# Projeto ConAle

Versão final do projeto da disciplina de Laboratório de Programação para a Internet (2026B), desenvolvido para a ConAle, consultoria contábil especializada na área da saúde.

A entrega reúne um site institucional responsivo e um painel administrativo. O site apresenta os serviços da ConAle, reúne os canais de atendimento e encaminha os clientes ao portal externo Onvio. A equipe gerencia os conteúdos do site pelo painel.

## Recursos entregues

- **Site público** com Home (`/`), Serviços (`/servicos`), Sobre Mim (`/sobre`) e Dúvidas Frequentes (`/duvidas`).
- **Painel administrativo** em `/admin`, com login em `/admin/login`. O perfil **Design** gerencia conteúdo. O perfil **Administrador** também acessa a gestão de contas. O Administrador original tem a permissão de atribuir o papel de Administrador e alterar papéis.
- **Banners** com imagens de computador e celular, prévia, recorte e links por posição. Cada posição aceita um banner ativo. Se a imagem de celular estiver vazia, o site usa a imagem de computador. A foto de Sobre Mim tem apenas função visual.
- **Links do site** para os cinco atalhos da Home e de Serviços, os três destaques da Home e os quatro serviços. O painel aceita URLs HTTP ou HTTPS e destinos existentes no site, como `/servicos` ou `/duvidas#faq`. Limpar um campo restaura o comportamento padrão do componente.
- **Cards informativos** compartilhados pela Home, por Serviços e por Dúvidas Frequentes. Cada registro tem título obrigatório e subtítulo opcional.
- **Indicadores e avaliações.** A Home exibe os indicadores de anos de atuação e empresas abertas. Sobre Mim mostra as avaliações cadastradas.
- **Rodapé** com modelos editáveis e um modelo ativo por vez. O Instagram abre o perfil cadastrado, o telefone e o e-mail alimentam os botões flutuantes e o endereço é usado na prévia clicável do Google Maps. No cadastro, é possível digitar o endereço em duas linhas.
- **Navegação no painel.** O botão **Voltar** dos formulários retorna à listagem da seção sem salvar. Na tela **Links do site** e nas listagens, a navegação entre seções fica no menu lateral.

Em um banco vazio, o sistema cadastra **cinco banners padrão, seis cards informativos, cinco avaliações, um indicador com 0 e 0 e um rodapé Padrão ativo**. Os destinos personalizados de **Links do site** são preenchidos pela equipe no painel. Até o cadastro dos destinos, os atalhos e os botões de contratação ficam desativados. Os destaques da Home apontam por padrão para a seção de serviços.

O escopo completo, as regras de acesso e os formatos dos banners estão no [PRD da versão final](docs/prd/conale.md).

## Tecnologias e estrutura

| Camada | Tecnologias |
| --- | --- |
| Frontend | React 19, TypeScript 6, Vite 8, React Router, React Icons, React Easy Crop e CSS |
| Backend | Java 24, Spring Boot 3.5.16, Spring Web, Validation, Spring Data JPA e Maven 3.9.9 |
| Segurança | Spring Security, BCrypt, sessão por cookie e proteção CSRF |
| Banco | MySQL 8.4.11 e Flyway |
| Execução e verificação | Docker Compose, Nginx, GitHub Actions, testes Java/Spring e ESLint |

As versões das dependências estão no [manifesto do frontend](frontend/package.json), no [lockfile](frontend/package-lock.json) e no [POM do backend](backend/pom.xml).

```text
backend/             API, segurança, migrations e testes
frontend/            Site público e painel administrativo
docs/prd/            PRD, diagramas PNG e fontes PlantUML
docs/postman/        Coleção de requisições para verificação da API
compose.yaml         Backend e banco para desenvolvimento
compose.test.yaml    Testes do backend com banco isolado
compose.prod.yaml    Nginx, backend e MySQL para a VM
.env.example         Modelo de configuração
.github/workflows/   Verificação automática de frontend e backend
```

## Executar localmente

Use Git, Docker com containers Linux e Docker Compose v2, além de Node.js 24.x e npm. O Docker fornece Java, Maven e MySQL para a execução do backend. Execute os comandos abaixo no PowerShell, a partir da raiz do projeto.

### 1. Configurar o ambiente

Crie o arquivo `.env` a partir do exemplo, preservando uma configuração existente.

```powershell
if (-not (Test-Path .env)) { Copy-Item .env.example .env }
```

| Variável | Finalidade |
| --- | --- |
| `DB_NAME`, `DB_USERNAME` e `DB_PASSWORD` | Banco e credenciais do backend no MySQL |
| `DB_ROOT_PASSWORD` | Senha do administrador do MySQL |
| `DB_PORT` e `API_PORT` | Portas locais do banco e da API. Padrões `3306` e `8080` |
| `ADMIN_INITIAL_NAME`, `ADMIN_INITIAL_EMAIL` e `ADMIN_INITIAL_PASSWORD` | Nome, e-mail e senha da primeira conta administrativa |
| `SESSION_COOKIE_SECURE` | Use `false` em HTTP local e `true` na implantação com HTTPS |
| `DB_URL` | Conexão para execução direta pela IDE. O Compose monta a conexão com o serviço `mysql` |
| `WEB_PORT` | Porta pública do Compose de publicação. Padrão `80` |

Use valores próprios para as senhas e mantenha o `.env` fora do Git. A senha do administrador precisa ter pelo menos 8 caracteres e no máximo 72 bytes em UTF-8.

### 2. Iniciar o backend e o banco

```powershell
docker compose config --quiet
docker compose up -d --build backend
docker compose ps -a
docker compose logs --tail=100 backend
```

O Compose aguarda o MySQL ficar saudável e o Spring Boot aplica as migrations. Quando o log mostrar `Started ConaleApplication`, consulte a rota de saúde. O resultado esperado é `status: UP`. Ajuste a URL se estiver usando outra porta.

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```

Em um banco sem Administrador, o backend cria a primeira conta com as variáveis `ADMIN_INITIAL_*` preenchidas. Em um banco que já tem Administrador e ainda não tem um original registrado, informe em `ADMIN_INITIAL_EMAIL` o e-mail da conta existente a vincular. O vínculo é persistido. Alterar a configuração do ambiente depois do vínculo mantém a conta original e a senha do banco.

Após confirmar o primeiro login, esvazie `ADMIN_INITIAL_PASSWORD` no `.env` e recrie o backend. A senha da conta continua válida e a sessão em andamento é encerrada.

```powershell
docker compose up -d --force-recreate backend
```

### 3. Iniciar o frontend

Em outro terminal, a partir da raiz do projeto, execute:

```powershell
cd frontend
npm ci
npm run dev
```

Abra o endereço informado pelo Vite, geralmente [localhost:5173](http://localhost:5173). O Vite encaminha `/api` e `/uploads` para `http://localhost:8080`. Se mudar `API_PORT`, ajuste também o destino em [vite.config.ts](frontend/vite.config.ts). Para usar o painel local, acrescente `/admin/login` ao endereço exibido.

Para executar o backend pela IDE, use JDK 24 e importe [backend/pom.xml](backend/pom.xml). Inicie o MySQL com `docker compose up -d mysql` e configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` e, no primeiro acesso, `ADMIN_INITIAL_*` no ambiente da execução Java. Use `UPLOAD_DIR` para escolher uma pasta absoluta de uploads. A porta do Spring pode ser definida com `SERVER_PORT` quando o backend é iniciado diretamente.

## Publicar com Docker Compose

Com o `.env` da VM configurado, execute na raiz do projeto:

```powershell
docker compose -f compose.prod.yaml config --quiet
docker compose -f compose.prod.yaml up -d --build
docker compose -f compose.prod.yaml ps
```

O Nginx expõe o site pela porta `WEB_PORT`, encaminha `/api` e `/uploads/banners/` ao backend e disponibiliza `/health` para verificação. Backend e MySQL permanecem na rede interna do Compose. O arquivo de publicação atende HTTP. Quando houver terminação HTTPS na infraestrutura da VM, use `SESSION_COOKIE_SECURE=true` para a sessão administrativa.

O volume `mysql_data` guarda o banco e o volume `banner_uploads` guarda as imagens enviadas. Preserve ambos na recriação dos containers. Os arquivos Compose local e de publicação usam o mesmo nome de projeto por padrão. Para ambientes independentes no mesmo host, use nomes de projeto diferentes com `-p` e portas distintas. Use `docker compose down` para encerrar o ambiente local e `docker compose -f compose.prod.yaml down` para encerrar o ambiente de publicação. A opção `-v` remove os volumes do projeto selecionado.

## API e autenticação

| Recurso | Leitura pública | Operações no painel |
| --- | --- | --- |
| Banners | `GET /api/banners/ativos` | CRUD em `/api/banners` e envio de imagem em `POST /api/banners/upload` |
| Links do site | `GET /api/links-site` | `PATCH /api/links-site` |
| Cards informativos | `GET /api/noticias` e `GET /api/noticias/{id}` | CRUD em `/api/noticias`. O nome técnico da rota foi mantido |
| Indicadores | `GET /api/indicadores-institucionais` | CRUD em `/api/indicadores-institucionais` |
| Avaliações | `GET /api/mensagens-clientes` | CRUD em `/api/mensagens-clientes` |
| Rodapé | `GET /api/rodape` | Gerenciar `/api/rodape/modelos`, ativar em `PUT /api/rodape/modelos/{id}/ativacao` e atualizar o ativo em `PUT /api/rodape` |
| Usuários | Acesso restrito a Administrador | CRUD em `/api/usuarios`, respeitando o papel da conta |

Para criar ou editar um card informativo pela API, envie um JSON com `titulo` e `sub_titulo`. Exemplo de corpo para `POST /api/noticias`:

```json
{
  "titulo": "Como agendar uma reunião?",
  "sub_titulo": "Entre em contato com nossa equipe pelo WhatsApp."
}
```

Para testar manualmente com o Postman, importe a [coleção do projeto](docs/postman/ConAle-Auth.postman_collection.json) e configure `baseUrl` de acordo com a porta da API. Mantenha os cookies e use o mesmo host durante toda a sessão.

1. Obtenha o token em `GET /api/auth/csrf`.
2. Entre com `POST /api/auth/login`, enviando `email`, `senha` e o token no cabeçalho informado pela API.
3. Obtenha um novo token de CSRF após o login e consulte `GET /api/auth/me` para verificar a conta autenticada.
4. Envie o cookie `JSESSIONID` e o token CSRF atualizado nas operações de gravação.
5. Use `POST /api/auth/logout` com o token válido para encerrar a sessão.

A sessão expira após 30 minutos de inatividade. O [guia de autenticação e Postman](docs/autenticacao-postman.md) detalha as permissões, os retornos e as verificações manuais.

## Verificar a entrega

No diretório `frontend`:

```powershell
npm run lint
npm run build
```

Na raiz do repositório, execute os testes do backend com MySQL temporário:

```powershell
docker compose -f compose.test.yaml up --build --abort-on-container-exit --exit-code-from backend-test
$resultadoTestes = $LASTEXITCODE
docker compose -f compose.test.yaml down
if ($resultadoTestes -ne 0) { throw 'Os testes falharam. Confira o relatório do Maven.' }
```

A suíte cobre migrations, autenticação e permissões, criação do Administrador original, operações dos cards informativos e o gerenciamento de rodapés. O [workflow de CI](.github/workflows/ci.yaml) executa o lint e o build do frontend e os testes do backend em pushes e pull requests para `dev` e `main`.

O Flyway é responsável pelo esquema e pelos dados iniciais. As migrations V1 a V17 ficam em [db/migration](backend/src/main/resources/db/migration). Preserve os arquivos já aplicados para manter os checksums registrados no banco.

## Solução de problemas

- **API indisponível.** Na raiz, veja `docker compose ps -a` e `docker compose logs --tail=100 backend mysql`. Confira a rota de saúde e o destino do proxy do Vite.
- **Variáveis de banco ausentes.** Confira o `.env` na raiz e execute `docker compose config --quiet`.
- **Porta ocupada.** Ajuste `DB_PORT`, `API_PORT` ou `WEB_PORT` conforme o ambiente.
- **Importação do frontend com erro.** Em `frontend`, confira a versão do Node e execute `npm ci`. Se o PowerShell bloquear `npm.ps1`, use `npm.cmd`.
- **Sessão ou CSRF inválido.** Preserve os cookies, use o mesmo host e solicite um novo token depois de autenticar.
