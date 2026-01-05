> **Leia em outros idiomas:** [Português](README.md)

<h1 align="center">
  Grades API
</h1>

<p align="center">
  <a href="https://github.com/GustavoDaMassa/MediasAPI/actions/workflows/ci.yml">
    <img src="https://github.com/GustavoDaMassa/MediasAPI/actions/workflows/ci.yml/badge.svg" alt="Java CI with Maven">
  </a>
</p>

<p align="center">
  <img src="images/img_19.png" alt="Image Description">
</p>

# 📌 REST API for Academic Grades and Projections Management

This solution was originally built with Java using the Spring Framework in this repository
but is also implemented with the following stacks:
- [c#/.Net with ASPNET core](https://github.com/GustavoDaMassa/dotNetMediasAPI)
- [typeScript/nodeJS with NestJs](https://github.com/GustavoDaMassa/nodeMediasAPI)

## Overview

This API was developed to offer a complete solution for academic grades management. With it, users can store their assessments and automatically get the final grade for each course. It was designed with the core objective of enabling strategic projections for better academic performance planning.

The unique characteristic of this API is the customization of the calculation method for each course. It's possible to define custom rules for calculating the average, making the system adaptable to different approaches. Additionally, the API automatically calculates the score needed to reach the cut-off grade.

Both students and teachers can use it to manage grades, create projections for each student or scenario, and post grades.

Learn more about the scope of the **[applied solution](#motivation-and-solution-)**.

---

## Main Features:

- Store grades in a structured and efficient way.
- Define custom methods for calculating averages.
- Automatically get the score needed to achieve academic goals.
- Simulate different scenarios for strategic planning.
- Manage courses.

---

## Demo

![img_3.png](images/projeções.png)

To demonstrate the main flow and exemplify the application's potential, it has a simple [integrated front-end](#example-1) developed with Thymeleaf.

---
## Using the API:
 #### Before detailing the endpoints and authentication, it's important to understand the API usage flow:

### **Login**:
  - The user creates their profile by registering in the system.

### **Authentication**:
  - The API validates the credentials and, if successful, returns a JWT token.
### **Courses**:
  - The user can create and edit their custom courses.
### **Projections**:
  - A projection is automatically created when defining the calculation method in the previous step. Users can create, edit and view other projections.
### **Assessments**:
  - Also automatically created for each projection. Users can post grades.

 ### If you wish to compile and navigate the system on your own, proceed to [how to run](#how-to-run)

---

## Authentication (JSON Web Token)

 To obtain a token, the user must make a POST request to the `/authenticate` endpoint with their credentials. The returned token must be included in the Authorization header of all subsequent requests, except for registration.

### Role Management (User Profiles)
The API implements a role system for profile-based access control. Each user can have one or more associated roles, which determine which resources and operations they are authorized to access. Roles are verified during the authorization process, ensuring that only users with appropriate permissions can perform certain actions.

![img.png](images/authenticate.png)

- **The user is authenticated through the email which is unique in the system, public route;**
- **Auth Type:** `Bearer Token`
- **Request Body:**
  ```json
   {
        "email":"gustavo.pereira@discente.ufg.br",
        "password":"aula321"
   }
```
---

## API Versioning

The API uses URI (Uniform Resource Identifier) versioning to manage the evolution of its interface in a controlled manner and avoid breaking existing clients. The API version is included directly in the URL path.

*   **Current Version:** `v1`
*   **Prefix:** All REST API endpoints are prefixed with `/api/v1`.

**Example:**
*   To access user resources in version 1: `/api/v1/users`
*   To access course resources in version 1: `/api/v1/{userId}/courses`

This strategy ensures that new API versions can be introduced in the future (e.g.: `/api/v2/users`) without impacting clients still using the previous version.

---
## API Endpoints

### user-controller

![img_1.png](images/cadastrarusuarios.png)
- **Creates a new user profile, authentication is not required.**
- **Endpoint:** `POST /api/v1/users`
- **Request Body:**
   ```json
   {
      "name":"Gustavo Henrique",
      "email":"gustavo.pereira@discente.ufg.br",
      "password":"aula321"
   }
```
- **Response**
  ```json
   {
       "id": 2,
       "name": "Gustavo Henrique",
       "email": "gustavo.pereira@discente.ufg.br"
   }
```


---
![img_2.png](images/img_2.png)
- **Returns a list with all users and their respective IDs which will be used as parameters in other requests;**
- **Endpoint:** `GET /api/v1/users`
- **Response**
  ```json
    [
        {
            "id": 2,
            "name": "Gustavo Henrique",
            "email": "gustavo.pereira@discente.ufg.br"
        }
    ]
```

---
![img_1.png](images/img_1.png)
- **Parameter**: `id` - user ID;
- **Endpoint:** `PATCH /api/v1/users/{id}/name`
- **Request Body:**
   ```json
    {
        "string": "Gustavo"
    }
```

- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavo.pereira@discente.ufg.br"
    }
```

---
![img_3.png](images/img_3.png)

- **Parameter**: `id` - user ID;
- **Endpoint:** `PATCH /api/v1/users/{id}/email`
- **Request Body:**
   ```json
    {
        "email":"gustavohenrique3gb@gmail.com"
    }
```

- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavohenrique3gb@gmail.com"
    }
```

---
![img_4.png](images/img_4.png)

- **Parameter**: `id` - user ID;
- **Endpoint:** `DELETE /api/v1/users/{id}`
- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavohenrique3gb@gmail.com"
    }
```

---
- **Parameter**: `email` - user email;
- **Endpoint:** `GET /api/v1/users/{email}`
- **Response**
  ```json
    {
        "id": 2,
        "name": "Gustavo",
        "email": "gustavo.pereira@discente.ufg.br"
    }
```

### course-controller

---
![img_5.png](images/img_5.png)

- **Creates a new course, and through the average calculation method automatically creates a projection with the same name, identifying and instantiating the defined assessments.**
- **Parameter**: `userId` - user ID;
- **Endpoint:** `POST /api/v1/{userId}/courses`
- **Request Body:**
   ```json
    {
        "name":"SGBD",
        "averageMethod":"(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0,6*(AV1+AV2[10]/2))",
        "cutOffGrade": 6.0
    }
```

- **`AverageMethod:`**
  - Constants are represented by `double` values;
  - Identifiers can contain numbers, the special character `_` and at least one letter;

  - Identifiers can have the suffix `[N]` indicating the maximum grade for the assessment;
    - **N** is a `double`;
    - if not informed, assumes the default value `10`.
  - `@M[n](i1;i2;...;im)` is an extra functionality beyond arithmetic operations;
    - It sums the n highest grades provided within the parentheses;
    - `m>=n` ;
    - n is an `int`.
  - `cutOffGrade` is also optional;
    - default value `6.0`.
  - Syntactic and semantic analysis of the formula is performed.

- **Response**
  ```json
    {
        "id": 2,
        "name": "SGBD",
        "averageMethod": "(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0,6*(AV1+AV2[10]/2))",
        "cutOffGrade": 6
}
```

---
![img_6.png](images/img_6.png)

- **Lists all user courses.**
- **Parameter**: `userId` - user ID;
- **Endpoint:** `GET /api/v1/{userId}/courses`
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
```



---
![img_7.png](images/img_7.png)

- **Endpoint:** `PATCH /api/v1/{userId}/courses/{id}/name`
- **Parameters**:
  - `userId` - user ID;
    - `id` - course ID.
- **Request Body**
    ```json
  {
      "string":"BD"
  }
```
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3*2)/3",
        "cutOffGrade": 6.0
    }
