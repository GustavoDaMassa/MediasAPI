# Requirements — MediasAPI

## Overview

REST API and web interface for academic grade management and projection.
Users register courses with custom average formulas, create grade projections, and track what scores are needed to reach the cutoff grade.
The system exposes both a stateless REST API (JWT) and a stateful web interface (Thymeleaf + Form Login).

---

## Functional Requirements

### FR-01 — User Management

| ID | Requirement |
|---|---|
| FR-01.1 | Any visitor can register a new account (role: USER) |
| FR-01.2 | An authenticated ADMIN can create accounts with role ADMIN |
| FR-01.3 | An authenticated ADMIN can list all registered users |
| FR-01.4 | An authenticated user can look up any account by email |
| FR-01.5 | An authenticated user can update their own name |
| FR-01.6 | An authenticated user can update their own email |
| FR-01.7 | An authenticated user can delete their own account |
| FR-01.8 | Deleting a user permanently removes all their courses, projections, and assessments |
| FR-01.9 | Email must be unique across all users |

---

### FR-02 — Authentication (REST)

| ID | Requirement |
|---|---|
| FR-02.1 | A registered user can authenticate with email and password via POST `/authenticate` |
| FR-02.2 | On successful authentication the system returns a JWT signed with RSA (asymmetric key pair) |
| FR-02.3 | All REST endpoints except user registration and login require a valid JWT Bearer token |
| FR-02.4 | The JWT carries the user's role and is used for authorization decisions |
| FR-02.5 | JWT expiration is approximately 4.4 hours (16000 seconds) |
| FR-02.6 | RSA public and private keys are injected via environment variables |

---

### FR-03 — Authentication (Web Interface)

| ID | Requirement |
|---|---|
| FR-03.1 | The web interface is accessible at `/web/**` |
| FR-03.2 | Users authenticate via a form login page at `/web/login` with email and password |
| FR-03.3 | Session is maintained with a JSESSIONID cookie (stateful) |
| FR-03.4 | The web security chain is independent of the REST security chain |

---

### FR-04 — Course Management

| ID | Requirement |
|---|---|
| FR-04.1 | An authenticated user can create a course with a name, average formula, and cutoff grade |
| FR-04.2 | Creating a course automatically creates a default projection with the same name |
| FR-04.3 | An authenticated user can list all their own courses |
| FR-04.4 | An authenticated user can update the name of one of their courses |
| FR-04.5 | An authenticated user can update the average formula of one of their courses |
| FR-04.6 | Updating the average formula deletes all existing projections and creates a new default projection |
| FR-04.7 | An authenticated user can update the cutoff grade of one of their courses |
| FR-04.8 | Updating the cutoff grade triggers recalculation of requiredGrade for all assessments in all projections of that course |
| FR-04.9 | An authenticated user can delete one of their courses |
| FR-04.10 | Deleting a course permanently removes all its projections and assessments |
| FR-04.11 | Course names must be unique per user |
| FR-04.12 | An authenticated user can list all projections across all their courses |

---

### FR-05 — Projection Management

| ID | Requirement |
|---|---|
| FR-05.1 | An authenticated user can create additional projections for one of their courses |
| FR-05.2 | Creating a projection automatically creates the corresponding assessments by parsing the course's average formula |
| FR-05.3 | An authenticated user can list all projections of a course |
| FR-05.4 | An authenticated user can update the name of one of their projections |
| FR-05.5 | An authenticated user can delete a single projection |
| FR-05.6 | An authenticated user can delete all projections of a course at once |
| FR-05.7 | Deleting a projection permanently removes all its assessments |
| FR-05.8 | Projection names must be unique per course |

---

### FR-06 — Assessment Management

| ID | Requirement |
|---|---|
| FR-06.1 | Assessments are created automatically when a projection is created — never manually |
| FR-06.2 | An authenticated user can list all assessments of a projection |
| FR-06.3 | An authenticated user can submit a grade for one of their assessments |
| FR-06.4 | A grade cannot exceed the assessment's maxValue |
| FR-06.5 | Once a grade is submitted the assessment is marked as fixed (immutable grade) |
| FR-06.6 | Submitting a grade triggers recalculation of the projection's finalGrade |
| FR-06.7 | Submitting a grade triggers recalculation of requiredGrade for all assessments in the projection |
| FR-06.8 | Assessment identifiers are unique per projection |
| FR-06.9 | Assessment identifier and maxValue are immutable after creation |

---

### FR-07 — Average Formula (averageMethod)

| ID | Requirement |
|---|---|
| FR-07.1 | The system must parse and validate a custom average formula on course creation |
| FR-07.2 | A valid formula may contain: identifiers (e.g. AV1, AT1), numeric coefficients, arithmetic operators (+, -, *, /), parentheses, and the special function @M |
| FR-07.3 | Identifiers may carry an optional maxValue annotation: AV1[10], AT2[5.5] — default maxValue is 10.0 |
| FR-07.4 | The special function @M[n](...) computes the sum of the n highest values among the semicolon-separated arguments |
| FR-07.5 | Decimal values in formulas may use either dot or comma as separator |
| FR-07.6 | Invalid formula terms must be rejected with a descriptive error message |
| FR-07.7 | Division by zero in formula evaluation must be handled gracefully |
| FR-07.8 | Requesting @M[n] with n greater than the number of provided arguments must be rejected |

