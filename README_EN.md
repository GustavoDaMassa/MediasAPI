> **Leia em outros idiomas:** [Português](README.md)

<h1 align="center">
  Academic Grades Management API
</h1>

<p align="center">
  <a href="https://github.com/GustavoDaMassa/MediasAPI/actions/workflows/ci.yml">
    <img src="https://github.com/GustavoDaMassa/MediasAPI/actions/workflows/ci.yml/badge.svg" alt="Java CI with Maven">
  </a>
</p>

<p align="center">
  <img src="images/img_19.png" alt="Application Preview">
</p>

## 🚀 Quick Start

```bash
# Clone and run with Docker
git clone https://github.com/GustavoDaMassa/MediasAPI.git
cd MediasAPI
docker compose up -d

# Access the application
open https://localhost/swagger-ui/index.html
```

**Alternative implementations:**
- [C#/.NET with ASP.NET Core](https://github.com/GustavoDaMassa/dotNetMediasAPI)
- [TypeScript/Node.js with NestJS](https://github.com/GustavoDaMassa/nodeMediasAPI)

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technical Stack](#technical-stack)
- [Core Features](#core-features)
- [Dynamic Calculation Engine](#dynamic-calculation-engine)
- [Security Architecture](#security-architecture)
- [API Reference](#api-reference)
- [Database Schema](#database-schema)
- [Deployment](#deployment)
- [Development Guide](#development-guide)
- [Monitoring & Observability](#monitoring--observability)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

A production-ready REST API for academic performance management with dynamic grade calculation capabilities. Built with Spring Boot 3.4.2 and Java 17, featuring a sophisticated expression evaluation engine based on Reverse Polish Notation (RPN) and the Shunting Yard algorithm.

### Key Capabilities

- **Dynamic Formula Processing**: Define custom grading formulas using mathematical expressions with regex-based parsing
- **Strategic Projections**: Create multiple scenarios to simulate academic outcomes
- **Automatic Calculations**: Real-time computation of final grades and required scores
- **Production-Ready**: Includes health checks, structured logging, ELK stack integration, and automated migrations

### Use Cases

- **Students**: Track grades across courses with personalized calculation methods
- **Educators**: Manage class grades with flexible grading schemes
- **Institutions**: Standardize grade management with customizable evaluation criteria

---

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         Nginx (443/80)                       │
│              SSL Termination + Reverse Proxy                │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                  Spring Boot Application (8080)              │
│  ┌────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │ REST API   │  │  Web MVC     │  │   Actuator       │   │
│  │ (JWT Auth) │  │ (Form Login) │  │ (Health Checks)  │   │
│  └────────────┘  └──────────────┘  └──────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │          Business Logic Layer                       │   │
│  │  • Dynamic Expression Parser (Regex + RPN)         │   │
│  │  • Grade Calculation Service                       │   │
│  │  • Projection Management                           │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │          Data Access Layer (Spring Data JPA)        │   │
│  └─────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
┌────────▼────────┐ ┌──────▼──────┐ ┌───────▼────────┐
│  MySQL 8.0      │ │ Logstash    │ │ Elasticsearch  │
│  (Persistent    │ │ (Log        │ │ (Log Storage & │
│   Storage)      │ │  Aggregation)│ │  Indexing)    │
└─────────────────┘ └─────────────┘ └────────┬───────┘
                                              │
                                     ┌────────▼───────┐
                                     │    Kibana      │
                                     │ (Log Analytics)│
                                     └────────────────┘
```

### Layered Architecture

```
┌──────────────────────────────────────────────────────┐
│                 Presentation Layer                    │
│  • UserController, CourseController, etc.            │
│  • REST API v1 (/api/v1/*)                          │
│  • Web Controllers (Thymeleaf)                       │
└───────────────────────┬──────────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────────┐
│                  Service Layer                        │
│  • UserService, CourseService                        │
│  • RegularExpressionProcessor                        │
│  • CalculateFinalGrade, CalculateRequiredGrade      │
│  • ConvertToPolishNotationReverse                   │
└───────────────────────┬──────────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────────┐
│               Repository Layer                        │
│  • UserRepository, CourseRepository                  │
│  • Spring Data JPA + JPQL queries                   │
└───────────────────────┬──────────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────────┐
│                  Domain Layer                         │
│  • User, Course, Projection, Assessment              │
│  • JPA Entities with validation                      │
└──────────────────────────────────────────────────────┘
```

### Design Patterns

- **Dependency Injection**: Constructor-based injection via Spring
- **DTO Pattern**: Decoupling domain models from API contracts
- **Repository Pattern**: Data access abstraction with Spring Data JPA
- **Strategy Pattern**: Multiple calculation strategies via service interfaces
- **Chain of Responsibility**: Expression parsing pipeline

---

## 🛠️ Technical Stack

### Core Framework
- **Spring Boot 3.4.2** - Application framework
- **Java 17** - Programming language
- **Maven** - Build automation and dependency management

### Data Layer
- **Spring Data JPA** - ORM and data access
- **MySQL 8.0** - Production database
- **Flyway** - Database migration management
- **H2 Database** - In-memory database for testing

### Security
- **Spring Security** - Authentication and authorization framework
- **OAuth2 Resource Server** - JWT token validation
- **RSA Key Pair** - Asymmetric key cryptography for JWT signing

### API Documentation
- **SpringDoc OpenAPI 3** - API specification (Swagger UI)
- **Bean Validation** - Request/Response validation

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **Nginx** - Reverse proxy and SSL termination
- **ELK Stack** - Centralized logging (Elasticsearch, Logstash, Kibana)

### Observability
- **Spring Boot Actuator** - Health checks and metrics
- **Logstash Logback Encoder** - Structured JSON logging
- **MDC (Mapped Diagnostic Context)** - Request tracing

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing

### DevOps
- **GitHub Actions** - CI/CD pipeline
- **Docker Hub** - Container registry

---

## ⚡ Core Features

### 1. Dynamic Grade Calculation Engine

The heart of the system is a custom mathematical expression parser and evaluator.

**Expression Syntax:**
```java
// Simple weighted average
"(P1*0.4 + P2*0.6)"

// Complex formula with special function
"(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0.6*(AV1+AV2[10]/2))"
```

**Features:**
- **Custom Identifiers**: `AT1`, `PROVA_FINAL`, `exam_1` (must contain at least one letter)
- **Max Grade Specification**: `AV2[10]` defines max grade of 10
- **Special Function `@M[n](...)`**: Sums the top N grades from provided assessments
- **Standard Operators**: `+`, `-`, `*`, `/`, `(`, `)`
- **Decimal Support**: Both `.` and `,` as decimal separators

### 2. Multi-Scenario Projections

Create unlimited projection scenarios per course to explore "what-if" situations.

**Workflow:**
1. Course created → Default projection auto-generated
2. User posts actual grades → Automatic recalculation of final grade
3. System calculates required grades for remaining assessments
4. User creates additional projections for different scenarios

### 3. Automated Grade Requirements

After posting any grade, the system automatically calculates:
- **Final Grade**: Using the custom formula
- **Required Grade**: Evenly distributed among remaining assessments to meet cutoff

**Example:**
```
Course: Database Management
Formula: (0.4*(@M[6](AT1;...;AT9)/6)) + (0.6*(AV1+AV2)/2)
Cutoff: 6.0

Posted Grades: AT1=6.0, AT2=5.0, AV1=7.0
Remaining: AT3-AT9, AV2

Result:
- Current Final Grade: 4.93
- Required Grade (per remaining assessment): 1.9
```

### 4. Dual Authentication System

**REST API (Stateless):**
- JWT Bearer tokens with RSA signature
- Public endpoints: `/api/v1/users` (POST), `/authenticate`
- Token-based authorization

**Web Interface (Stateful):**
- Form-based login with session management
- Thymeleaf templates for UI
- CSRF protection enabled

### 5. Production-Grade Operations

- **Health Checks**: Liveness and readiness probes for K8s/Docker
- **Structured Logging**: JSON logs with MDC context (user email)
- **Database Migrations**: Automated with Flyway
- **API Versioning**: URI-based (`/api/v1/*`)
- **CORS Configuration**: Global cross-origin policy

---

## 🧮 Dynamic Calculation Engine

### Architecture Overview

The calculation engine processes custom formulas through a 5-stage pipeline:

```
Input Formula
     │
     ▼
┌─────────────────────────────────────────┐
│  1. RegularExpressionProcessor          │
│     Tokenizes using complex regex       │
│     Output: List<String> tokens         │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│  2. IdentifiersDefinition               │
│     Maps identifiers to max grades      │
│     Output: Map<String, Double>         │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│  3. ConvertToPolishNotationReverse      │
│     Shunting Yard Algorithm             │
│     Output: RPN token list              │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│  4. CalculateFinalGrade                 │
│     Stack-based RPN evaluation          │
│     Output: Double (final grade)        │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│  5. CalculateRequiredGrade              │
│     Inverse calculation for target      │
│     Output: Required grade per assess.  │
└─────────────────────────────────────────┘
```

### Regex Pattern Breakdown

```regex
^(\d+(([.,])?\d+)?)(?=[\+\-\*\/])
|(?<=[\+\-\*\/\(;])(\d+(([.,])?\d+)?)(?=[\+\-\*\/\);])
|(?<=[\+\-\*\/])(\d+(([.,])?\d+)?)$
|[\+\-\*\/\(\)\;]
|(?<=[\+\-\*\/\)\(;])@M(\[\d+\]\()?
|^@M(\[\d+\]\()?
|(?<!@)\w*[A-Za-z]\w*(\[(\d+(([.,])?\d+)?)\])?
```

**Components:**
1. **Constants**: Decimal numbers with `.` or `,`
2. **Operators**: `+`, `-`, `*`, `/`, `(`, `)`, `;`
3. **Special Function**: `@M[n](...)` with optional opening
4. **Identifiers**: Alphanumeric with `_`, requires at least one letter
5. **Max Grade Suffix**: `[N]` where N is a double

**Test Your Regex:** [regexr.com](https://regexr.com/)

### RPN Conversion (Shunting Yard)

**Input:** `(0.4 * (AV1 + AV2) / 2)`

**Output (RPN):** `0.4 AV1 AV2 + * 2 /`

**Evaluation:**
```
Stack: []
Token: 0.4    → Stack: [0.4]
Token: AV1    → Stack: [0.4, 7.0]  // Assume AV1 = 7.0
Token: AV2    → Stack: [0.4, 7.0, 8.0]  // Assume AV2 = 8.0
Token: +      → Stack: [0.4, 15.0]
Token: *      → Stack: [6.0]
Token: 2      → Stack: [6.0, 2.0]
Token: /      → Stack: [3.0]
Result: 3.0
```

### Special Function: @M[n](...)

**Purpose:** Select top N grades from a larger set

**Example:**
```
Formula: @M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)
Grades:  5, 8, 3, 9, 7, 4, 6, 10, 2

Top 6: 10, 9, 8, 7, 6, 5
Sum: 45
```

**Constraints:**
- `n` must be an integer
- Number of provided assessments ≥ n
- Semicolon-separated list

---

## 🔒 Security Architecture

### Dual Security Configuration

```java
// REST API Security (Order 1)
@Configuration
@Order(1)
public class SecurityConfig {
    // JWT Bearer token validation
    // RSA public/private key pair
    // Stateless session management
}

// Web Security (Order 2)
@Configuration
@Order(2)
public class WebSecurityConfig {
    // Form-based login
    // Stateful sessions
    // CSRF protection
}
```

### JWT Token Flow

```
1. User → POST /authenticate
         {email, password}

2. API → Validates credentials
      → Generates JWT with RSA private key
      → Returns token

3. User → Subsequent requests with header:
          Authorization: Bearer <token>

4. API → Validates JWT with RSA public key
      → Extracts user info
      → Processes request
```

### Token Structure

```
Header:
{
  "alg": "RS256",
  "typ": "JWT"
}

Payload:
{
  "sub": "user@example.com",
  "roles": ["USER", "ADMIN"],
  "iat": 1234567890,
  "exp": 1234571490
}

Signature:
RSA_SHA256(
  base64(header) + "." + base64(payload),
  privateKey
)
```

### Role-Based Access Control

- **Roles**: Defined in User entity, verified at runtime
- **Authorization**: Method-level security with `@PreAuthorize`
- **Resource Ownership**: Users can only access their own resources

### Environment Variables (Required)

```bash
JWT_PUBLIC_KEY_CONTENT=<RSA public key PEM>
JWT_PRIVATE_KEY_CONTENT=<RSA private key PEM>
DATABASE_URL=jdbc:mysql://host:3306/dbname
DATABASE_USERNAME=user
DATABASE_PASSWORD=pass
```

---

## 📡 API Reference

### Base URL
```
Production: https://localhost
Development: http://localhost:8080
Swagger UI: https://localhost/swagger-ui/index.html
```

### API Versioning
All endpoints are prefixed with `/api/v1/`

### Authentication

#### Register User (Public)
```http
POST /api/v1/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secure123"
}

Response: 201 Created
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

#### Login (Public)
```http
POST /authenticate
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "secure123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

### User Management

#### List All Users
```http
GET /api/v1/users
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
]
```

#### Get User by Email
```http
GET /api/v1/users/{email}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

#### Update User Name
```http
PATCH /api/v1/users/{id}/name
Authorization: Bearer <token>
Content-Type: application/json

{
  "string": "Jane Doe"
}

Response: 200 OK
{
  "id": 1,
  "name": "Jane Doe",
  "email": "john@example.com"
}
```

#### Update User Email
```http
PATCH /api/v1/users/{id}/email
Authorization: Bearer <token>
Content-Type: application/json

{
  "email": "jane@example.com"
}

Response: 200 OK
{
  "id": 1,
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

#### Delete User
```http
DELETE /api/v1/users/{id}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

### Course Management

#### Create Course
```http
POST /api/v1/{userId}/courses
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Database Management",
  "averageMethod": "(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0.6*(AV1+AV2[10])/2)",
  "cutOffGrade": 6.0
}

Response: 201 Created
{
  "id": 1,
  "name": "Database Management",
  "averageMethod": "(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0.6*(AV1+AV2[10])/2)",
  "cutOffGrade": 6.0
}

Notes:
- Auto-creates default projection
- Auto-instantiates assessments from formula
- cutOffGrade defaults to 6.0 if omitted
```

#### List User Courses
```http
GET /api/v1/{userId}/courses
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "name": "Database Management",
    "averageMethod": "(P1+P2)/2",
    "cutOffGrade": 6.0
  }
]
```

#### Update Course Name
```http
PATCH /api/v1/{userId}/courses/{id}/name
Authorization: Bearer <token>
Content-Type: application/json

{
  "string": "Advanced Databases"
}

Response: 200 OK
{
  "id": 1,
  "name": "Advanced Databases",
  "averageMethod": "(P1+P2)/2",
  "cutOffGrade": 6.0
}
```

#### Update Average Method
```http
PATCH /api/v1/{userId}/courses/{id}/method
Authorization: Bearer <token>
Content-Type: application/json

{
  "string": "(P1*0.3 + P2*0.7)"
}

Response: 200 OK
{
  "id": 1,
  "name": "Advanced Databases",
  "averageMethod": "(P1*0.3 + P2*0.7)",
  "cutOffGrade": 6.0
}

Note: Deletes all projections and creates new default
```

#### Update Cut-Off Grade
```http
PATCH /api/v1/{userId}/courses/{id}/cutoffgrade
Authorization: Bearer <token>
Content-Type: application/json

{
  "value": 7.0
}

Response: 200 OK
{
  "id": 1,
  "name": "Advanced Databases",
  "averageMethod": "(P1*0.3 + P2*0.7)",
  "cutOffGrade": 7.0
}
```

#### Get Courses with Projections
```http
GET /api/v1/{userId}/courses/projections
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "name": "Advanced Databases",
    "assessment": [
      {
        "id": 1,
        "identifier": "P1",
        "grade": 0.0,
        "requiredGrade": 7.0
      },
      {
        "id": 2,
        "identifier": "P2",
        "grade": 0.0,
        "requiredGrade": 7.0
      }
    ],
    "finalGrade": 0.0,
    "courseName": "Advanced Databases"
  }
]
```

#### Delete Course
```http
DELETE /api/v1/{userId}/courses/{id}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "name": "Advanced Databases",
  "averageMethod": "(P1*0.3 + P2*0.7)",
  "cutOffGrade": 7.0
}
```

### Projection Management

#### Create Projection
```http
POST /api/v1/{courseId}/projections
Authorization: Bearer <token>
Content-Type: application/json

{
  "string": "Optimistic Scenario"
}

Response: 201 Created
{
  "id": 2,
  "name": "Optimistic Scenario",
  "assessment": [...],
  "finalGrade": 0.0,
  "courseName": "Advanced Databases"
}
```

#### List Course Projections
```http
GET /api/v1/{courseId}/projections
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "name": "Advanced Databases",
    "assessment": [
      {
        "id": 1,
        "identifier": "P1",
        "grade": 7.5,
        "requiredGrade": 0.0
      },
      {
        "id": 2,
        "identifier": "P2",
        "grade": 0.0,
        "requiredGrade": 6.14
      }
    ],
    "finalGrade": 2.25,
    "courseName": "Advanced Databases"
  },
  {
    "id": 2,
    "name": "Optimistic Scenario",
    "assessment": [...],
    "finalGrade": 0.0,
    "courseName": "Advanced Databases"
  }
]
```

#### Update Projection Name
```http
PATCH /api/v1/{courseId}/projections/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "string": "Pessimistic Scenario"
}

Response: 200 OK
{
  "id": 2,
  "name": "Pessimistic Scenario",
  "assessment": [...],
  "finalGrade": 0.0,
  "courseName": "Advanced Databases"
}
```

#### Delete Projection
```http
DELETE /api/v1/{courseId}/projections/{id}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 2,
  "name": "Pessimistic Scenario",
  ...
}
```

#### Delete All Projections
```http
DELETE /api/v1/{courseId}/projections/all
Authorization: Bearer <token>

Response: 204 No Content
```

### Assessment Management

#### List Assessments
```http
GET /api/v1/{projectionId}/assessments
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "identifier": "P1",
    "grade": 7.5,
    "requiredGrade": 0.0
  },
  {
    "id": 2,
    "identifier": "P2",
    "grade": 0.0,
    "requiredGrade": 6.14
  }
]
```

#### Update Assessment Grade
```http
PATCH /api/v1/{projectionId}/assessments/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "value": 8.5
}

Response: 200 OK
{
  "id": 2,
  "identifier": "P2",
  "grade": 8.5,
  "requiredGrade": 0.0
}

Note: Automatically recalculates finalGrade and requiredGrade for all assessments
```

### Error Responses

All errors follow this format:

```json
{
  "statusCode": 400,
  "error": "Detailed error message",
  "path": "/api/v1/2/courses",
  "timestamp": "2025-01-05T10:30:45.123Z"
}
```

**Common Error Codes:**

| Status | Exception | Description |
|--------|-----------|-------------|
| 400 | `IllegalArgumentException` | Invalid formula syntax or parameters |
| 400 | `NoSuchElementException` | Missing operands in expression |
| 400 | `DataIntegrityException` | Duplicate entity (name/email) |
| 404 | `NotFoundArgumentException` | Resource not found |
| 500 | `InternalServerError` | Division by zero or unexpected error |

**Example Error:**
```http
POST /api/v1/1/courses
{
  "name": "Math",
  "averageMethod": "2.5(P_1)+3 / 2.5*(P2#)3"
}

Response: 400 Bad Request
{
  "statusCode": 400,
  "error": "Method for calculating averages not accepted, formula terms are invalid: 2,5----------#-3",
  "path": "/1/courses",
  "timestamp": "2025-01-05T10:30:45.123Z"
}
```

---

## 🗄️ Database Schema

### Entity Relationship Diagram

![Database Schema](images/img.png)

### Schema Overview

```
User (1) ──────< (N) Course (1) ──────< (N) Projection (1) ──────< (N) Assessment
```

### Entities

#### User
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_email UNIQUE (email)
);
```

#### Course
```sql
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    average_method TEXT NOT NULL,
    cut_off_grade DOUBLE DEFAULT 6.0,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_course_name_user UNIQUE (name, user_id)
);
```

#### Projection
```sql
CREATE TABLE projections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    final_grade DOUBLE DEFAULT 0.0,
    course_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT uk_projection_name_course UNIQUE (name, course_id)
);
```

#### Assessment
```sql
CREATE TABLE assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(100) NOT NULL,
    grade DOUBLE DEFAULT 0.0,
    max_grade DOUBLE DEFAULT 10.0,
    required_grade DOUBLE DEFAULT 0.0,
    projection_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (projection_id) REFERENCES projections(id) ON DELETE CASCADE,
    CONSTRAINT uk_assessment_identifier_projection UNIQUE (identifier, projection_id)
);
```

### Constraints

- **Uniqueness**: Name/ID pairs are unique within their parent context
- **Cascade Deletes**: Deleting parent entity removes all children
- **NOT NULL**: Critical fields enforce data integrity
- **Defaults**: Sensible defaults for grades (0.0) and cutoffs (6.0)

### Indexes

```sql
-- Automatically created by JPA/Hibernate
CREATE INDEX idx_course_user ON courses(user_id);
CREATE INDEX idx_projection_course ON projections(course_id);
CREATE INDEX idx_assessment_projection ON assessments(projection_id);
```

### Migration Strategy

**Flyway** manages all schema changes:

```
src/main/resources/db/migration/
├── V1__initial_schema.sql
├── V2__add_timestamps.sql
└── V3__add_indexes.sql
```

**Migration Commands:**
```bash
# Migrations run automatically on application start

# Manual migration info
./mvnw flyway:info

# Validate migrations
./mvnw flyway:validate

# Clean database (DEV ONLY)
./mvnw flyway:clean
```

**Best Practices:**
- Never modify applied migrations in production
- Test migrations locally first
- Use descriptive names: `V{version}__{description}.sql`
- Keep migrations idempotent when possible

---

## 🚀 Deployment

### Docker Deployment (Recommended)

#### Prerequisites
- Docker 20.10+
- Docker Compose 2.0+

#### Option 1: Clone and Build

```bash
# Clone repository
git clone https://github.com/GustavoDaMassa/MediasAPI.git
cd MediasAPI

# Start all services
docker compose up -d

# View logs
docker compose logs -f

# Stop services
docker compose down

# Clean restart (removes volumes)
docker compose down -v
docker compose up --build -d
```

#### Option 2: Pre-built Image

```bash
# Download docker-compose.yaml
curl -O https://raw.githubusercontent.com/GustavoDaMassa/MediasAPI/main/docker-compose.yaml

# Start services
docker compose up -d
```

#### Services

| Service | Port | Description |
|---------|------|-------------|
| Nginx | 443 (HTTPS), 80 (HTTP) | Reverse proxy + SSL |
| MediasAPI | 8080 (internal) | Spring Boot application |
| MySQL | 3306 (internal) | Database |
| Elasticsearch | 9200 (internal) | Log storage |
| Logstash | 5000 (internal) | Log aggregation |
| Kibana | 5601 | Log visualization UI |

#### Environment Configuration

Create `.env` file:

```bash
# Database
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=mediasdb
MYSQL_USER=mediasuser
MYSQL_PASSWORD=mediaspassword
DATABASE_URL=jdbc:mysql://mysql:3306/mediasdb?useSSL=false

# JWT Keys (replace with your own)
JWT_PUBLIC_KEY_CONTENT=-----BEGIN PUBLIC KEY-----\nMIIBIjANBgk...\n-----END PUBLIC KEY-----
JWT_PRIVATE_KEY_CONTENT=-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgk...\n-----END PRIVATE KEY-----

# Application
SPRING_PROFILES_ACTIVE=docker
```

#### Generate RSA Keys

```bash
# Generate private key
openssl genrsa -out private_key.pem 2048

# Extract public key
openssl rsa -in private_key.pem -pubout -out public_key.pem

# Convert to single line for env var
cat public_key.pem | tr '\n' '\\n'
cat private_key.pem | tr '\n' '\\n'
```

#### Health Checks

```bash
# Application health
curl https://localhost/actuator/health

# Liveness probe
curl https://localhost/actuator/health/liveness

# Readiness probe
curl https://localhost/actuator/health/readiness

# Docker health status
docker compose ps
```

### SSL/TLS Configuration

#### Development (Self-Signed Certificate)

Generated automatically in `nginx/certs/`:
```bash
nginx/certs/
├── nginx-selfsigned.crt
└── nginx-selfsigned.key
```

Browser will show security warning - safe to ignore locally.

#### Production (Let's Encrypt)

```bash
# Install certbot
sudo apt install certbot python3-certbot-nginx

# Obtain certificate
sudo certbot --nginx -d yourdomain.com

# Auto-renewal (cron)
0 0 * * * certbot renew --quiet
```

Update `nginx/nginx.conf`:
```nginx
ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
```

### CORS Configuration

Edit `src/main/java/br/com/gustavohenrique/MediasAPI/config/CorsConfig.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "https://yourdomain.com",
        "http://localhost:3000"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### Scaling Considerations

#### Horizontal Scaling
```yaml
# docker-compose.yml
services:
  mediasapi:
    deploy:
      replicas: 3
    ...
```

#### Database Connection Pool
```properties
# application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

#### Caching (Future Enhancement)
- Add Redis for session storage
- Implement Spring Cache for frequent queries
- CDN for static assets

---

## 💻 Development Guide

### Local Development Setup

#### Prerequisites
- Java 17 (JDK)
- Maven 3.8+
- Docker Desktop (for local ELK stack)
- IDE: IntelliJ IDEA / VS Code / Eclipse

#### Setup Steps

```bash
# 1. Clone repository
git clone https://github.com/GustavoDaMassa/MediasAPI.git
cd MediasAPI

# 2. Configure environment
cp .env.example .env
# Edit .env with your configuration

# 3. Start dependencies (MySQL + ELK)
docker compose up -d mysql elasticsearch logstash kibana

# 4. Run application
./mvnw spring-boot:run

# Alternative: Run from IDE
# Open MediasApiApplication.java and run main method
```

#### Build Commands

```bash
# Compile
./mvnw clean compile

# Run tests
./mvnw test

# Run specific test
./mvnw test -Dtest=CourseServiceTest

# Package (creates JAR)
./mvnw clean package

# Skip tests
./mvnw clean package -DskipTests

# Install to local Maven repo
./mvnw clean install

# Build Docker image
docker build -t mediasapi:latest .
```

#### Project Structure

```
MediasAPI/
├── src/
│   ├── main/
│   │   ├── java/br/com/gustavohenrique/MediasAPI/
│   │   │   ├── authentication/       # JWT services
│   │   │   ├── config/              # Spring configurations
│   │   │   ├── controller/
│   │   │   │   ├── rest/v1/         # REST API controllers
│   │   │   │   │   └── mapper/      # DTO mappers
│   │   │   │   └── web/             # Thymeleaf controllers
│   │   │   ├── dtos/                # Data Transfer Objects
│   │   │   ├── exception/           # Global exception handler
│   │   │   ├── model/               # JPA entities
│   │   │   ├── repository/          # Spring Data repositories
│   │   │   └── service/
│   │   │       ├── Impl/            # Service implementations
│   │   │       └── Interfaces/      # Service contracts
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-docker.properties
│   │       ├── db/migration/        # Flyway migrations
│   │       ├── static/              # CSS, JS
│   │       └── templates/           # Thymeleaf templates
│   └── test/
│       └── java/                    # Test classes
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

#### Running Tests

```bash
# All tests
./mvnw test

# With coverage
./mvnw test jacoco:report
# View: target/site/jacoco/index.html

# Integration tests only
./mvnw verify -DskipUnitTests

# Unit tests only
./mvnw test -DskipIntegrationTests
```

#### Code Style

Project uses default Java conventions:
- 4 spaces indentation
- Max line length: 120 characters
- Javadoc for public APIs

#### Adding New Endpoints

1. **Create DTO:**
```java
// src/main/java/.../ dtos/NewFeatureDTO.java
public record NewFeatureDTO(String name, Double value) {}
```

2. **Add Service Method:**
```java
// src/main/java/.../service/Interfaces/INewFeatureService.java
public interface INewFeatureService {
    NewFeatureDTO create(NewFeatureDTO dto);
}
```

3. **Implement Service:**
```java
// src/main/java/.../service/Impl/NewFeatureServiceImpl.java
@Service
public class NewFeatureServiceImpl implements INewFeatureService {
    @Override
    public NewFeatureDTO create(NewFeatureDTO dto) {
        // Implementation
    }
}
```

4. **Create Controller:**
```java
// src/main/java/.../controller/rest/v1/NewFeatureController.java
@RestController
@RequestMapping("/api/v1/features")
public class NewFeatureController {

    @PostMapping
    public ResponseEntity<NewFeatureDTO> create(@RequestBody NewFeatureDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }
}
```

5. **Write Tests:**
```java
// src/test/java/.../NewFeatureControllerTest.java
@SpringBootTest
@AutoConfigureMockMvc
class NewFeatureControllerTest {

    @Test
    void shouldCreateNewFeature() {
        // Test implementation
    }
}
```

#### Database Migrations

```bash
# Create new migration
# File: src/main/resources/db/migration/V4__add_new_table.sql

CREATE TABLE new_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

# Migration runs automatically on next startup
./mvnw spring-boot:run
```

---

## 📊 Monitoring & Observability

### Structured Logging

#### JSON Log Format

```json
{
  "@timestamp": "2025-01-05T10:30:45.123Z",
  "level": "INFO",
  "logger_name": "br.com.gustavohenrique.MediasAPI.controller.rest.v1.CourseController",
  "message": "Creating new course for user 1",
  "thread_name": "http-nio-8080-exec-1",
  "userEmail": "john@example.com"
}
```

#### MDC Context

Automatically adds user email to all logs:

```java
// MdcFilter.java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if (auth != null && auth.getPrincipal() instanceof UserDetails) {
    String email = auth.getName();
    MDC.put("userEmail", email);
}
```

### ELK Stack Setup

#### Start ELK Services

```bash
# Start full stack
docker compose up -d

# Start ELK only (run app from IDE)
docker compose up -d elasticsearch logstash kibana
```

#### Configure Kibana

1. **Access Kibana**: http://localhost:5601

2. **Create Index Pattern**:
   - Go to Management → Stack Management → Index Patterns
   - Click "Create index pattern"
   - Pattern: `mediasapi-logs-*`
   - Time field: `@timestamp`
   - Click "Create"

3. **View Logs**:
   - Go to Analytics → Discover
   - Select `mediasapi-logs-*` index

#### Log Queries

**Search by user:**
```
userEmail: "john@example.com"
```

**Filter by level:**
```
level: "ERROR"
```

**Filter by time range:**
```
@timestamp: [now-1h TO now]
```

**Complex query:**
```
level: "ERROR" AND userEmail: "john@example.com" AND message: *"calculation"*
```

### Health Checks

#### Endpoints

```bash
# Overall health
curl http://localhost:8080/actuator/health
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 123456789012,
        "threshold": 10485760
      }
    }
  }
}

# Liveness (for K8s)
curl http://localhost:8080/actuator/health/liveness
{
  "status": "UP"
}

# Readiness (for load balancers)
curl http://localhost:8080/actuator/health/readiness
{
  "status": "UP"
}
```

#### Docker Health Check

Configured in `docker-compose.yml`:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 40s
```

### Metrics (Future Enhancement)

Enable additional actuator endpoints:

```properties
# application.properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.metrics.export.prometheus.enabled=true
```

Access metrics:
```bash
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/prometheus
```

---

## 🔧 Troubleshooting

### Common Issues

#### 1. Application Won't Start

**Symptom:** Error on startup about database connection

```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**Solution:**
```bash
# Check MySQL is running
docker compose ps

# Check MySQL logs
docker compose logs mysql

# Wait for MySQL health check
docker compose up -d mysql
sleep 30
docker compose up mediasapi
```

#### 2. JWT Token Invalid

**Symptom:** 401 Unauthorized on authenticated endpoints

```json
{
  "statusCode": 401,
  "error": "Invalid JWT token"
}
```

**Solution:**
- Verify JWT keys are correctly set in environment
- Check token hasn't expired
- Ensure `Authorization: Bearer <token>` header format

```bash
# Test authentication
curl -X POST https://localhost/authenticate \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"pass123"}'

# Use returned token
curl -X GET https://localhost/api/v1/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

#### 3. Flyway Migration Fails

**Symptom:** Error about migration checksum mismatch

```
FlywayException: Validate failed: Migration checksum mismatch
```

**Solution (Development Only):**
```bash
# Stop application
docker compose down

# Remove database volume
docker volume rm mediasapi_mysql-data

# Restart
docker compose up -d
```

**Production:** Never modify applied migrations. Create new migration to fix.

#### 4. CORS Errors

**Symptom:** Browser console shows CORS policy error

```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution:**

Edit `src/main/java/.../config/CorsConfig.java`:
```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "https://yourfrontend.com"
));
```

Rebuild and restart application.

#### 5. SSL Certificate Warning

**Symptom:** Browser shows "Your connection is not private"

**Solution (Development):**
- This is expected with self-signed certificates
- Click "Advanced" → "Proceed to localhost (unsafe)"

**Solution (Production):**
- Replace with Let's Encrypt certificate
- See [SSL/TLS Configuration](#ssltls-configuration)

#### 6. Formula Parsing Error

**Symptom:** 400 Bad Request when creating course

```json
{
  "error": "Method for calculating averages not accepted, formula terms are invalid"
}
```

**Solution:**
- Check formula syntax: [regexr.com](https://regexr.com/)
- Ensure identifiers contain at least one letter
- Verify parentheses are balanced
- Use semicolons `;` in `@M[n](...)` function

**Valid:**
```
(P1 + P2) / 2
@M[3](A1;A2;A3;A4)
(0.4 * EXAM1[20]) + (0.6 * EXAM2)
```

**Invalid:**
```
P1 + P2 / 2            # Missing parentheses for precedence
@M[3](A1,A2,A3)        # Commas instead of semicolons
0.4 * 123              # Identifier "123" has no letters
```

#### 7. Logs Not Appearing in Kibana

**Symptom:** No logs visible in Kibana Discover

**Solution:**
```bash
# Check Logstash is receiving logs
docker compose logs logstash | grep "Pipeline started"

# Check Elasticsearch
curl http://localhost:9200/_cat/indices
# Should see mediasapi-logs-* indices

# Verify index pattern in Kibana
# Management → Index Patterns → mediasapi-logs-*

# Check application is sending logs
docker compose logs mediasapi | tail -20
```

#### 8. Out of Memory Error

**Symptom:** Application crashes with OOM

```
java.lang.OutOfMemoryError: Java heap space
```

**Solution:**

Edit `docker-compose.yml`:
```yaml
services:
  mediasapi:
    environment:
      JAVA_OPTS: "-Xms512m -Xmx2g"
```

Or run locally:
```bash
export MAVEN_OPTS="-Xmx2g"
./mvnw spring-boot:run
```

### Debug Mode

Enable debug logging:

```properties
# application.properties
logging.level.br.com.gustavohenrique.MediasAPI=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Getting Help

1. **Check Logs**: Always check application and Docker logs first
2. **GitHub Issues**: [Report bugs](https://github.com/GustavoDaMassa/MediasAPI/issues)
3. **Documentation**: Review this README and CLAUDE.md
4. **Swagger UI**: Test endpoints at https://localhost/swagger-ui/index.html

---

## 📚 Additional Resources

### Official Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA Guide](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Docker Compose Reference](https://docs.docker.com/compose/)

### Related Projects

- **.NET Implementation**: [dotNetMediasAPI](https://github.com/GustavoDaMassa/dotNetMediasAPI)
- **Node.js Implementation**: [nodeMediasAPI](https://github.com/GustavoDaMassa/nodeMediasAPI)

### Tools

- **API Testing**: [Postman](https://www.postman.com/)
- **Database Client**: [MySQL Workbench](https://www.mysql.com/products/workbench/)
- **Regex Testing**: [RegExr](https://regexr.com/)
- **JWT Decoder**: [jwt.io](https://jwt.io/)

### CI/CD

GitHub Actions workflow: `.github/workflows/ci.yml`

```yaml
on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Build and Test
        run: ./mvnw clean test
```

---

## 📄 License

This project is licensed under the MIT License.

---

## 👤 Author

**Gustavo Henrique**
- GitHub: [@GustavoDaMassa](https://github.com/GustavoDaMassa)
- Email: gustavo.pereira@discente.ufg.br

---

## 🙏 Acknowledgments

- Federal University of Goiás (UFG)
- Spring Boot Community
- Open Source Contributors

---