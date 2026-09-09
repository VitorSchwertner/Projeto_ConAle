**Problema**

O escritório ConAle presta serviços de contabilidade especializados para profissionais e empresas da área da saúde. Atualmente, a empresa não possui um site institucional, fazendo com que sua presença digital seja limitada e que a divulgação de seus serviços dependa principalmente de indicações e contatos já existentes.

Essa limitação dificulta que potenciais clientes encontrem a ConAle, conheçam sua especialização na área da saúde e tenham acesso, de forma clara e centralizada, às informações necessárias para avaliar a contratação dos serviços.

Quem perde com isso, e como:

* A ConAle, que possui menos oportunidades de apresentar seus serviços a potenciais clientes fora de sua rede atual de indicações.  
* Potenciais clientes, que podem ter dificuldade para encontrar uma empresa especializada em contabilidade para a área da saúde e conhecer seus serviços.  
* A empresa como marca, que deixa de utilizar uma presença digital profissional para transmitir credibilidade e fortalecer seu posicionamento no mercado.

 

**Solução**

Desenvolver um site institucional para a ConAle, apresentando de forma clara sua atuação e especialização em contabilidade para profissionais e empresas da área da saúde.

O site terá como objetivo fortalecer a presença digital da empresa, transmitindo uma imagem profissional, confiável e acolhedora, além de apresentar seus serviços, notícias e novidades relevantes do ramo.

A plataforma também facilitará o contato de potenciais clientes com a ConAle, disponibilizando informações e caminhos claros para solicitar atendimento e conhecer o processo para se tornar cliente.

Para os clientes atuais, o site disponibilizará um direcionamento para a área do cliente, permitindo o acesso ao sistema já utilizado pelo escritório sem substituir ou duplicar suas funcionalidades.

**Escopo**

O sistema será desenvolvido em partes, conforme tabela abaixo. Começará pela parte visual do site (front-end) e seguindo pela parte editável, que será um painel administrativo (como se fosse o back-end).

| Ordem | Parte | Porque nessa posição |
| :---- | :---- | :---- |
| 1 | Estrutura base e navegação do site | Define o menu, rodapé e estrutura compartilhada pelas demais páginas. |
| 2 | Página inicial | É o principal ponto de entrada e apresenta a ConAle aos potenciais clientes. |
| 3 | Página de Serviços | Apresenta os serviços oferecidos e permite ao visitante conhecer as soluções da empresa. |
| 4 | Página Sobre Nós | Apresenta a empresa, sua atuação, propósito, missão, valores e credenciais. |
| 5 | Página de Dúvidas Frequentes | Responde às principais dúvidas dos visitantes e complementa as informações institucionais. |
| 6 | Área de contato e conversão | Facilita o contato e o início do processo de contratação por potenciais clientes. |
| 7 | Direcionamento para Área do Cliente  | Permite que clientes atuais encontrem o acesso ao sistema já utilizado pela ConAle. |
| 8 | Interface do chatbot | Implementa a interface de atendimento e orientação aos visitantes. |
| 9 | Autenticação do Painel Administrativo | Estabelece o controle de acesso antes das funcionalidades administrativas. |
| 10 | Gerenciamento do chatbot | Permite configurar as opções e informações apresentadas pelo chatbot. |
| 11 | Gerenciamento de conteúdo | Permite administrar banners, textos, imagens e demais conteúdos do site. |

**Requisitos Funcionais**

