# Autenticação e permissões no Postman

Use este guia para verificar a API da versão final da ConAle. O backend autentica **Administrador** e **Design** com e-mail e senha e mantém a sessão no servidor, identificada pelo cookie `JSESSIONID`.

## Preparação

1. Configure a conta administrativa e inicie o backend local conforme o [README](../README.md).
2. Importe a [coleção ConAle](postman/ConAle-Auth.postman_collection.json). Configure `baseUrl` para o endereço da API local, normalmente `http://localhost:8080`. A porta `18081` no arquivo é o valor da demonstração original.
3. Preencha `adminEmail` e `adminPassword` localmente. Use um banco de teste para executar a coleção inteira, pois ela cria, edita e exclui registros. Mantenha os cookies habilitados e use o mesmo host durante a sessão.

Para executar o fluxo completo de contas, use o Administrador original. A requisição de cadastro da coleção omite `administrador`. Com o original, o padrão é Administrador. Com outro Administrador, o padrão é Design.

## Sessão e CSRF

| Etapa | Requisição | Resultado esperado |
| --- | --- | --- |
| Obter CSRF | `GET /api/auth/csrf` | `200` com `token` e `headerName`. Preserve o cookie de sessão. |
| Entrar | `POST /api/auth/login` | `200` ao enviar `email` e `senha` em JSON com o cabeçalho CSRF válido. A resposta identifica a conta e seu papel. |
| Renovar CSRF | `GET /api/auth/csrf` | `200` com novo token. O login troca o identificador da sessão e invalida o token anterior. |
| Verificar a conta | `GET /api/auth/me` | `200` com ID, nome, e-mail e indicadores da conta. |
| Testar gravações | `POST`, `PUT`, `PATCH` ou `DELETE` | Envie o cookie e o token CSRF atualizado. Na coleção, o token é salvo em `csrfToken` para envio no cabeçalho `X-CSRF-TOKEN`. |
| Sair | `POST /api/auth/logout` | `204` com CSRF válido. A sessão é encerrada. |
| Confirmar a saída | `GET /api/auth/me` | `401` após o logout. |

O cookie usa HttpOnly e SameSite=Strict. A sessão expira após 30 minutos de inatividade e também é encerrada ao reiniciar o backend. Para o Compose de publicação com HTTPS na infraestrutura, configure `SESSION_COOKIE_SECURE=true`.

## Permissões e respostas

| Operação | Sem sessão | Design | Administrador |
| --- | --- | --- | --- |
| Consultar cards informativos em `GET /api/noticias` | `200` | `200` | `200` |
| Consultar `GET /api/auth/me` | `401` | `200` | `200` |
| Alterar conteúdo com CSRF válido | `401` | Permitido | Permitido |
| Consultar `GET /api/usuarios` | `401` | `403` | `200` |
| Gerenciar usuários com CSRF válido | `401` | `403` | Permitido conforme as regras de papéis |

Somente o Administrador original pode atribuir o papel de Administrador a novas contas ou alterar o papel de outras contas. O papel do original é fixo e a exclusão dessa conta é bloqueada. No `PUT /api/usuarios/{id}`, omita a senha para manter a atual.

Gravações sem CSRF válido retornam `403`. Credenciais inválidas retornam `401`. E-mail duplicado e tentativa de excluir o último administrador retornam `409`. Alterações no e-mail, na senha ou no papel, assim como a exclusão da conta, invalidam sessões anteriores no próximo acesso.

A coleção denomina os cards como “notícias” em algumas requisições porque o endpoint continua em `/api/noticias`. A tabela de rotas dos outros módulos e o comando da suíte automatizada estão no [README](../README.md).
