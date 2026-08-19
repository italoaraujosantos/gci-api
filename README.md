# Gestor de Carteira de Investimentos — API

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de uma carteira de investimentos.

A aplicação permite cadastrar, consultar, atualizar e excluir ativos de uma carteira, armazenando as informações em um banco de dados **MySQL**.

O projeto faz parte de uma aplicação Full-Stack composta por:

* Frontend HTML/CSS/JavaScript
* API REST Spring Boot
* Banco de dados MySQL
* API pública de cotações financeiras

---

## Objetivo

O objetivo do projeto é desenvolver uma aplicação capaz de gerenciar os ativos de uma carteira de investimentos.

O frontend consulta uma API pública de cotações para obter informações sobre os ativos negociados na B3 e, posteriormente, utiliza a API REST desenvolvida em Spring Boot para armazenar essas informações no banco de dados MySQL.

O sistema permite realizar as operações:

* Cadastro de ativos
* Consulta dos ativos
* Atualização dos ativos
* Exclusão dos ativos

---

## Tecnologias

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Bean Validation
* MySQL
* Maven
* REST API
* HTML
* CSS
* JavaScript
* Fetch API
* Docker

---

## Arquitetura

```text
                 ┌──────────────────────┐
                 │     HG Brasil API    │
                 │   Cotações B3        │
                 └──────────┬───────────┘
                            │
                            │ HTTPS
                            ▼
┌──────────────────┐   HTTP/REST   ┌──────────────────────┐
│                  │──────────────▶│                      │
│    Frontend      │               │     Spring Boot      │
│ HTML/CSS/JS      │◀──────────────│       REST API       │
│                  │               │                      │
└──────────────────┘               └──────────┬───────────┘
                                              │
                                              │ JPA
                                              ▼
                                    ┌──────────────────────┐
                                    │        MySQL         │
                                    │   gestor_carteira    │
                                    └──────────────────────┘
```

---

## Entidade

A aplicação possui a entidade `Ativo`.

Principais atributos:

| Campo          | Tipo       | Descrição              |
| -------------- | ---------- | ---------------------- |
| id             | Long       | Identificador do ativo |
| ticker         | String     | Código do ativo na B3  |
| nome           | String     | Nome do ativo          |
| quantidade     | Integer    | Quantidade de ativos   |
| precoCompra    | BigDecimal | Preço de compra        |
| precoAtual     | BigDecimal | Cotação atual          |
| valorInvestido | BigDecimal | Valor total investido  |
| valorAtual     | BigDecimal | Valor atual da posição |

A entidade é mantida de forma plana, sem relacionamentos complexos com outras entidades.

---

## Endpoints

A API está disponível, por padrão, em:

```text
http://localhost:8080
```

### Listar todos os ativos

```http
GET /api/ativos
```

Exemplo:

```bash
curl http://localhost:8080/api/ativos
```

---

### Buscar ativo por ID

```http
GET /api/ativos/{id}
```

Exemplo:

```bash
curl http://localhost:8080/api/ativos/1
```

---

### Buscar ativo por ticker

```http
GET /api/ativos/ticker/{ticker}
```

Exemplo:

```bash
curl http://localhost:8080/api/ativos/ticker/PETR4
```

---

### Cadastrar ativo

```http
POST /api/ativos
```

Body:

```json
{
    "ticker": "PETR4",
    "nome": "Petrobras",
    "quantidade": 100,
    "precoCompra": 35.50,
    "precoAtual": 38.20
}
```

---

### Atualizar ativo

```http
PUT /api/ativos/{id}
```

Exemplo:

```http
PUT /api/ativos/1
```

Body:

```json
{
    "ticker": "PETR4",
    "nome": "Petrobras",
    "quantidade": 120,
    "precoCompra": 35.50,
    "precoAtual": 38.20
}
```

---

### Excluir ativo

```http
DELETE /api/ativos/{id}
```

Exemplo:

```bash
curl -X DELETE http://localhost:8080/api/ativos/1
```

---

# Banco de dados

## MySQL

O projeto utiliza o banco:

```text
gestor_carteira
```

Caso o banco seja criado manualmente, execute:

```sql
CREATE DATABASE gestor_carteira;
```

Depois configure as credenciais do banco no arquivo:

```text
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestor_carteira
spring.datasource.username=carteira
spring.datasource.password=carteira

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Altere `username` e `password` de acordo com a configuração do seu ambiente.

---

# MySQL com Docker

Também é possível executar o MySQL utilizando Docker.

Exemplo:

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: gestor-carteira-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: gestor_carteira
      MYSQL_USER: carteira
      MYSQL_PASSWORD: carteira
    ports:
      - "3306:3306"
```

Execute:

```bash
docker compose up -d
```

Verifique o container:

```bash
docker ps
```

Para interromper:

```bash
docker compose down
```

---