```


---
![img_8.png](images/img_8.png)

- **Changes how the final average calculation method is defined. Deletes the equivalent projections and creates a new updated one with a new ID for this projection.**
- **Endpoint:** `PATCH /api/v1/{userId}/courses/{id}/method`
- **Parameters**:
    - `userId` - user ID;
    - `id` - course ID.
- **Request Body**
    ```json
  {
      "string":"(P2+P3)/2"
  }
```
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3)/2",
        "cutOffGrade": 6.0
    }
```

---
![img_9.png](images/img_9.png)

- **Endpoint:** `PATCH /api/v1/{userId}/courses/{id}/cutoffgrade`
- **Parameters**:
    - `userId` - user ID;
    - `id` - course ID.
- **Request Body**
    ```json
  {
      "value": 7
  }
```
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3)/2",
        "cutOffGrade": 7.0
    }
```

---
![img_10.png](images/img_10.png)

- **Parameter**: `userId` - user ID;
- **Endpoint:** `GET /api/v1/{userId}/courses/projections`
- **Lists all courses along with their projections and activities.**
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
```


---
![img_11.png](images/img_11.png)

- **Delete course**
- **Endpoint:** `DELETE /api/v1/{userId}/courses/{id}`
- **Parameter**: `userId` - user ID;
- **Response**
    ```json
    {
        "id": 1,
        "name": "BD",
        "averageMethod": "(P2+P3)/2",
        "cutOffGrade": 7.0
    }
```

