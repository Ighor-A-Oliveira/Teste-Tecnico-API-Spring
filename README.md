# Teste Técnico Para Estagio

API REST de carteira digital desenvolvida como teste técnico para processo de estagio.

[![Java 21](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring_Boot-3.3+-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)

## Funcionalidades implementadas
- Cadastro de usuários com CPF, nome, email e senha criptografada (BCrypt)
- Login com retorno de JWT (access + refresh token com rotação)
- Perfis: USER (padrão) e ADMIN (configurável no banco)
- Criação de conta digital por usuário: número gerado automaticamente, saldo inicial 0, data de criação e status (ativa/inativa)
- Transações internas: depósito (positivo), saque (sem negativo), transferência interna (validação de saldo e conta destino)
- Transações externas: transferência simulada com validação de saldo, consumo da API pública de bancos[](https://brasilapi.com.br/api/banks/v1) para validar código de banco
- Histórico completo de transações: tipo, valor, contas origem/destino, timestamp, saldo pós-operação
- Auditoria: logs estruturados em console (com usuário, endpoint, data/hora, payload) — pode ser extendido para arquivo ou banco
- Segurança: JWT para autenticação, criptografia de senha, validações de entrada (valores >0, formatos de CPF, etc.)

## Pré-requisitos
| Ferramenta   | Versão mínima | Comando de verificação |
|--------------|---------------|-------------------------|
| Java JDK     | 21            | `java -version`        |
| Maven        | 3.8+         | `mvn -v`               |
| PostgreSQL   | 15+           | `psql --version`       |
| Docker (opcional) | Latest      | `docker --version`     |

## Configuração do banco (execute uma única vez)
```sql
CREATE DATABASE security_test;
CREATE USER spring_user WITH PASSWORD 'strongpassword';
GRANT ALL PRIVILEGES ON DATABASE security_test TO spring_user;
```

## Como rodar
- git clone https://github.com/Ighor-A-Oliveira/Teste-Tecnico-API-Spring.git
- cd Teste-Tecnico-API-Spring
- Va em application.properties e veja se tem tem a configuração spring.datasource.url=jdbc:postgresql://localhost:5432/security_test
- mvn clean install
- mvn spring-boot:run
> **Aplicação disponível em: http://localhost:8080*  


## Endpoints da API 

| Método | Rota                                   | Descrição                              | Auth     |
|--------|----------------------------------------|----------------------------------------|----------|
| **POST** | `/user/register`                     | Cadastra usuário                       | Pública  |
| **POST** | `/user/login`                        | Login → retorna JWT + refresh token    | Pública  |
| **POST** | `/account/register`                  | Cria conta bancária (Bom fazer logo apos criar Usuario)                   | JWT      |
| **POST** | `/transaction/deposit`               | Depósito                               | JWT      |
| **POST** | `/transaction/withdraw`              | Saque                                  | JWT      |
| **POST** | `/transaction/internal-transfer`     | Transferência interna                  | JWT      |
| **POST** | `/transaction/external-transfer`     | Transferência externa (simulada)       | JWT      |

> **Header obrigatório nas rotas protegidas**  
> `Authorization: Bearer <token>`

## Exemplos de payload

### Cadastro de usuário
```json
{
  "name": "ighor",
  "email": "usuario@email.com",
  "password": "senha123",
  "cpf": "12345678901"
}
```

### Cadastro de Conta (Precisa ser feito logo apos criacao do Usuario)
```json
{
  "cpf": "12345678901"
}
```

### Depósito ou Saque
```json
{
  "fromAccountId": 1,
  "amount": 100.0
}
```

### Transferência interna
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 50.0
}
```

### Transferencia Externa
```json
{
  "fromAccountId": 2,   
  "amount": 250.00,                
  "toBankCode": "001",             
  "toAgency": "0001",              
  "toAccountNumber": 987654,     
  "toAccountHolderCpf": "11111111111" 
}
```

## Rodando com Docker Compose (docker-compose.yml já incluso dentro do repositorio)


```bash
git clone https://github.com/Ighor-A-Oliveira/Teste-Tecnico-API-Spring.git
cd Teste-Tecnico-API-Spring
Va em application.properties e veja se tem tem a configuração spring.datasource.url=jdbc:postgresql://db:5432/security_test

# Sobe banco + API em containers
docker-compose up -d --build

# Para parar
docker-compose down
```

> **Aplicação disponível em: http://localhost:8080*  



