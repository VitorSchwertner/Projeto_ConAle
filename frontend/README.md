# Frontend da ConAle

Interface do site institucional e do painel administrativo da ConAle, construída com React, TypeScript e Vite.

## Desenvolvimento local

Com Node.js 24.x e o backend local iniciado, execute neste diretório:

```powershell
npm ci
npm run dev
```

O Vite informa o endereço do site no terminal. Para abrir o painel, acesse `/admin/login`. O [proxy do Vite](vite.config.ts) encaminha `/api` e `/uploads` para `http://localhost:8080`.

## Verificações

```powershell
npm run lint
npm run build
```

O build verifica o TypeScript e gera os arquivos em `dist/`. Na publicação, o Nginx serve o frontend e encaminha as requisições ao backend.

Consulte o [README do projeto](../README.md) para configurar o ambiente e publicar a aplicação. O [PRD](../docs/prd/conale.md) descreve as páginas e os módulos entregues.