### projection-controller

---
![img_12.png](images/img_12.png)

-**Creates a new projection and automatically its assessments according to the course definition.**
- **Endpoint:** `POST /api/v1/{courseId}/projections`
- **Parameter**: `courseId` - course ID;
- **Request Body**
    ```json
      {
          "string": "projection 2"
      }
```
- **Response**
    ```json
    {
        "id": 5,
        "name": "projection 2",
            .
            .
            .
        "finalGrade": 0.0,
        "courseName": "SGBD"
    }
```
---
![img_13.png](images/img_13.png)

- **Returns all projections of a specific course with a list of equivalent assessments.**
- **Endpoint:** `GET /api/v1/{courseId}/projections`
- **Parameter**: `courseId` - course ID;
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
          "name": "projection 2",
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
```
---
![img_14.png](images/img_14.png)

- **Endpoint:** `PATCH /api/v1/{courseId}/projections/{id}`
- **Parameter**: `courseId` - course ID;
- **Parameter**: `id` - projection ID;
- **Request Body**
    ```json
      {
          "string":"Optimistic Projection"
      }
```
- **Response**
    ```json
    {
       "id": 5,
      "name": "Optimistic Projection",
         .
         .
         .
      "courseName": "SGBD"
    }
```
---
![img_15.png](images/img_15.png)

- **Deletes only the specified projection.**
- **Endpoint:** `DELETE /api/v1/{courseId}/projections/{id}`
- **Parameter**: `courseId` - course ID;
- **Parameter**: `id` - projection ID;
- **Response**
    ```json
    {
      "id": 5,
      "name": "Optimistic Projection",
         .
         .
         .
      "courseName": "SGBD"
    }
```

---
![img_16.png](images/img_16.png)

- **Deletes all course projections, including the default projection.**
- **Endpoint:** `DELETE /api/v1/{courseId}/projections/all`
- **Parameter**: `courseId` - course ID;

---
![img_18.png](images/img_18.png)

- **Lists all assessments of a projection.**
- **Endpoint:** `GET /api/v1/{projectionId}/assessments`
- **Parameter**: `projectionId` - projection ID;
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
```


---
![img_17.png](images/img_17.png)

- **Posts the acquired grade. The final average is automatically calculated along with how much score is still needed in each assessment not yet taken, to reach the cut-off grade.**
- **Endpoint:** `PATCH /api/v1/{projectionId}/assessments/{id}`
- **Parameter**: `projectionId` - projection ID;
- **Parameter**: `id` - assessment ID;
- **Request Body**
    ```json
  {
      "value": 7
  }
```

- **Response**
    ```json
    {
       "id": 12,
       "identifier": "AV1",
       "grade": 7.0,
       "requiredGrade": 0.0
  }
```