| RF01 | O sistema deve disponibilizar uma página inicial com informações institucionais e de apresentação da ConAle. |
| :---- | :---- |
| RF02 | O sistema deve apresentar a atuação da ConAle e sua especialização em contabilidade para profissionais e empresas da área da saúde. |
| RF03 | O sistema deve permitir a navegação entre as principais seções do site por meio do menu de navegação.  |
| RF04 | O sistema deve apresentar informações sobre o propósito, a missão e os valores da ConAle.  |
| RF05 | O sistema deve apresentar uma seção dedicada para exibir as credenciais institucionais da ConAle, incluindo as entidades/associações do setor nas quais atua e os logotipos dos principais clientes e clínicas atendidos.  |
| RF06 | O sistema deve apresentar os serviços oferecidos pela ConAle.  |
| RF07 | O sistema deve apresentar depoimentos de clientes da ConAle.  |
| RF08 | O sistema deve apresentar uma seção de dúvidas frequentes (FAQ) com perguntas e respectivas respostas.  |
| RF09 | O sistema deve apresentar os canais de contato disponibilizados pela ConAle.  |
| RF10 | O sistema deve permitir que o visitante acesse os canais de contato disponibilizados pela ConAle.  |
| RF11 | O sistema deve apresentar opções para que potenciais clientes solicitem atendimento ou iniciem o processo de contratação dos serviços.  |
| RF12 | O sistema deve disponibilizar um direcionamento para a área do cliente existente da ConAle.  |
| RF13 | O sistema deve disponibilizar um Painel Administrativo restrito à equipe interna para gestão de conteúdos.  |
| RF14 | O sistema deve permitir que os administradores gerenciem os banners, textos e imagens das seções da página inicial através do Painel Administrativo.  |
| RF15 | O sistema deve permitir que administradores cadastrem, editem e removam as marcas de clientes e clínicas exibidas no site. |
| RF16  | O sistema deve permitir que administradores cadastrem, editem e removam notícias e artigos pelo Painel Administrativo. |
| RF17 | O sistema deve disponibilizar um chatbot para orientar os visitantes sobre informações e serviços da ConAle. |

	

						

		

	

**Requisitos Não Funcionais**

| RNF01 | O site deve ser responsivo e manter seus conteúdos legíveis e funcionais em telas a partir de 360px de largura. | Teste em diferentes tamanhos de tela, incluindo 360px. |
| :---- | :---- | :---- |
| RNF02 | A interface deve seguir a identidade visual definida para a ConAle, mantendo consistência entre as páginas.  | Comparação com o protótipo aprovado. |
| RNF03 | As páginas principais do site devem carregar em até 3 segundos em condições adequadas de conexão. | Teste utilizando ferramenta de desempenho do navegador. |
| RNF04 | O site deve ser compatível com versões atuais dos principais navegadores. | Teste no Chrome, Firefox e Edge. |
| RNF05 | O acesso ao Painel Administrativo deve ser restrito a usuários autorizados. | Tentativa de acessar o painel sem autenticação. |
| RNF06 | O conteúdo apresentado no site deve ser gerenciável pelo Painel Administrativo sem necessidade de alteração direta no código-fonte. | Cadastrar ou alterar um conteúdo utilizando exclusivamente o painel. |

	

	

		

		

								

**Histórias de usuários**

1. Como profissional da saúde, quero identificar rapidamente no site que a ConAle é especializada em contabilidade para a área da saúde, para avaliar se seus serviços são adequados às minhas necessidades.

   

2. Como visitante do site, quero navegar facilmente entre as principais seções, para encontrar rapidamente as informações que procuro.

   

3. Como gestor de uma clínica, quero acessar o site da ConAle e ver claramente a lista de serviços oferecidos, para avaliar se eles atendem às necessidades específicas da minha empresa. 

   

4. Como potencial cliente, quero consultar depoimentos de clientes da ConAle, para avaliar a credibilidade e a experiência do escritório antes de entrar em contato.

   

5. Como potencial cliente, quero encontrar facilmente os canais de contato da ConAle, como WhatsApp e e-mail, para solicitar uma proposta comercial ou obter atendimento sem burocracia.

   

6. Como cliente da ConAle, quero um atalho direto no site para a Área do Cliente, para acessar meu portal contábil rapidamente.  

   

7. Como contadora, quero uma área institucional para destacar em quais diretorias, associações e entidades do setor de saúde e contabilidade a ConAle atua, para fortalecer nossa autoridade no mercado. 

   

8. Como contadora, quero uma seção dedicada no site para cadastrar e exibir a marca dos nossos maiores clientes e clínicas atendidas, para transmitir credibilidade e demonstrar nossa relevância no setor da saúde. 

   

9. Como contadora, quero poder alterar os banners, imagens e textos da página inicial sempre que desejar, para destacar novas campanhas, serviços ou eventos do escritório. 

   

10. Como contadora, quero um painel administrativo fácil de usar para publicar e editar notícias, para manter o site atualizado com novidades sobre legislação médica e tributária sem depender de um programador.

     

11. Como potencial cliente, quero conhecer o propósito, a missão e os valores da ConAle, para entender a forma como a empresa atua e decidir se seus princípios estão alinhados às minhas expectativas.

12. Como potencial cliente, quero consultar as dúvidas frequentes sobre os serviços da ConAle, para encontrar respostas antes de entrar em contato com o escritório.

