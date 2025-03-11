# Estoque API
Sistema para Gerenciamento de Estoque.

## 🚀 Problema Resolvido
- Ajuda empresas a gerenciar seu estoque de produtos de forma eficiente.
- Facilita o controle de estoque, através de um monitoramento prático e intuitivo.
-Fácil Acesso ao banco de dados, permitindo buscas por nome ou número da transação
-Funcionalidades integradas a API's externas de Geolocalização e Currency.
- Proporciona integração com plataformas de e-commerce para sincronização de dados.

🛠 Tecnologias Integradas 
- **Backend:** Java, Spring Boot, Hibernate, Maven  
- **Frontend:** React, Axios, CSS  
- **Database:** Advanced SQL, MySQL WorkBench  
- **Cloud & Infrastructure:** AWS (EC2, LoadBalancer, SSL Certificates)  
- **Netlify:** CI/CD, CDN, DNS Manager
- 
## 📂 Funcionalidades
- Cadastro, edição e exclusão de produtos.
- Controle de entrada e saída de estoque.
- Informações detalhadas sobre produtos críticos e movimentações.
- Integração com APIs externas para sincronização simulando e-commerce.
-Possibilidade de serviços integrados como cálculo de frete e análise de preço em diferentes moedas. 


## 🧗 Desafios Enfrentados
- **Integração com APIs externas:** Gerenciar os erros de comunicação entre o sistema e as plataformas de e-commerce foi um desafio que resolvi utilizando tratamento de exceções customizado.
- **Autenticação JWT:** Configurar permissões e CORS para permitir que o frontend acessasse os endpoints protegidos exigiu ajustes detalhados. Foi uma tarefa que me trouxe compreensao
sobre a  implementação de segurança através de tokens(JWT), envolvendo a criação, validação e autenticação desses tokens.
- **Integrar o BackEnd com o FrontEnd utilizando basicamente o React, me trouxe a compreensão sobre como traduzir as funcionalidades entregadas pelo BackEnd, de uma forma prática
e intuitiva, para que o Usuário possa utilizar a API de forma confortável, e tenha as informações disponíveis de forma rápida. 

🌟 Resultados
Gestão Eficiente: Melhorou o controle de estoque, reduzindo perdas e otimizando o abastecimento.
Automação de Processos: Facilitou a sincronização de dados com plataformas de e-commerce.
Segurança : Sistema de segurança com Tokens JWT implementado, exigindo que o Usuário tenha a permissão necessária para acessar  partes protegidas da aplicação. 
Usabilidade: Interface amigável no Swagger e no FrontEnd para explorar a API e endpoints bem documentados.


## 🛠️ Como Rodar o Projeto
1. Clone o repositório:
   ```bash
https://github.com/NemoBJJ/Estoque-API

2. Instale as dependencias : mvn install

3. Execute o BackEnd : mvn spring-boot:run

4. Inicie o FrontEnd  : npm start

Swagger : http://localhost:8083/swagger-ui/index.html