---

### 📌 Observing the Results
After this flow we can make a request to the endpoint `/api/v1/{courseId}/projections` with the HTTP method `GET` and observe:

- **Automatic calculation for the final average;**
- **Calculation for the required grades.**
  - Those where the grade is defined receive the value zero;
  - They are calculated uniformly.

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
```

- Calculation : 0.4*((**6**+**5**+0+0+0+0)/6)+0,6*(**7**+(0/2))

---
## Data Model

![img.png](images/img.png)

- User has N --> Courses;
- Course has N --> Projections;
- Projection has N --> Assessments;

All IDs are primary keys. Entities have a uniqueness constraint between their respective `name` and `id`, in the case of Assessment this uniqueness is between the `identifier` and its `id`.

---

##  Exception Handling

The API returns standardized responses for errors and exceptions. Below are the error codes and their descriptions:

###  Custom Exceptions

| StatusCode | Exception            | Error example                                             |
|------------|--------------------|-----------------------------------------------------------|
| 404        | `NotFoundArgumentException`  | Course id 30 not found for UserId 2                       |
| 400        | `IllegalArgumentException` | It is not possible to select more values than those provided |
| 400        | `NoSuchElementException`    | The equation has operators without arguments              |
| 400        | `DataIntegrityException`     | The attribute SGBD already exist for this context         |
| 500        | `InternalServerError` | possible division by zero detected                                     |

### Example:
- **Method**: POST;
- **URI** {userId}/courses
  - **Request:**
  ```json
  {
      "name":"Greek",
      "averageMethod":" 2,5(P_1)+3 / 2,5*(P2#)3 ",
      "cutOffGrade": 6.0
  }
```
- **Response:**
  ```json
  {
    "statusCode": 400,
    "error": "Method for calculating averages not accepted, formula terms are invalid: 2,5----------#-3",
    "path": "/2/courses",
    "timestamp": "2025-03-28T17:31:04.744315954"
  }
```
---
## How to Run

The Application uses Docker and is made available inside a container with the API image and the database it connects to.

#### Dependencies:

- [Docker](#docker);
- [Docker Compose](#docker-compose).

### Option 1


#### Steps to run:

  - Clone the repository and enter the directory:
```
git clone https://github.com/GustavoDaMassa/MediasAPI.git
cd MediasAPI
```
- Start the container with an updated image:
```
docker compose up --build -d
```
- Stopping the application:
```
docker compose down
```
### Option 2
 **If you want to run it more easily without the need to clone the repository**

- Download the file [docker compose](./Compose%20docker%20/docker-compose.yaml)
  - This file creates an instance of the application according to the most recent version in the repository [docker hub](https://hub.docker.com/r/gustavodamassa/medias-api/tags);
  - **Keep the file name.**
- Run the following command in the directory where the file was downloaded
```
docker compose up
```
### Application Running

  After running you can navigate through it by making requests through:
 ### - [Project Swagger](https://localhost/swagger-ui/index.html)
  - Or through any API Client of your choice at **https://localhost**

---

## HTTPS Configuration with Nginx (Docker)

To ensure communication security, the API is exposed via HTTPS in Docker environment, using Nginx as a reverse proxy.

### How it works:
1.  **Nginx as Reverse Proxy:** Nginx intercepts all external requests on port 443 (HTTPS).
2.  **SSL Termination:** Nginx is responsible for decrypting HTTPS traffic and forwarding it to the Spring Boot application (which runs internally on HTTP on port 8080) within the Docker network.
3.  **HTTP to HTTPS Redirect:** Any access attempt via HTTP (port 80) is automatically redirected to HTTPS.

### SSL Certificates:
*   **Local Development:** To facilitate development, a self-signed SSL certificate is generated and used by Nginx. When accessing `https://localhost`, your browser will display a security warning, which can be safely ignored for development purposes.
*   **Production:** In a production environment, it is **essential** to replace the self-signed certificate with a certificate issued by a trusted Certificate Authority (CA), such as Let's Encrypt. This ensures that users do not receive security warnings and that communication is fully secure and verified.

