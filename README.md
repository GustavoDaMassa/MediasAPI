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

![img_2.png](images/img_2.png)

- **O usuário é autenticado através do email que é único no sistema, rota pública;**
- **Auth Type:** `Bearer Token`
- **Request Body:**
  ```json
   {
        "email":"gustavo.pereira@discente.ufg.br",
        "password":"aula321"
   }
---
## API Endpoints

### user-controller

![img.png](img.png)
- **Cria um novo perfil de usuário, não é necessário autenticação.** 

- **Request Body:**
   ```json
   {
      "name":"Gustavo Henrique",
      "email":"gustavo.pereira@discente.ufg.br",
      "password":"aula321"
   }
- **Response**
  ```json
   {
       "id": 2,
       "name": "Gustavo Henrique",
       "email": "gustavo.pereira@discente.ufg.br"
   }
  

![img_2.png](img_2.png)
- **Retorna uma lista com todos os usuários e seus respectivos id's que serão usados como parâmetros em demais requisições;**
- **Response**
  ```json
    [
        {
            "id": 2,
            "name": "Gustavo Henrique",
            "email": "gustavo.pereira@discente.ufg.br"
        }
    ]

![img_1.png](img_1.png)
- **Parâmetro**: `id` - id do usuário;
- **Request Body:**
   ```json
    {
        "string": "Gustavo"
    }

- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavo.pereira@discente.ufg.br"
    }

![img_3.png](img_3.png)

- **Parâmetro**: `id` - id do usuário;
- **Request Body:**
   ```json
    {
        "email":"gustavohenrique3gb@gmail.com"
    }

- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavohenrique3gb@gmail.com"
    }

![img_4.png](img_4.png)

- **Parâmetro**: `id` - id do usuário;
- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavohenrique3gb@gmail.com"
    }

### course-controller

![img_5.png](img_5.png)

- **Cria um novo curso, e através do método de cálculo das médias cria automaticamente uma projeção com o mesmo nome, identificando e instânciando as avaliações definidas.**
- **Parâmetro**: `userId` - id do usuário;
- **Request Body:**
   ```json
    {
        "name":"SGBD",
        "averageMethod":"(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0,6*(AV1+AV2[10]/2))",
        "cutOffGrade": 6.0
    }

- **AverageMethod funcionalidades:**
  - Constantes são representadas por valores `double`;
  - Identificadores podem conter números, e devem conter uma ou mais letras;
  - Identificadores podem ter o sufixo `[n]` indicando a nota máxima da avaliação, **n** é um `double`, caso não seja informado assume o valor default `10`;
  - `@M[n](i1;i2;...;im)` é uma funcionalidade extra além das operações aritméticas, ela faz o somatório das n maiores notas fornecidas entres os parênteses, `m>=n` ;
  - `cutOffGrade` também é opcional, valor default `6.0`

- **Response**
  ```json
    {
        "id": 2,
        "name": "SGBD",
        "averageMethod": "(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0,6*(AV1+AV2[10]/2))",
        "cutOffGrade": 6
}

![img_6.png](img_6.png)

- **Lista todos os cursos do usuário.**
- **Parâmetro**: `userId` - id do usuário;
- **Response**
  ```json
    [
       {
          "id": 1,
          "name": "BD 1",
          "averageMethod": "(P2+P3*2)/3",
          "cutOffGrade": 6
       },
       {
          "id": 2,
          "name": "SGBD",
          "averageMethod": "(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0,6*(AV1+AV2[10]/2))",
          "cutOffGrade": 6
       }
    ]



![img_7.png](img_7.png)

- **Parâmetros**: 
  - `userId` - id do usuário;
    - `id` - id da disciplina.
- **Request Body**
    ```json
  {
      "string":"BD"
  }
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3*2)/3",
        "cutOffGrade": 6.0
    }


![img_8.png](img_8.png)