13. Como potencial cliente, quero interagir com um chatbot no site, para obter respostas rápidas sobre os serviços da ConAle.

    

    

**Casos de uso**

**Atores**

| Ator | Quem é |
| :---- | :---- |
| Visitante | Pessoa que acessa o site institucional, seja cliente atual ou potencial |
| Administrador | Membro da equipe da ConAle responsável pela administração do site |
| Portal do Cliente | Sistema externo para o qual o cliente é direcionado |

**Casos de uso e rastreabilidade**

| Caso de uso | Vem da história: | Realiza |
| :---- | :---- | :---- |
| UC01 · Visualizar apresentação e especialização da ConAle | 1 | RF01, RF02 |
| UC02 · Navegar pelas seções do site | 2 | RF03 |
| UC03 · Visualizar lista de serviços oferecidos | 3 | RF06 |
| UC04 · Visualizar propósito, missão e valores | 11 | RF04 |
| UC05 · Ler depoimentos de clientes | 4 | RF07 |
| UC06 · Consultar dúvidas frequentes | 12 | RF08 |
| UC07 · Solicitar proposta e acessar canais de contato | 5 | RF09, RF10, RF11 |
| UC08 · Acessar a Área do Cliente | 6 | RF12 |
| UC09 · Visualizar e gerenciar credenciais, entidades e clientes atendidos | 7,8 | RF05, RF15  |
| UC10 · Gerenciar banners, textos e imagens da Home | 9 | RF13, RF14 |
| UC11 · Gerenciar notícias e artigos pelo Painel | 10 | RF13, RF16 |
| UC12 · Interagir com o chatbot | 13 | RF17 |

**Diagrama de casos de uso**

O diagrama apresenta os atores que interagem com o Site Institucional da ConAle e os respectivos casos de uso. O Visitante acessa as funcionalidades públicas do site, enquanto o Administrador utiliza as funcionalidades de gerenciamento de conteúdo. O Portal do Cliente é representado como um sistema externo, responsável pelo atendimento das funcionalidades da área do cliente.

\!\[Diagrama de casos de uso da ConAle\](../diagrama_de_casos_de_uso.png)

**UC07 \- Solicitar proposta e acessar canais de contato**

| Campo | Conteúdo |
| ----- | ----- |
| Ator principal | Visitante |
| Pré-condição | O visitante deve estar acessando o site institucional da ConAle. |
| Disparo | O visitante escolhe uma opção de contato ou atendimento disponibilizada pelo site. |
| Requisitos ligados | RF09, RF10, RF11, RNF01, RNF02, RNF04 |

**Fluxo principal:**

1. O visitante acessa a seção de contato ou uma área de conversão do site.  
2. O sistema apresenta os canais de contato disponibilizados pela ConAle.  
3. O visitante escolhe o canal de contato desejado, como WhatsApp ou e-mail.  
4. O sistema identifica o canal selecionado.  
5. O sistema direciona o visitante para o aplicativo, programa ou serviço externo correspondente.  
6. O visitante pode continuar o atendimento pelo canal externo escolhido.

**Fluxos alternativos:**

* A1, canal externo indisponível: caso o canal selecionado não esteja disponível, o sistema informa a indisponibilidade e apresenta os demais canais de contato disponíveis.  
* A2, visitante escolhe outro canal: caso o visitante não queira utilizar o canal inicialmente selecionado, pode retornar à página de contato e escolher outra opção.  
* A3, aplicativo externo não instalado: caso o visitante selecione um canal que dependa de aplicativo não instalado no dispositivo, o sistema direciona para a versão web do serviço, quando disponível, ou informa que o aplicativo é necessário.  
* A4, acesso direto ao e-mail: caso o visitante escolha o contato por e-mail, o sistema abre o programa de e-mail configurado no dispositivo com o endereço da ConAle preenchido.

Pós-condição: o visitante é direcionado ao canal externo de contato escolhido ou permanece na página de contato para selecionar outra opção.

**UC10 \- Gerenciar banners, textos e imagens da Home**

| Campo | Conteúdo |
| ----- | ----- |
| Ator principal | Administrador |
| Pré-condição | O administrador deve estar autenticado e autorizado a acessar o Painel Administrativo. |
| Disparo | O administrador acessa o gerenciamento de conteúdo da página inicial. |
| Requisitos ligados | RF13, RF14, RNF02, RNF05, RNF06 |