# API pública de cotações

O projeto utiliza a API pública da **HG Brasil** para obter informações de ativos financeiros.

Exemplo de consulta:

```javascript
const url = new URL(
    "/v2/finance/quotes",
    "https://api.hgbrasil.com"
);

url.searchParams.set("tickers", "B3:PETR4");
url.searchParams.set("key", "SUA_CHAVE");

const response = await fetch(url.href);
const data = await response.json();
```

A consulta utiliza o ticker:

```text
B3:PETR4
```

A aplicação frontend utiliza os dados retornados pela API externa para auxiliar no preenchimento das informações do ativo.

Depois disso, os dados são enviados para esta API REST e armazenados no MySQL.

---

# Fluxo da aplicação

```text
1. Usuário informa o ticker
            ↓
2. Frontend consulta HG Brasil
            ↓
3. Cotação do ativo é retornada
            ↓
4. Usuário informa a quantidade
            ↓
5. Frontend envia POST para Spring Boot
            ↓
6. Spring Boot valida os dados
            ↓
7. Spring Boot salva no MySQL
            ↓
8. Frontend atualiza a carteira
```

---

# Validações

A API possui validações para evitar o armazenamento de informações inválidas.

Exemplos:

* Ticker não pode ser vazio
* Nome não pode ser vazio
* Quantidade deve ser maior que zero
* Preço de compra deve ser informado
* Dados obrigatórios não podem ser `null`

---

# Tratamento de erros

A aplicação possui tratamento para situações como:

* Ativo não encontrado
* Dados inválidos
* ID inexistente
* Erros de validação
* Erros durante as operações de persistência

---

# Como executar o projeto

## Pré-requisitos

Instale:

* Java
* Maven
* Docker
* Docker Compose
* MySQL, caso não utilize Docker

Verifique o Java:

```bash
java -version
```

Verifique o Maven:

```bash
mvn -version
```

Verifique o Docker:

```bash
docker --version
```

---

## 1. Clonar o projeto

```bash
git clone <URL_DO_REPOSITORIO>
```

Entrar no diretório:

```bash
cd gestor-carteira-api
```

---

## 2. Iniciar o MySQL

Com Docker:

```bash
docker compose up -d
```

---

## 3. Configurar o banco

Verifique o arquivo:

```text
src/main/resources/application.properties
```

Configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestor_carteira
spring.datasource.username=carteira
spring.datasource.password=carteira
```

---

## 4. Executar a aplicação

Com Maven:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

---

# Testando a API

Depois de iniciar o projeto, pode ser utilizado o Postman, Insomnia ou `curl`.

### GET

```bash
curl http://localhost:8080/api/ativos
```

### POST

```bash
curl -X POST http://localhost:8080/api/ativos \
-H "Content-Type: application/json" \
-d '{
    "ticker": "PETR4",
    "nome": "Petrobras",
    "quantidade": 100,
    "precoCompra": 35.50,
    "precoAtual": 38.20
}'
```

### PUT

```bash
curl -X PUT http://localhost:8080/api/ativos/1 \
-H "Content-Type: application/json" \
-d '{
    "ticker": "PETR4",
    "nome": "Petrobras",
    "quantidade": 120,
    "precoCompra": 35.50,
    "precoAtual": 38.20
}'
```

### DELETE

```bash
curl -X DELETE http://localhost:8080/api/ativos/1
```

---

# Estrutura do projeto

```text
src/
└── main/
    ├── java/
    │   └── br/com/italo/carteira/
    │       ├── controller/
    │       │   └── AtivoController.java
    │       │
    │       ├── entity/
    │       │   └── Ativo.java
    │       │
    │       ├── repository/
    │       │   └── AtivoRepository.java
    │       │
    │       └── service/
    │           └── AtivoService.java
    │
    └── resources/
        └── application.properties
```

---

# Repositório do Frontend

O frontend da aplicação possui um repositório separado e é responsável pela interface gráfica, consumo da API pública de cotações e comunicação com esta API REST.

```text
Frontend:
<URL_DO_REPOSITORIO_FRONTEND>
```

---

# Repositório Backend

```text
Backend:
<URL_DO_REPOSITORIO_BACKEND>
```

---

# Status do projeto

Em desenvolvimento.

## Funcionalidades

* [x] Estrutura inicial da API
* [ ] Integração com MySQL
* [ ] Cadastro de ativos
* [ ] Consulta de ativos
* [ ] Atualização de ativos
* [ ] Exclusão de ativos
* [ ] Integração com HG Brasil
* [ ] Interface frontend
* [ ] Tratamento de erros
* [ ] Validações
* [ ] Documentação completa

---

## Projeto acadêmico

Projeto desenvolvido como atividade final do curso de Desenvolvimento Java, utilizando Spring Boot, MySQL, HTML/CSS/JavaScript e integração com API pública.