- **Altera a forma como o método de cálculo da média final é definida. Deleta as projeções equivalentes e criar uma nova atualizada com um novo id pra essa projeção.**
- **Parâmetros**:
    - `userId` - id do usuário;
    - `id` - id da disciplina.
- **Request Body**
    ```json
  {
      "string":"(P2+P3)/2"
  }
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3)/2",
        "cutOffGrade": 6.0
    }

![img_9.png](img_9.png)

- **Parâmetros**:
    - `userId` - id do usuário;
    - `id` - id da disciplina.
- **Request Body**
    ```json
  {
      "value": 7
  }
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3)/2",
        "cutOffGrade": 7.0
    }

![img_10.png](img_10.png)

- **Parâmetro**: `userId` - id do usuário;
- **Lista todos os cursos juntamente com suas projeções e as atividades.**
- **Response**
  ```json
  [
     {
          "id": 2,
          "name": "SGBD",
          "assessment":[
              {
                 "id": 3,
                 "identifier": "AT1",
                 "grade": 0,
                 "requiredGrade": 4.7
              },
              {
                 "id": 4,
                 "identifier": "AT2",
                 "grade": 0,
                 "requiredGrade": 4.7
              },
                    .
                    .
                    .
                    .
                    .
              {
                 "id": 12,
                 "identifier": "AV1",
                 "grade": 0,
                 "requiredGrade": 4.7
              },
              {
                 "id": 13,
                 "identifier": "AV2",
                 "grade": 0,
                 "requiredGrade": 4.7
              }
          ],
          "finalGrade": 0,
          "courseName": "SGBD"
     },
     {
          "id": 4,
          "name": "BD",
          "assessment": [
              {
                 "id": 16,
                 "identifier": "P2",
                 "grade": 0,
                 "requiredGrade": 7
              },
              {
                 "id": 17,
                 "identifier": "P3",
                 "grade": 0,
                 "requiredGrade": 7
              }
          ],
          "finalGrade": 0,
          "courseName": "BD"
     }
  ]


![img_11.png](img_11.png)

- **Parâmetro**: `userId` - id do usuário;
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3)/2",
        "cutOffGrade": 7.0
    }

### projection-controller

![img_12.png](img_12.png)

-**Cria uma nova projeção e automaticamente suas avaliações de acordo com a definição do curso.**
- **Parâmetro**: `courseId` - id da disciplina;
- **Request Body**
    ```json
      {
          "string": "projeção 2"
      }
