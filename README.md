# 🛡️ Rate Limiter Dinâmico com Spring Boot e AOP

<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" />
  <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black" />
  
</p>

Este projeto é um **microsserviço de backend robusto** que implementa um **controle de taxa (rate limiting)** dinâmico e configurável, demonstrando uma solução de engenharia de software *vertical* e pronta para um ambiente de produção SaaS. O frontend (em **React**) serve como um **Dashboard de Simulação** interativo para visualizar e testar a lógica do backend em tempo real.

## 🎓 Contexto e Justificativa

Em qualquer arquitetura de **API pública** ou modelo **SaaS (Software as a Service)**, é crucial proteger os endpoints contra abuso, uso excessivo ou ataques de negação de serviço (DoS).

Este projeto resolve dois problemas centrais:

  * **Estabilidade e Resiliência:** Impedir que um único usuário faça milhares de requisições e sobrecarregue o serviço para todos os outros clientes.
  * **Modelo de Negócio (Monetização):** Criar diferentes *planos* (ex: *Free*, *Pro*, *Enterprise*) com limites de uso distintos para cada cliente, permitindo a monetização da API.

## ✨ Funcionalidades e Destaques de Engenharia

O valor deste projeto reside nas **decisões de arquitetura** que o tornam escalável e pronto para ambientes concorrentes.

  * **Regras Dinâmicas (SaaS Ready):** As regras de limite (tokens, tempo de recarga) **não estão hardcoded** — são lidas do banco de dados (MySQL), permitindo que o time de negócios altere planos de clientes sem novo deploy do backend.
  * **Arquitetura Limpa com AOP:** A lógica de verificação de limite é um *cross-cutting concern* e é tratada de forma **declarativa e desacoplada** usando **Programação Orientada a Aspectos (AOP)**.
      * Uma anotação customizada (`@RateLimited`) é usada para marcar o `Controller`.
      * Um `RateLimiterAspect` intercepta e executa a verificação antes de qualquer lógica de negócio.
  * **Atomicidade e Concorrência:** O método de consumo de tokens é anotado com `@Transactional`, garantindo que a leitura e atualização de tokens sejam **atômicas**. Isso previne *race conditions* em ambientes multi-thread.
  * **Tratamento de Erros Semântico:** Retornos HTTP padronizados e informativos:
      * `401 Unauthorized`: Chave `X-API-KEY` ausente ou inválida.
      * `429 Too Many Requests`: Chave válida, mas limite de tokens excedido.

## 📸 Galeria do Sistema



## 🚀 Arquitetura e Tecnologias

A solução é dividida em um microsserviço (backend) responsável pela lógica de rate limiting e um painel de visualização (frontend).

### ☕ Backend (Java/Spring Boot)

O backend é a fonte da verdade para o estado do Rate Limiter.

  * **Java 21 & Spring Boot:** Plataforma robusta para o microsserviço.
  * **Spring Data JPA:** Para persistência e transações atômicas com o banco de dados.
  * **Spring AOP:** Uso fundamental para desacoplar a lógica de segurança (Rate Limiting) da lógica de negócio (Controller).
  * **MySQL:** Armazenamento centralizado e relacional das regras de Rate Limit e associação de chaves.

### ⚛️ Frontend (React)

O frontend simula um cliente consumindo a API.

  * **React (Vite):** Dashboard reativo e de alta velocidade para testes.
  * **Axios:** Para chamadas assíncronas ao backend.
  * **Hooks de Estado:** Para visualização em tempo real da contagem de tokens após cada requisição.

## 💻 Como Executar o Projeto Localmente

### 🧩 Pré-requisitos

  * **Java 21+** (JDK)
  * **MySQL 8.0+**
  * **Node.js 18+**

### 🖥️ 1. Configuração do Backend (Java/MySQL)

1.  Clone o repositório.

2.  Abra seu cliente MySQL (Workbench, DBeaver, etc.) e execute o script de setup:

    ```
    RateLimite-backend/sql/setup.sql
    ```

    > Este script criará o banco `ratelimit_db` e populará as tabelas de `plans`, `api_keys` e `endpoint_costs`.

3.  No projeto `RateLimite-backend`, atualize o arquivo de propriedades com sua senha do banco:

    ```properties
    # src/main/resources/application.properties
    spring.datasource.password=SUA_SENHA_ROOT_AQUI
    ```

4.  Rode a classe `RateLimiteBackendApplication.java` para iniciar o servidor na porta **8080**.

### 💻 2. Configuração do Frontend (React)

1.  Abra o terminal e navegue até a pasta:
    ```bash
    cd frontend-react
    ```
2.  Instale as dependências e inicie:
    ```bash
    npm install
    npm run dev
    ```
3.  Acesse o projeto em `http://localhost:5173`.

## 👨‍💻 Autor

**Augusto Ortigoso Barbosa**

  * **GitHub:** [github.com/supp3rguto](https://github.com/supp3rguto)
  * **LinkedIn:** [linkedin.com/in/augusto-barbosa-769602194](https://www.linkedin.com/in/augusto-barbosa-769602194)
