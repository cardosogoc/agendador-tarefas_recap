# 📋 Microsserviço de Agendamento de Tarefas

Microsserviço responsável pelo **gerenciamento das tarefas e agendamentos** da aplicação.

Este serviço faz parte de uma arquitetura baseada em **microsserviços**, sendo consumido principalmente pelo **BFF Agendador**, que centraliza a comunicação entre o cliente e os serviços da aplicação.

## 🛠️ Tecnologias

* ☕ Java
* 🌱 Spring Boot
* 📦 Gradle
* 🧩 Lombok
* 📄 DTOs
* 🗄️ MongoDB
* 📚 Swagger / OpenAPI
* 🐳 Docker
* 📊 SonarQube

## 📌 Responsabilidades

* Cadastro de tarefas
* Consulta de tarefas
* Atualização de tarefas
* Exclusão de tarefas
* Alteração do status das tarefas
* Gerenciamento dos agendamentos

## 🏗️ Arquitetura

O microsserviço faz parte do seguinte fluxo:

```text
                    ┌─────────────────┐
                    │     Cliente     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  BFF Agendador  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Microsserviço   │
                    │     Tarefas     │
                    └─────────────────┘
```

O cliente não precisa acessar diretamente este microsserviço. O **BFF Agendador** funciona como ponto de entrada da aplicação.

## 🐳 Docker

O serviço pode ser executado individualmente ou em conjunto com os demais microsserviços através do Docker Compose.

## 📖 Documentação

A documentação completa da arquitetura, integração entre os serviços e instruções para execução pode ser encontrada no projeto principal:

👉 **[BFF Agendador](https://github.com/cardosogoc/bff-agendador)**

## 🔗 Projeto

* **BFF Agendador:** https://github.com/cardosogoc/bff-agendador
* **Este microsserviço:** https://github.com/cardosogoc/agendador-tarefas_recap

## 👨‍💻 Autor

**Gabriel Cardoso**

Projeto desenvolvido para estudos e evolução prática em **desenvolvimento Backend Java e Spring Boot**.