- **Response**
    ```json
    {
        "id": 5,
        "name": "projeção 2",
            .
            .
            .
        "finalGrade": 0.0,
        "courseName": "SGBD"
    }
![img_13.png](img_13.png)

- **retorna todas as projeções de um determinido curso com uma lista de avaliações equivalentes.**
- **Parâmetro**: `courseId` - id da disciplina;
  - **Response**
      ```json
    [
        {
          "id": 2,
          "name": "SGBD",
          "assessment": [
              {
                  "id": 3,
                  "identifier": "AT1",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              },
              {
                  .
                  .
                  .
                  .
              {
                  "id": 10,
                  "identifier": "AT8",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              },
              {
                  "id": 11,
                  "identifier": "AT9",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              },
              {
                  "id": 12,
                  "identifier": "AV1",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              },
              {
                  "id": 13,
                  "identifier": "AV2",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              }
          ],
          "finalGrade": 0.0,
          "courseName": "SGBD"
        },
        {
          "id": 5,
          "name": "projeção 2",
          "assessment": [
              {
                  "id": 18,
                  "identifier": "AT1",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              },
                  .
                  .
                  .
                  .
              {
                  "id": 28,
                  "identifier": "AV2",
                  "grade": 0.0,
                  "requiredGrade": 4.7
              }
          ],
          "finalGrade": 0.0,
          "courseName": "SGBD"
        }
    ]
![img_14.png](img_14.png)

-**retorna todas as projeções de um determinido curso com umalista de avaliações equivalentes.**
- **Parâmetro**: `courseId` - id da disciplina;
- **Parâmetro**: `id` - id da projeção;
- **Request Body**
    ```json
      {
          "string":"Projeção otimista"
      }
- **Response**
    ```json
    {
       "id": 5,
      "name": "Projeção otimista",
         .
         .
         .
      "courseName": "SGBD"
    }
![img_15.png](img_15.png)

- **Deleta apenas a projeção especificada.**
- **Parâmetro**: `courseId` - id da disciplina;
- **Parâmetro**: `id` - id da projeção;
- **Response**
    ```json
    {
      "id": 5,
      "name": "Projeção otimista",
         .
         .
         .
      "courseName": "SGBD"
    }

![img_16.png](img_16.png)

- **Deleta todas as projeções do curso, incluindo a projeção default.**
- **Parâmetro**: `courseId` - id da disciplina;

### assessment-controller

![img_18.png](img_18.png)

- **Lista todas as avaliações de uma projeção.**
- **Parâmetro**: `projectionId` - id da projeção;
- **Response**
    ```json
  [
       {
          "id": 3,
          "identifier": "AT1",
          "grade": 0.0,
          "requiredGrade": 4.7
       },
       {
          "id": 4,
          "identifier": "AT2",
          "grade": 0.0,
          "requiredGrade": 4.7
       },
       {
          .
          .
          .
          .
       {
          "id": 13,
          "identifier": "AV2",
          "grade": 0.0,
          "requiredGrade": 4.7
        }
  ]



![img_17.png](img_17.png)

- **Posta a nota adquirida. Automaticamente a média final é calculada juntamente com o quanto de pontuação ainda falta em cada avaliação ainda não realizada, para atingir a nota de corte.**
- **Parâmetro**: `projectionId` - id da projeção;
- **Parâmetro**: `id` - id da avaliação;
- **Request Body**
    ```json
  {
      "value": 7
  }
  
- **Response**
    ```json
    {
       "id": 12,
       "identifier": "AV1",
       "grade": 7.0,
       "requiredGrade": 0.0
  }

### 📌 Após esse fluxo podemos realizar uma requisição para o end-point `/{courseId}/projections` com o método HTTP `GET` e observa:

- **o cálculo automático para a média final;**
- **O cálculo para as notas requeridas.**
  - aquelas em que a nota está definida recebe o valor zero;
  - São calculadas de forma uniforme.

- **Response**
    ```json
      [
    {
        "id": 2,
        "name": "SGBD",
        "assessment": [
            {
                "id": 3,
                "identifier": "AT1",
                "grade": 6.0,
                "requiredGrade": 0.0
            },
            {
                "id": 4,
                "identifier": "AT2",
                "grade": 5.0,
                "requiredGrade": 0.0
            },
            {
                "id": 5,
                "identifier": "AT3",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 6,
                "identifier": "AT4",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 7,
                "identifier": "AT5",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 8,
                "identifier": "AT6",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 9,
                "identifier": "AT7",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 10,
                "identifier": "AT8",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 11,
                "identifier": "AT9",
                "grade": 0.0,
                "requiredGrade": 1.9
            },
            {
                "id": 12,
                "identifier": "AV1",
                "grade": 7.0,
                "requiredGrade": 0.0
            },
            {
                "id": 13,
                "identifier": "AV2",
                "grade": 0.0,
                "requiredGrade": 1.9
            }
        ],
        "finalGrade": 4.933333333333334,
        "courseName": "SGBD"
    }
  ]

- Cálculo : 0.4*((**6**+**5**+0+0+0+0)/6)+0,6*(**7**+(0/2))


## Modelo de Dados
## Tratamento de Erros
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

## Motivação e Solução 



## Práticas adotadas

- SOLID
- API REST
- Consultas com Spring Data JPA
- Injeção de Dependências
- Tratamento de respostas de erro
- Geração automática do Swagger com a OpenAPI 3
- Testes automatizados
- Banco de dados relacional

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