---

### Dependencies

####  Docker:
Install according to your operating system:

- Linux (Ubuntu/Debian):

```
  sudo apt update && sudo apt install docker.io -y
```
```
  sudo systemctl enable --now docker
```

- Windows/Mac: Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/).

####  Docker Compose:

- Linux
```
  sudo apt install docker-compose -y
```
- Windows/Mac: Docker Compose is already included in Docker Desktop.

---

## CORS Configuration

To ensure security and interoperability with frontend applications, this API implements a global Cross-Origin Resource Sharing (CORS) policy.

### How it works:
1.  **Origin Control:** The API explicitly defines which domains (origins) are allowed to make HTTP/HTTPS requests to its endpoints. This prevents unauthorized sites from accessing your resources.
2.  **Allowed Methods and Headers:** HTTP methods (GET, POST, PUT, DELETE, OPTIONS) and headers that can be used in cross-origin requests are specified.
3.  **Credentials:** The configuration allows sending credentials (such as JWT authentication tokens or cookies) in authentication flows.

---

## Example

---
## Motivation and Solution

During the academic journey at university, managing grades or even just storing them is a process that can be automated efficiently. However, as each professor and course define their own evaluation methods and performance criteria according to their preferences and approaches, there is great variability and flexibility in this process.

For the application to support these custom definitions, a solution based on regular expression processing was implemented, allowing dynamic identification and manipulation of variables, constants and operators. This way, final grade calculation is automated in a flexible and adaptable manner to different evaluation rules.

Another challenge was implementing this dynamic calculation. The adopted solution uses Reverse Polish Notation (RPN), which eliminates the need for parentheses by defining the correct order of precedence directly in its structure. Additionally, an adaptation of the Shunting Yard algorithm was employed, using stacks and lists as data structures to ensure correct expression evaluation.