---

### FR-08 — Grade Calculation

| ID | Requirement |
|---|---|
| FR-08.1 | The system calculates finalGrade by evaluating the course's formula with each assessment's current grade using a Reverse Polish Notation (RPN) engine |
| FR-08.2 | Before any grades are submitted all assessment grades default to 0.0 |
| FR-08.3 | The system calculates requiredGrade: the minimum grade needed in all unfixed assessments to reach the course's cutOffGrade |
| FR-08.4 | requiredGrade is calculated via binary search simulation |
| FR-08.5 | If the cutoff grade is no longer mathematically achievable, requiredGrade is set to -1 for all unfixed assessments |
| FR-08.6 | requiredGrade for a fixed assessment is always 0.0 |
| FR-08.7 | requiredGrade is capped at the assessment's maxValue |

---

### FR-09 — Ownership and Access Control

| ID | Requirement |
|---|---|
| FR-09.1 | A user can only read and modify their own courses, projections, and assessments |
| FR-09.2 | Accessing a resource that belongs to another user returns 403 Forbidden |
| FR-09.3 | ADMIN users can list all users but follow the same ownership rules for courses and below |

---

### FR-10 — Observability (ELK Stack)

| ID | Requirement |
|---|---|
| FR-10.1 | Application logs are shipped to Elasticsearch via Logstash (TCP port 5000) |
| FR-10.2 | Logs include the authenticated user's email via MDC (Mapped Diagnostic Context) |
| FR-10.3 | Logs are visualizable in Kibana with index pattern `mediasapi-logs-*` |

---

## Non-Functional Requirements

### NFR-01 — Security

| ID | Requirement |
|---|---|
| NFR-01.1 | Passwords are stored hashed using BCrypt |
| NFR-01.2 | REST JWT tokens are signed with RSA (asymmetric key pair injected via environment variables) |
| NFR-01.3 | Web session uses JSESSIONID cookie with Spring Security defaults |
| NFR-01.4 | Private key must never be hardcoded in source code or committed to version control |
| NFR-01.5 | The REST API is stateless — no server-side sessions |
| NFR-01.6 | The two security chains (REST and Web) operate independently with explicit order (REST = Order 1, Web = Order 2) |

### NFR-02 — Architecture

| ID | Requirement |
|---|---|
| NFR-02.1 | The system follows a domain-driven package structure |
| NFR-02.2 | Each domain exposes an interface; the implementation is hidden behind it |
| NFR-02.3 | SOLID principles must be applied throughout |
| NFR-02.4 | Ownership validation follows the Template Method pattern via `OwnedResourceService` |
| NFR-02.5 | All domain exceptions extend a common abstract base class (`NotFoundArgumentException`) |
| NFR-02.6 | Error responses follow a single standard format (`StandardError`) across all endpoints |
| NFR-02.7 | The calculation pipeline components are decoupled via interfaces (IRegularExpressionProcessor, IConvertToPolishNotation, ICalculateFinalGrade, ICalculateRequiredGrade, IIdentifiersDefinition, ISimulationResult) |

### NFR-03 — Quality

| ID | Requirement |
|---|---|
| NFR-03.1 | Every class must have a corresponding unit test |
| NFR-03.2 | Test coverage must exercise all business rules and exception paths |
| NFR-03.3 | Controller tests use MockMvc with mocked dependencies |
| NFR-03.4 | Repository tests use an in-memory database |
| NFR-03.5 | Service tests use Mockito |

### NFR-04 — API Design

| ID | Requirement |
|---|---|
| NFR-04.1 | All REST endpoints follow the `/api/v1/` prefix |
| NFR-04.2 | HTTP methods and status codes follow REST semantics |
| NFR-04.3 | Request and response bodies use JSON |
| NFR-04.4 | Entities are never exposed directly — always mapped to DTOs |
| NFR-04.5 | The API is documented via Swagger/OpenAPI at `/swagger-ui/index.html` |

### NFR-05 — Infrastructure

| ID | Requirement |
|---|---|
| NFR-05.1 | Database schema is managed exclusively by Flyway migrations |
| NFR-05.2 | `ddl-auto` is set to `validate` in production |
| NFR-05.3 | Application configuration uses environment variables for all secrets and environment-specific values |
| NFR-05.4 | The application is containerized with Docker |
| NFR-05.5 | Production docker-compose includes: app, MySQL, Nginx, ELK Stack (Elasticsearch, Logstash, Kibana), Watchtower |
| NFR-05.6 | CI/CD pipeline runs tests on every push and publishes a Docker image on merge to main |
| NFR-05.7 | Watchtower monitors Docker Hub every 30 seconds and auto-deploys new images |

### NFR-06 — Observability

| ID | Requirement |
|---|---|
| NFR-06.1 | Spring Boot Actuator exposes `/actuator/health`, `/actuator/health/liveness`, `/actuator/health/readiness` |
| NFR-06.2 | Application logs are structured JSON shipped to ELK Stack |
| NFR-06.3 | Every authenticated request includes the user's email in the log context (MDC) |
