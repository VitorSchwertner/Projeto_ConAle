# Projeto ConAle

Projeto desenvolvido para a disciplina de Laboratório de Programação para a Internet (2026B), tendo como cliente a ConAle.

## Estado atual

Backend com Java 24, Spring Boot 3.5.16 e Maven 3.9.9; banco MySQL 8.4.11 com migrations gerenciadas pelo Flyway. A estrutura inicial contém `usuario`, `banner`, `noticia` e `rodape`.

Atualmente, apenas a rota de saúde está disponível. Frontend, entidades JPA, autenticação e APIs de conteúdo serão implementados nas próximas etapas.

## Como iniciar

Instale o Git para clonar o repositório e o Docker Desktop com Docker Compose v2. Inicie o Docker usando containers Linux. Pelo Docker, não é necessário instalar Java, Maven ou MySQL separadamente.

Na raiz do projeto, abra o PowerShell e crie a configuração local sem sobrescrever um arquivo existente, ou apenas copie o .env.example para um .env:

```powershell
if (-not (Test-Path .env)) { Copy-Item .env.example .env }
```

Ajuste as senhas no `.env` antes da primeira inicialização. `DB_PASSWORD` é a senha do usuário MySQL utilizado pelo backend; `DB_ROOT_PASSWORD` é a senha do administrador do banco. Não são senhas de login do site.

O `.env` deve conter apenas variáveis e comentários, nunca comandos de terminal.

Para validar a configuração e iniciar o backend junto com o banco:

```powershell
docker compose config --quiet
docker compose up -d --build backend
docker compose ps -a
docker compose logs --tail=100 backend
```

O primeiro início pode levar alguns minutos. O Compose aguarda o MySQL responder e o Flyway cria as tabelas automaticamente.

## Como confirmar que funcionou

- MySQL aparece como `healthy` e o backend permanece em execução.
- Os logs mostram `Started ConaleApplication`.
- [A rota de saúde](http://localhost:8080/actuator/health) retorna `{"status":"UP"}`.


Para consultar o banco com os nomes padrão do exemplo:

```powershell
docker compose exec mysql mysql --user=conale --password conale
```

Digite a senha de `DB_PASSWORD`. Se mudou o usuário ou o nome do banco, ajuste o comando. Dentro do MySQL:

```sql
SHOW TABLES;
SELECT version, description, success FROM flyway_schema_history;
SELECT * FROM rodape;
exit
```

O resultado deve incluir as quatro tabelas, o histórico do Flyway com a versão 1 bem-sucedida e o rodapé inicial com `id = 1`.

## Banco de dados

- `usuario`: nome, email único e hash de senha obrigatórios. Nenhum usuário do site é criado automaticamente.
- `banner`: título e referência da imagem obrigatórios; link opcional; começa inativo.
- `noticia`: título obrigatório e subtítulo opcional.
- `rodape`: registro único com `id = 1`, inicialmente sem informações de contato.

As migrations ficam em `backend/src/main/resources/db/migration/`. Não altere migrations já aplicadas, nem seus comentários: isso pode causar erro de checksum. Evoluções devem entrar em novos arquivos, como `V2__descricao.sql`. O Hibernate está configurado para validar os futuros mapeamentos JPA, não para criar tabelas.

## Parar e iniciar novamente

```powershell
docker compose down
docker compose up -d
```

Os dados ficam no volume `conale_mysql_data`. **Não use `docker compose down -v` no fluxo normal: ele apaga o volume e seus dados.**

Após alterar o código, use `docker compose up -d --build backend` para reconstruir a aplicação.

## Cuidados e problemas comuns

- Não envie o `.env` ao Git. As senhas de exemplo e a conexão sem TLS são somente para desenvolvimento local.
- Alterar senhas no `.env` após a criação do banco não altera as senhas já gravadas no MySQL.
- Se a porta 8080 estiver ocupada, altere `API_PORT` no `.env`, execute novamente o comando de inicialização e use a nova porta no navegador.
- Se a porta 3306 estiver ocupada, altere `DB_PORT`. Dentro do Docker, o backend continua usando `mysql:3306`.
- Para investigar falhas, consulte `docker compose logs --tail=100 backend mysql`. Evite compartilhar configurações ou logs que contenham senhas.
- Duas cópias deste projeto no mesmo Docker compartilham o ambiente chamado `conale`; não são bancos independentes.
- Caso apareça um aviso de compatibilidade entre Flyway e MySQL, confira se as migrations concluíram. O aviso não comprova suporte completo; reavalie as versões antes de usar em produção.

## Execução pela IDE

Para executar fora do Docker, use JDK 24 e Maven (o Dockerfile usa 3.9.9). Abra `backend/pom.xml` como projeto Maven e configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` no ambiente de execução. O Spring Boot não lê o `.env` automaticamente.

A URL deve apontar para `localhost` e a porta publicada do MySQL, mantendo as opções JDBC do `.env.example`. Inicie o banco com `docker compose up -d mysql` e execute `ConaleApplication`. Não execute o backend pela IDE e pelo Docker na mesma porta.

## Testes automatizados

O arquivo `DatabaseMigrationTest.java` contém cinco testes de integração: histórico de migrations, email único e hash obrigatório, regras do banner, título obrigatório da notícia e registro único do rodapé.

Execute na raiz do projeto:

```powershell
docker compose -f compose.test.yaml up --build --abort-on-container-exit --exit-code-from backend-test
docker compose -f compose.test.yaml down
```

Confira o resultado do primeiro comando antes da limpeza: deve retornar código 0 e `Tests run: 5, Failures: 0, Errors: 0`. O banco de testes é separado, temporário e não publica portas; não utiliza os dados do banco local. Nunca execute esses testes apontando para um banco real.

## Antes do commit

- Execute os testes e confira a rota de saúde.
- Confira `git diff --check` e `git status --short`.
- Inclua as versões finais dos arquivos, inclusive os testes; não inclua `.env`, `target/` ou senhas pessoais.