```
^(\d+(([.,])?\d+)?)(?=[\+\-\*\/])|(?<=[\+\-\*\/\(;])(\d+(([.,])?\d+)?)(?=[\+\-\*\/\);])|(?<=[\+\-\*\/])(\d+(([.,])?\d+)?)$|[\+\-\*\/\(\)\;]|(?<=[\+\-\*\/\)\(;])@M(\[\d+\]\()?|^@M(\[\d+\]\()?|(?<!@)\w*[A-Za-z]\w*(\[(\d+(([.,])?\d+)?)\])?
```
- Regex engineering tool: [regexr](https://regexr.com/)

---

## Adopted Practices

- **Architecture and Design**
  - REST API with layered architecture
  - Application of SOLID principles
  - Dependency Injection
  - Use of the Data Transfer Object (DTO) pattern

- **Validation and Security**
  - Custom validations and use of Bean Validation
  - Implementation of authentication and authorization via JWT
  - CORS configuration for origin access control

- **Error Handling and Responses**
  - Standardized error capture and handling

- **Documentation**
  - API documentation with diagrams and examples
  - Technical endpoint documentation with OpenAPI 3

- **Testing and Code Quality**
  - Automated tests with mock creation and separate environment

- **Database**
  - Relational database modeling with constraint definitions
  - JPQL and native SQL queries with Spring Data JPA

- **Tools and Deployment**
  - Use of API Client and Database Client during development
  - Application encapsulation with Docker, creating custom images and containers
  - Code versioning with Git

- **CI/CD with GitHub Actions**
  - The project uses a Continuous Integration (CI) pipeline with GitHub Actions.
  - With each `push` or `pull request` to the `main` branch, the workflow in `.github/workflows/ci.yml` is triggered.
  - The pipeline performs code checkout, configures the Java 17 environment and executes the `./mvnw test` command to compile and test the application, ensuring that new changes don't break existing code.

### Technologies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Maven](https://maven.apache.org/)
- [H2 DataBase](https://www.h2database.com/html/main.html)
- [Bean Validation](https://beanvalidation.org/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [JUnit](https://junit.org/junit5/)
- [Mysql](https://dev.mysql.com/downloads/)
- [Workbench](https://www.mysql.com/products/workbench/)
- [Postman](https://postman.com/)
- [Docker](https://www.docker.com/products/docker-hub/)
---

## Observability and Logging

To ensure visibility into application behavior and facilitate problem diagnosis, we implemented a structured logging system and a local observability environment with the ELK Stack.

### Structured Logging

The application now generates logs in JSON format, making them easily machine-readable and ideal for processing by log analysis tools.

*   **JSON Format**: All logs are emitted in JSON format, using the `logstash-logback-encoder` library. This allows easy ingestion and analysis by systems like Elasticsearch.
*   **Enriched Context (MDC)**: For authenticated requests, the user's email (`userEmail`) is automatically added to the context of each log (Mapped Diagnostic Context - MDC). This facilitates tracking actions of specific users.
*   **Detailed Error Logs**: The `GlobalExceptionHandler` has been instrumented to log all captured exceptions at the `ERROR` level, including the complete stack trace, ensuring no error goes unnoticed.
*   **Request Logs**: The main REST controller endpoints now log `INFO` messages when accessed, providing visibility into the request flow.

### Local Observability with ELK Stack (Elasticsearch, Logstash, Kibana)

An ELK Stack environment has been configured via `docker-compose` to collect, process and visualize the application's JSON logs locally.

#### Components:
*   **Elasticsearch**: Stores and indexes JSON logs.
*   **Logstash**: Receives logs from the application via TCP, processes them and sends them to Elasticsearch.
*   **Kibana**: Web interface to search, analyze and visualize logs stored in Elasticsearch.

#### How to Use:

**1. Start the ELK Stack:**

To start only the ELK Stack services (useful when you run the application through the IDE):

```bash
docker-compose up -d elasticsearch logstash kibana
```

To start the complete environment (application, database, Nginx and ELK):

```bash
docker-compose up -d
```

**2. Access Kibana:**

After the containers are up (it may take a few minutes for Elasticsearch and Kibana to be fully ready), access Kibana in your browser:

```
http://localhost:5601
```

**3. Configure the Index Pattern in Kibana:**

The first time you access Kibana, or if you haven't already:
*   Go to **Management** (gear icon) -> **Stack Management** -> **Index Patterns**.
*   Click **Create index pattern**.
*   In the "Index pattern name" field, type `mediasapi-logs-*` and click **Next step**.
*   In the "Time field" field, select `@timestamp` and click **Create index pattern**.

**4. View Logs:**

Go to **Analytics** (compass icon) -> **Discover**. You should see the JSON logs from your application.

#### Running the Application:

*   **Via Docker (with `docker-compose up -d`)**: The application (`mediasapi`) will be started with the `docker` profile active, and logs will be automatically sent to the `logstash` service within the Docker network.
*   **Via IDE (IntelliJ, etc.)**: Start the `MediasApiApplication` class directly. The application will use the default profile, which sends logs to `localhost:5000`. Make sure the ELK Stack is running (step 1) so that Logstash receives the logs.

---

## Application Monitoring and Health (Health Check)

This project uses **Spring Boot Actuator** to expose endpoints that allow monitoring the application's health and status. This is fundamental for ensuring resilience and observability in a production environment.

### Available Endpoints

*   **General Health Check:** `GET /actuator/health`
    *   Returns a detailed status of the application, including database connectivity and disk space.

*   **Liveness Probe:** `GET /actuator/health/liveness`
    *   Indicates if the application is running (alive). Used by container orchestrators (like Docker and Kubernetes) to decide if a container needs to be restarted.

*   **Readiness Probe:** `GET /actuator/health/readiness`
    *   Indicates if the application is ready to accept new requests. Used by load balancers to decide whether or not to send traffic to an application instance.
