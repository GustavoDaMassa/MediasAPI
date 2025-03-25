<h1 align="center">
  Médias API
</h1>

# 📌 API Rest de Gestão de Notas e Projeções Acadêmicas

## 🎯 Apresentação

Esta API foi desenvolvida para oferecer uma solução completa na gestão de notas acadêmicas. Com ela, os usuários podem armazenar suas avaliações e obter automaticamente a média final de cada disciplina. Ela foi idealizada com o objetivo core de permitir projeções estratégicas para um melhor planejamento do desempenho acadêmico.

A característica exclusiva desta API está na personalização do método de cálculo em cada disciplina. Onde é possível definir regras próprias para o cálculo da média, tornando o sistema adaptável a diferentes abordagens. Além disso, a API calcula automaticamente a pontuação necessária para atingir a nota de corte.

Tanto estudantes quanto  docentes podem utilizá-la para gerenciar notas, criar projeções para cada aluno ou cenário e realizar o lançamento de notas.

---

## Principais funcionalidades:

- Armazene notas de forma estruturada e eficiente.
- Defina métodos personalizados para cálculo de médias.
- Obtenha automaticamente a pontuação necessária para alcançar metas acadêmicas.
- Simule diferentes cenários para planejamento estratégico.
- Gerencie disciplinas.

---
## Utilizando a API: 
 #### Antes de detalharmos os endpoints e a autenticação, é importante entender o fluxo de uso da API:

- **Login**: O usuário cria seu perfil se cadastrando no sistema.

- **Autenticação**: A API valida as credenciais e, se bem-sucedida, retorna um token JWT.
- **Disciplinas**: o usuário pode criar e editar suas disciplinas personalizadas.
- **Projeções**: Uma projeção é criada automaticamente na definição do método de cálculo no passo anterior. Usuários podem criar, editar e visualizar outras projeções.
- **Avaliações**: Também criadas automaticamente a cada projeção. Usuários podem lançar notas.

 #### caso deseje compilar e navegar por si só no sistema siga para [como executar](#como-executar)

---

## Autenticação (JSON Web Token)

 Para obter um token, o usuário deve fazer uma requisição POST para o endpoint `/authenticate` com suas credenciais. O token retornado deve ser incluído no cabeçalho Authorization de todas as requisições subsequentes, exceto para cadastro.     
 Auth Type : **Bearer Token**

![img.png](img.png)

![img_1.png](img_1.png)

![img_2.png](img_2.png)

![img_3.png](img_3.png)

![img_4.png](img_4.png)

![img_5.png](img_5.png)

![img_6.png](img_6.png)

![img_7.png](img_7.png)

---
## API Endpoints

### user-controller


1. **Criar Tarefa**
    - **Método:** `POST`
    - **Endereço:** `/tasks`
    - **Request Body:**
   ```json
   {
       "name": "Criar tarefa",
       "description": "Requisição web",
       "done": false,
       "priority": 3
   }


#### 2. **Listar Tarefas**
- **Método:** `GET`
- **Endereço:** `/tasks`
- **Descrição:** Retorna uma lista de todas as tarefas cadastradas.
- **Exemplo de Resposta:**
```json
[
  {
    "id": 18,
    "name": "Agenda-Desafio",
    "description": "Construir a API do zero",
    "done": true,
    "priority": 10
  },
  {
    "id": 4,
    "name": "Estudar",
    "description": "Implementação do swagger para documentar os end-points",
    "done": true,
    "priority": 6
  },
  {
    "id": 19,
    "name": "Treinar",
    "description": "Academia",
    "done": true,
    "priority": 4
  }
]
```


#### 3. **Atualizar Tarefa**
- **Método:** `PUT`
- **Endereço:** `/tasks/{id}`
- **Descrição:** Atualiza as informações de uma tarefa existente, utilizando o ID como parâmetro.
- **Request Body:**
```json
{
    "name": "Nova tarefa",
    "description": "Requisição web",
    "done": false,
    "priority": 3
}
```

#### 4. **Remover Tarefa**
- **Método:** `DELETE`
- **Endereço:** `/tasks/{id}`
- **Descrição:** Remove uma tarefa da lista, utilizando o ID como parâmetro. Retorna a lista atualizada de tarefas após a exclusão.

## Tecnologias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Mysql](https://dev.mysql.com/downloads/)
- [Workbench](https://www.mysql.com/products/workbench/)
- [Postman](https://postman.com/)
- [Git](https://git-scm.com/)
- [H2 DataBase](https://www.h2database.com/html/main.html)
- [Bean Validation](https://beanvalidation.org/)


## Práticas adotadas

- SOLID
- API REST
- Consultas com Spring Data JPA
- Injeção de Dependências
- Tratamento de respostas de erro
- Geração automática do Swagger com a OpenAPI 3
- Testes automatizados
- Banco de dados relacional

## Como executar

- Clonar repositório git

```
git clone https://github.com/GustavoDaMassa/AgendaToDo.git
```

- Construir o projeto:
```
$ ./mvnw clean package
```
- Executar a aplicação:
```
$ java -jar target/Agenda-0.0.1-SNAPSHOT.jar
```
### Usando Docker

- Clonar repositório git
- Construir o projeto:

- Construir a imagem:
```
./mvnw spring-boot:build-image
```
- Executar o container:
```
docker run --name place-service -p 8080:8080  -d place-service:0.0.1-SNAPSHOT
```

A API poderá ser acessada em http://localhost:8080/tasks.

### [swagger do projeto](http://localhost:8080/swagger-ui.html)