**Fluxo principal:**

1. O administrador acessa o Painel Administrativo.  
2. O sistema verifica se o administrador está autenticado e autorizado.  
3. O administrador acessa a área de gerenciamento da página inicial.  
4. O sistema apresenta os banners, textos e imagens atualmente cadastrados.  
5. O administrador escolhe o conteúdo que deseja cadastrar, editar ou remover.  
6. O administrador realiza a alteração desejada.  
7. O sistema valida as informações e os arquivos enviados.  
8. O sistema salva a alteração.  
9. O conteúdo atualizado fica disponível para exibição na página inicial do site.

**Fluxos alternativos:**

* A1, usuário não autenticado: caso uma pessoa tente acessar o Painel Administrativo sem autenticação, o sistema impede o acesso e solicita a autenticação.  
* A2, imagem acima do limite: caso uma imagem ultrapasse o limite definido para o sistema, o envio é recusado e o administrador é informado sobre o limite permitido.  
* A3, imagem com dimensões inválidas: caso a imagem não esteja nas dimensões definidas para o conteúdo, o sistema recusa o arquivo e informa ao administrador as dimensões esperadas.  
* A4, conteúdo inválido: caso algum campo obrigatório não seja preenchido, o sistema informa o erro e não salva a alteração.

Pós-condição: o conteúdo da página inicial é atualizado de acordo com a alteração realizada pelo administrador, sem necessidade de alteração direta no código-fonte.

**UC12 \- Interagir com o chatbot**

| Campo | Conteúdo |
| ----- | ----- |
| Ator principal | Visitante |
| Pré-condição | O visitante deve estar acessando o site institucional da ConAle e o chatbot deve estar disponível. |
| Disparo | O visitante abre o chatbot e envia uma pergunta ou seleciona uma opção de atendimento. |
| Requisitos ligados | RF17, RNF01, RNF02, RNF04 |

**Fluxo principal:**

1. O visitante acessa o site institucional da ConAle.  
2. O visitante abre a interface do chatbot.  
3. O sistema apresenta as opções de assuntos ou orientações disponíveis.  
4. O visitante seleciona uma opção ou envia uma pergunta.  
5. O sistema identifica o assunto ou pergunta informado.  
6. O sistema consulta as informações cadastradas para o chatbot.  
7. O sistema apresenta uma resposta relacionada à dúvida do visitante.  
8. O visitante pode realizar uma nova pergunta ou encerrar a interação.

**Fluxos alternativos:**

* A1, pergunta não cadastrada: caso o chatbot não possua uma resposta para a pergunta realizada, o sistema informa que não encontrou uma resposta e disponibiliza uma opção para contato com a ConAle pelo WhatsApp.  
* A2, opção inválida: caso o visitante selecione uma opção que não esteja disponível, o sistema informa que a opção não é válida e apresenta novamente as opções disponíveis.  
* A3, chatbot indisponível: caso o chatbot não esteja disponível, o visitante continua podendo utilizar os demais canais de contato disponibilizados pelo site.  
* A4, nova pergunta: caso o visitante faça outra pergunta após receber uma resposta, o sistema processa a nova solicitação sem encerrar a interação.

Pós-condição: o visitante recebe uma orientação relacionada à sua pergunta ou é direcionado para um canal de contato da ConAle quando o chatbot não possui a informação solicitada.

**Modelagem**

A modelagem do sistema representa os principais elementos envolvidos no funcionamento do site institucional da ConAle e do Painel Administrativo.

Para este projeto, serão apresentadas duas formas de modelagem:

* Diagrama de classes, representando os principais conceitos e seus relacionamentos no sistema.  
* Modelo de dados, representando as principais entidades que precisam ser armazenadas no banco de dados.

Os dois modelos estão relacionados às funcionalidades descritas nos requisitos funcionais e casos de uso, principalmente às funcionalidades de gerenciamento de conteúdo pelo Administrador.

**Diagrama de classes**

O diagrama de classes representa os principais objetos do sistema e os relacionamentos entre eles. Como o projeto possui uma área pública e um Painel Administrativo, o modelo concentra-se nos conteúdos que podem ser apresentados no site e administrados pela equipe da ConAle.

\!\[Diagrama de classes\](diagrama_de_classes.png)

**Modelo de dados**

