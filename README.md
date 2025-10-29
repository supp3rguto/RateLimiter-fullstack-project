# 🚀 Microsserviço de Rate Limiter com Java, Spring Boot e MySQL

Este projeto é um **microsserviço de backend robusto** que implementa um **controle de taxa (rate limiting)** dinâmico e configurável, demonstrando uma solução de engenharia de software *vertical* e pronta para um ambiente de produção SaaS.

O frontend (em **React**) serve como um **Dashboard de Simulação** interativo para visualizar e testar a lógica do backend em tempo real.


## 🎥 Demonstração em Ação

obs: gravar um gif do projeto


## 🎯 O Problema de Mercado

Em qualquer arquitetura de **API pública** ou modelo **SaaS (Software as a Service)**, é crucial proteger os endpoints contra abuso, uso excessivo ou ataques de negação de serviço (DoS).

Este projeto resolve dois problemas centrais:

- **Estabilidade:** Impedir que um único usuário faça milhares de requisições e derrube o serviço para todos os outros.  
- **Modelo de Negócio:** Criar diferentes *planos* (ex: *Free*, *Pro*) com limites de uso distintos para cada cliente.



## 🛠️ Stack Tecnológica

### Backend
- Java 21  
- Spring Boot (Web, Data JPA, AOP)  
- MySQL  

### Frontend (Dashboard)
- React (Vite)  
- Axios  

### Build
- Maven  


## ✨ Destaques de Engenharia (A Complexidade "Oculta")

O valor deste projeto não está no que o frontend mostra, mas nas **decisões de arquitetura** por trás dele.


### 1. Arquitetura Limpa com Programação Orientada a Aspectos (AOP)

A lógica de verificação de limite — um *cross-cutting concern* — **não polui o Controller**.  
A verificação é feita de forma **declarativa e desacoplada** usando AOP.

- Criada a anotação customizada `@RateLimited(endpointName = "...")`.  
- O `RateLimiterAspect` intercepta métodos anotados, executando a verificação antes da execução do endpoint.  

**Resultado:**  
O `ApiController` permanece limpo e focado apenas em suas rotas e dados, enquanto o *Aspecto* cuida da segurança.


### 2. Regras Dinâmicas (Pronto para SaaS)

As regras de limite **não estão hardcoded** no código Java — são lidas do **banco de dados**.  
Isso permite que o time de negócios altere os planos de clientes sem novo deploy do backend.

**Estrutura do banco:**
- `plans`: Define planos (ex: *Free*, 10 tokens, 60s de recarga).  
- `api_keys`: Associa uma chave de API a um plano.  
- `endpoint_costs`: Define quantos tokens uma chamada específica consome (ex: `"create_resource"` custa 5 tokens).


### 3. Resiliência e Integridade de Dados

O backend foi projetado para **ambientes concorrentes (multi-thread)**.

- **Atomicidade (ACID):**  
  O método `RateLimitingService.tryConsumeToken()` é anotado com `@Transactional`.  
  Isso garante que a leitura e atualização de tokens sejam **atômicas**, evitando *race conditions* (ex: múltiplos requests consumindo o mesmo token).

- **Tratamento de Erros:**  
  Retornos HTTP padronizados e semânticos:
  - `401 Unauthorized`: Chave `X-API-KEY` ausente ou inválida.  
  - `429 Too Many Requests`: Chave válida, mas limite de tokens excedido.


## 🚀 Como Executar o Projeto Localmente

### 🧩 Pré-requisitos
- Java 21+ (JDK)  
- MySQL 8.0+  
- Node.js 18+


### 🖥️ 1. Backend (Java)

1. Clone o repositório.  
2. Abra o **MySQL Workbench** (ou cliente de sua preferência).  
3. Execute o script, e criará o banco `ratelimit_db` e populará as tabelas.  :
   
```
RateLimite-backend/sql/setup.sql
````

4. Abra o projeto `RateLimite-backend` no IntelliJ (ou IDE preferida).  
5. Atualize o arquivo `src/main/resources/application.properties` com sua senha do MySQL:

```properties
spring.datasource.password=SUA_SENHA_ROOT_AQUI
````

6. Rode a classe `RateLimiteBackendApplication.java`.
   O servidor iniciará na porta **8080**.


### 💻 2. Frontend (React)

1. Abra um terminal e vá até a pasta:

   ```bash
   cd frontend-react
   ```
2. Instale as dependências:

   ```bash
   npm install
   ```
3. Inicie o servidor de desenvolvimento:

   ```bash
   npm run dev
   ```
4. Acesse o projeto em:

   ```
   http://localhost:5173
   ```


## 👨‍💻 Autor

**Augusto Ortigoso Barbosa**

* **GitHub:** [github.com/supp3rguto](https://github.com/supp3rguto)
* **LinkedIn:** [linkedin.com/in/augusto-barbosa-769602194](https://www.linkedin.com/in/augusto-barbosa-769602194)
