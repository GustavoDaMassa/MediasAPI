# MediasAPI — Routes

All REST endpoints are prefixed with `/api/v1`. Protected routes require `Authorization: Bearer <token>`.

---

## Authentication

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/authenticate` | Public | Authenticate and receive JWT |

**Request:**
```json
{ "email": "user@email.com", "password": "secret" }
```

**Response:** JWT string (`eyJ...`)

---

## REST API — `/api/v1`

### Users — `/api/v1/users`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/users` | Public | Create user account |
| `POST` | `/api/v1/users/admin` | JWT (ADMIN) | Create admin user |
| `GET` | `/api/v1/users` | JWT (ADMIN) | List all users |
| `GET` | `/api/v1/users/{email}` | JWT | Find user by email |
| `PATCH` | `/api/v1/users/{id}/name` | JWT (owner) | Update user name |
| `PATCH` | `/api/v1/users/{id}/email` | JWT (owner) | Update user email |
| `DELETE` | `/api/v1/users/{id}` | JWT (owner) | Delete user account |

> **owner** — endpoint validates that the authenticated user matches the `{id}` in the path. Attempting to modify another user returns 403.

**Create user request:**
```json
{ "name": "Gustavo", "email": "user@email.com", "password": "secret" }
```

**Response:**
```json
{ "id": 1, "name": "Gustavo", "email": "user@email.com" }
```

---

### Courses — `/api/v1/{userId}/courses`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/{userId}/courses` | JWT | Create course (auto-creates default projection and assessments) |
| `GET` | `/api/v1/{userId}/courses` | JWT | List all courses of a user |
| `GET` | `/api/v1/{userId}/courses/projections` | JWT | List all courses with their projections and assessments |
| `PATCH` | `/api/v1/{userId}/courses/{id}/name` | JWT | Update course name |
| `PATCH` | `/api/v1/{userId}/courses/{id}/method` | JWT | Update average calculation method (recreates projections) |
| `PATCH` | `/api/v1/{userId}/courses/{id}/cutoffgrade` | JWT | Update passing grade |
| `DELETE` | `/api/v1/{userId}/courses/{id}` | JWT | Delete course and all its projections |

**Create course request:**
```json
{
  "name": "SGBD",
  "averageMethod": "(0.4*(@M[6](AT1;AT2;AT3;AT4;AT5;AT6;AT7;AT8;AT9)/6))+(0,6*(AV1+AV2[10]/2))",
  "cutOffGrade": 6.0
}
```

**`averageMethod` syntax:**
- Identifiers must contain at least one letter (e.g. `AV1`, `P_1`)
- `[N]` suffix sets the max grade: `AV2[10]` — default is `10`
- `@M[n](i1;i2;...;im)` sums the `n` highest grades among `m` assessments (`m >= n`)
- Standard arithmetic operators: `+`, `-`, `*`, `/`, `(`, `)`

---

### Projections — `/api/v1/{courseId}/projections`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/{courseId}/projections` | JWT | Create additional projection (auto-creates assessments) |
| `GET` | `/api/v1/{courseId}/projections` | JWT | List all projections of a course with assessments |
| `PATCH` | `/api/v1/{courseId}/projections/{id}` | JWT | Rename projection |
| `DELETE` | `/api/v1/{courseId}/projections/{id}` | JWT | Delete specific projection |
| `DELETE` | `/api/v1/{courseId}/projections/all` | JWT | Delete all projections including the default one |

**Create projection request:**
```json
{ "string": "Optimistic scenario" }
```

**Response includes** — projection with full assessment list, `finalGrade` and `requiredGrade` per assessment.

---

### Assessments — `/api/v1/{projectionId}/assessments`

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/api/v1/{projectionId}/assessments` | JWT | List all assessments of a projection |
| `PATCH` | `/api/v1/{projectionId}/assessments/{id}` | JWT | Submit grade — triggers recalculation of finalGrade and requiredGrade |

**Submit grade request:**
```json
{ "value": 7.5 }
```

**Response:**
```json
{ "id": 12, "identifier": "AV1", "grade": 7.5, "requiredGrade": 0.0 }
```

> After grading, `requiredGrade` for remaining assessments is recalculated uniformly to reach `cutOffGrade`.

---

## Actuator

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/actuator/health` | Public | General application health |
| `GET` | `/actuator/health/liveness` | Public | Liveness probe |
| `GET` | `/actuator/health/readiness` | Public | Readiness probe |

---

## Documentation

| Path | Auth | Description |
|------|------|-------------|
| `/swagger-ui/index.html` | Public | Swagger UI — interactive REST documentation |
| `/v3/api-docs` | Public | OpenAPI 3 JSON spec |

---

## Web Interface (Thymeleaf)

Session-based authentication — separate security chain from the REST API.

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/web/login` | Public | Login page |
| `GET` | `/web/` | Session | Dashboard (projections overview) |
| `GET` | `/web/register` | Public | Registration form |
| `POST` | `/web/register` | Public | Submit registration |
| `GET` | `/web/courses` | Session | List courses |
| `GET` | `/web/courses/{id}/delete` | Session | Delete course |
| `GET` | `/web/courses/create` | Session | New course form |
| `POST` | `/web/courses/create` | Session | Submit new course |
| `GET` | `/web/courses/{courseId}/projections` | Session | List projections of a course |
| `GET` | `/web/courses/{courseId}/projections/{projectionId}/delete` | Session | Delete projection |
| `GET` | `/web/courses/{courseId}/projections/create` | Session | New projection form |
| `POST` | `/web/courses/{courseId}/projections/create` | Session | Submit new projection |
| `GET` | `/web/courses/{courseId}/projections/{projectionId}/assessments/{assessmentId}/grade` | Session | Grade form |
| `POST` | `/web/courses/{courseId}/projections/{projectionId}/assessments/{assessmentId}/grade` | Session | Submit grade |

---

## Authorization Summary

| Scope | Required |
|-------|----------|
| Public | No credentials |
| JWT | `Authorization: Bearer <token>` header |
| JWT (ADMIN) | Valid token with role `ADMIN` |
| JWT (owner) | Valid token — service validates `authenticatedUser.id == pathId` |
| Session | Active session cookie (Thymeleaf web interface) |