O modelo de dados representa as informações persistentes utilizadas pelo sistema. O banco de dados deverá armazenar os usuários administrativos e os conteúdos que podem ser gerenciados pelo Painel Administrativo.

| Entidade | Finalidade |
| ----- | ----- |
| Administrador | Armazena os usuários autorizados a acessar o Painel Administrativo. |
| ConteudoHome | Armazena textos e imagens utilizados nas seções editáveis da página inicial. |
| Banner | Armazena os banners apresentados na página inicial. |
| Noticia | Armazena notícias e artigos publicados pela ConAle. |
| ClienteMarca | Armazena as marcas e logotipos de clientes e clínicas exibidos no site. |
| Chatbot | Representa a configuração geral do chatbot. |
| OpcaoChatbot | Armazena perguntas, opções e respostas disponibilizadas pelo chatbot. |

\!\[Modelo de dados\](modelo_de_dados.png)

**Decisões de implementação**

\- Autenticação e autorização do Painel Administrativo são verificadas no servidor, sempre. Esconder os menus de gerenciamento no front não é controle de acesso (ver RNF05).

\- Banners, textos e imagens ficam armazenados como conteúdo editável no banco de dados, e não fixos no código-fonte, permitindo atualização pelo Painel sem necessidade de novo deploy (ver RNF06).

\- O direcionamento para a Área do Cliente é um redirecionamento externo para o sistema já existente, sem duplicar ou reimplementar suas funcionalidades (ver RF12).

\- As respostas do chatbot são consultadas a partir das opções e perguntas cadastradas no Painel, e não fixas na interface, permitindo que o Administrador atualize o conteúdo sem alterar código (ver RF17, RNF06).

\- Marcas de clientes e clínicas exibidas no site vêm do cadastro feito pelo Administrador, e a remoção de uma marca no Painel reflete na página inicial sem necessidade de intervenção manual (ver RF15).

**Decisões de teste**

\- Acesso ao Painel Administrativo sem autenticação é bloqueado (RNF05). É o teste mais importante do sistema, pois protege o gerenciamento de conteúdo de acessos não autorizados.

\- Cadastro de conteúdo com campo obrigatório vazio é recusado (RF14).

\- Imagem enviada acima do limite definido é recusada com mensagem ao administrador (RF14).

\- Imagem com dimensões fora do padrão é recusada e o administrador é informado das dimensões esperadas (RF14).

\- Conteúdo publicado pelo Painel fica visível no site sem necessidade de alteração no código-fonte (RNF06).

\- Pergunta sem resposta cadastrada no chatbot direciona o visitante para o WhatsApp (RF17).

\- Seleção de opção inválida no chatbot reapresenta as opções disponíveis sem travar a interação (RF17).

\- Canal de contato indisponível não impede o visitante de acessar os demais canais (RF09, RF10).

\- Páginas principais carregam em até 3 segundos em condição adequada de conexão (RNF03).

\- Layout permanece legível e funcional em telas a partir de 360px (RNF01).

**Fora de escopo**

\- Emissão, cálculo ou envio de guias, impostos ou obrigações contábeis.

\- Substituição ou integração com o sistema já utilizado pela ConAle na Área do

Cliente.

\- Assinatura digital de documentos.

\- Aplicativo nativo. O site é web e responsivo (RNF01).

\- Pagamento ou cobrança de honorários dentro do site.

\- Atendimento contábil ou consultivo realizado pelo chatbot. Ele orienta e direciona o visitante, não substitui o atendimento humano (RF17).

\- Cadastro ou login de área exclusiva para clientes dentro do próprio site. O acesso segue sendo feito pelo sistema externo já existente (RF12).

**Glossário**

**Visitante:** pessoa que acessa o site institucional, cliente atual ou potencial.

**Administrador:** membro da equipe da ConAle responsável pela gestão do site.

**Área do Cliente:** sistema externo já usado pelo escritório, para o qual o site direciona.

**Painel Administrativo:** interface restrita usada pelo Administrador para gerenciar o conteúdo.

**Conteúdo da Home:** banners, textos e imagens editáveis da página inicial.

**Marca/Cliente atendido:** logotipo de cliente ou clínica exibido como credencial no site.

**Credencial:** entidade, associação ou marca que reforça a autoridade da ConAle.

**Chatbot:** assistente virtual do site que orienta o visitante com respostas pré-cadastradas.

**Opção do chatbot:** pergunta e resposta cadastrada no Painel para uso do chatbot.

