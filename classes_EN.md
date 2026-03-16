# MediasAPI — Class Map


<details id="dir-root">
<summary><strong>/ (root)</strong></summary>

<blockquote>

- [Dockerfile](Dockerfile) — Docker image of the Spring Boot application
- [docker-compose.yaml](docker-compose.yaml) — local development environment (app + MySQL + Nginx + ELK)
- [docker-compose.prod.yml](docker-compose.prod.yml) — production environment on the home server
- [pom.xml](pom.xml) — Maven dependencies and build
- [mvnw](mvnw) / [mvnw.cmd](mvnw.cmd) — Maven Wrapper (run without Maven installed)
- [start.sh](start.sh) — auxiliary startup script
- [.env.example](.env.example) — environment variables template
- [README.md](README.md) / [README_EN.md](README_EN.md) — project documentation (PT/EN)
- [HELP.md](HELP.md) — auto-generated Spring Boot reference guide
- [.gitignore](.gitignore) / [.gitattributes](.gitattributes) — Git configuration

</blockquote>

</details>


---


<details id="dir-github">
<summary><strong>.github/</strong></summary>

<blockquote>

- [SECRETS.md](.github/SECRETS.md) — documentation of secrets required in GitHub Actions

<details id="dir-github-workflows">
<summary><strong>workflows/</strong></summary>

<blockquote>

- [ci.yml](.github/workflows/ci.yml) — CI/CD pipeline: build, tests and image publication on Docker Hub

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-images">
<summary><strong>images/</strong></summary>

<blockquote>

- [img.png](images/img.png), [img_1.png](images/img_1.png) … [img_19.png](images/img_19.png) — screenshots of API endpoints and responses
- [authenticate.png](images/authenticate.png) — example of the authentication endpoint
- [cadastrarusuarios.png](images/cadastrarusuarios.png) — example of the user registration endpoint
- [projeções.png](images/projeções.png) — example of projection listing

</blockquote>

</details>


<details id="dir-infra">
<summary><strong>infra/</strong></summary>

<blockquote>

- [main.tf](infra/main.tf) — main AWS resources (EC2, Security Group, Elastic IP)
- [variables.tf](infra/variables.tf) — Terraform variable declarations
- [outputs.tf](infra/outputs.tf) — Terraform outputs (public IP, IDs)
- [terraform.tfvars.example](infra/terraform.tfvars.example) — variable values template
- [user-data.sh](infra/user-data.sh) — EC2 instance bootstrap script
- [README.md](infra/README.md) — infrastructure documentation

</blockquote>

</details>


<details id="dir-logstash">
<summary><strong>logstash/</strong></summary>

<blockquote>

<details id="dir-logstash-pipeline">
<summary><strong>pipeline/</strong></summary>

<blockquote>

- [logstash.conf](logstash/pipeline/logstash.conf) — Logstash pipeline: receives JSON logs via TCP (port 5000) and sends them to Elasticsearch

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-nginx">
<summary><strong>nginx/</strong></summary>

<blockquote>

- [nginx.conf](nginx/nginx.conf) — local development configuration (HTTPS with self-signed certificate)
- [nginx.prod.conf](nginx/nginx.prod.conf) — production configuration (plain HTTP, SSL terminated by Cloudflare)

<details id="dir-nginx-certs">
<summary><strong>certs/</strong></summary>

<blockquote>

- [nginx.crt](nginx/certs/nginx.crt) — self-signed certificate (local dev only)
- [nginx.key](nginx/certs/nginx.key) — private key for the self-signed certificate (local dev only)

</blockquote>

</details>

</blockquote>

</details>


---


## src/main


<details id="dir-authentication">
<summary><strong>authentication/</strong></summary>

<blockquote>

<details id="authenticationcontroller">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/AuthenticationController.java">AuthenticationController.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

REST authentication endpoint — receives credentials and returns the JWT token

</details>

<details><summary>dependencies</summary>

- `authenticationService : `[`AuthenticationService`](#authenticationservice)

</details>

<details><summary>attributes</summary>

- `logger                : Logger`
- `authenticationService : AuthenticationService`

</details>

<details><summary>methods</summary>

- `authenticate(AuthDto user) : String`

</details>

</blockquote>

</details>



<details id="authenticationservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/AuthenticationService.java">AuthenticationService.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Authenticates credentials via AuthenticationManager and returns a signed JWT token

</details>

<details><summary>dependencies</summary>

- `jwtService            : `[`JwtService`](#jwtservice)
- `authenticationManager : AuthenticationManager`

</details>

<details><summary>attributes</summary>

- `jwtService            : JwtService`
- `authenticationManager : AuthenticationManager`

</details>

<details><summary>methods</summary>

- `authenticate(AuthDto authDto) : String`

</details>

</blockquote>

</details>



<details id="jwtservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/JwtService.java">JwtService.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Generates JWT tokens signed with an RSA key using Spring Security OAuth2 Resource Server

</details>

<details><summary>dependencies</summary>

- `encoder : JwtEncoder   [Spring Security OAuth2]`

</details>

<details><summary>attributes</summary>

- `encoder : JwtEncoder`

</details>

<details><summary>methods</summary>

- `generateToken(Authentication authentication) : String`

</details>

</blockquote>

</details>



<details id="mdcfilter">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/MdcFilter.java">MdcFilter.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Populates the MDC (Mapped Diagnostic Context) with a requestId per request for traceability in logs (ELK Stack)

</details>

<details><summary>extends</summary>

- `OncePerRequestFilter   [Spring Web]`

</details>

<details><summary>methods</summary>

- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void`

</details>

</blockquote>

</details>



<details id="securityconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/SecurityConfig.java">SecurityConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Configures the security chain for the REST API — JWT Bearer Token, public endpoints and OAuth2 Resource Server with RSA keys

</details>

</blockquote>

</details>



<details id="userauthenticated">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/UserAuthenticated.java">UserAuthenticated.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Adapts the Users entity to the Spring Security UserDetails interface (Adapter pattern)

</details>

<details><summary>implements</summary>

- `UserDetails   [Spring Security]`

</details>

<details><summary>dependencies</summary>

- `user : `[`Users`](#users)

</details>

<details><summary>attributes</summary>

- `user : Users`

</details>

<details><summary>methods</summary>

- `getAuthorities() : Collection<? extends GrantedAuthority>`
- `getPassword()    : String`
- `getUsername()    : String`

</details>

</blockquote>

</details>



<details id="userdetailsserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/UserDetailsServiceImpl.java">UserDetailsServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Loads user from the database by email for Spring Security during authentication

</details>

<details><summary>implements</summary>

- `UserDetailsService   [Spring Security]`

</details>

<details><summary>dependencies</summary>

- `userRepository : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `userRepository : UserRepository`

</details>

<details><summary>methods</summary>

- `loadUserByUsername(String email) : UserDetails`

</details>

</blockquote>

</details>



<details id="websecurityconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/WebSecurityConfig.java">WebSecurityConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Configures the security chain for the Thymeleaf Web interface — stateful authentication via form login, session and route protection for `/web/**`

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-config">
<summary><strong>config/</strong></summary>

<blockquote>

<details id="corsconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/config/CorsConfig.java">CorsConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Configures CORS globally for the application via `${cors.allowed-origins}`

</details>

</blockquote>

</details>



<details id="modelmapperconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/config/modelMapperConfig.java">modelMapperConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Registers the ModelMapper bean in the Spring context

</details>

</blockquote>

</details>



<details id="swaggerconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/config/SwaggerConfig.java">SwaggerConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Configures the OpenAPI/Swagger UI documentation for the REST API

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-controller">
<summary><strong>controller/</strong></summary>

<blockquote>

<details id="dir-controller-rest-v1">
<summary><strong>rest/v1/</strong></summary>

<blockquote>

<details id="assessmentcontroller">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/AssessmentController.java">AssessmentController.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

REST endpoints for grade entry and assessment listing within a projection

</details>

<details><summary>dependencies</summary>

- `assessmentService : `[`AssessmentService`](#assessmentservice)
  - `impl/ AssessmentServiceImpl.java`
- `modelMapper       : ModelMapper`

</details>

<details><summary>attributes</summary>

- `logger            : Logger`
- `assessmentService : AssessmentService`
- `modelMapper       : ModelMapper`

</details>

<details><summary>methods</summary>

- `insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto) : ResponseEntity<AssessmentDTO>`
- `showAssessment(Long projectionId)                                  : ResponseEntity<List<AssessmentDTO>>`

</details>

</blockquote>

</details>



<details id="coursecontroller">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/CourseController.java">CourseController.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

REST endpoints for course CRUD and projection listing by user

</details>

<details><summary>dependencies</summary>

- `courseService     : `[`CourseService`](#courseservice)
  - `impl/ CourseServiceImpl.java`
- `projectionService : `[`ProjectionService`](#projectionservice)
  - `impl/ ProjectionServiceImpl.java`
- `modelMapper       : ModelMapper`

</details>

<details><summary>attributes</summary>

- `logger            : Logger`
- `courseService     : CourseService`
- `projectionService : ProjectionService`
- `modelMapper       : ModelMapper`

</details>

<details><summary>methods</summary>

- `createCourse(Long userId, RequestCourseDto course)                    : ResponseEntity<CourseDTO>`
- `showCourses(Long userId)                                              : ResponseEntity<List<CourseDTO>>`
- `updateCourseName(Long userId, Long id, StringRequestDTO nameDto)      : ResponseEntity<CourseDTO>`
- `updateCourseMethod(Long userId, Long id, StringRequestDTO dto)        : ResponseEntity<CourseDTO>`
- `updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)   : ResponseEntity<CourseDTO>`
- `deleteCourse(Long userId, Long id)                                    : ResponseEntity<CourseDTO>`
- `showAllProjections(Long userId)                                       : ResponseEntity<List<ProjectionDTO>>`

</details>

</blockquote>

</details>



<details id="projectioncontroller">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/ProjectionController.java">ProjectionController.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

REST endpoints for projection CRUD within a course

</details>

<details><summary>dependencies</summary>

- `projectionService : `[`ProjectionService`](#projectionservice)
  - `impl/ ProjectionServiceImpl.java`
- `userService       : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `modelMapper       : ModelMapper`
- `mapDTO            : `[`MapDTO`](#mapdto)
  - `impl/ MapProjectionDTO.java`

</details>

<details><summary>attributes</summary>

- `logger            : Logger`
- `projectionService : ProjectionService`
- `userService       : UserService`
- `modelMapper       : ModelMapper`
- `mapDTO            : MapDTO`

</details>

<details><summary>methods</summary>

- `createProjection(Long courseId, StringRequestDTO projectionName)         : ResponseEntity<ProjectionDTO>`
- `showProjections(Long courseId)                                           : ResponseEntity<List<ProjectionDTO>>`
- `updateProjectionName(Long courseId, Long id, StringRequestDTO newName)   : ResponseEntity<ProjectionDTO>`
- `deleteProjection(Long courseId, Long id)                                 : ResponseEntity<ProjectionDTO>`
- `deleteAllProjections(Long courseId)                                      : void`

</details>

</blockquote>

</details>



<details id="usercontroller">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/UserController.java">UserController.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

REST endpoints for user CRUD — create, list, update, delete and find users

</details>

<details><summary>dependencies</summary>

- `userService  : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `modelMapper  : ModelMapper`

</details>

<details><summary>attributes</summary>

- `logger      : Logger`
- `userService : UserService`
- `modelMapper : ModelMapper`

</details>

<details><summary>methods</summary>

- `showUsers()                                              : ResponseEntity<List<UserDTO>>`
- `createAdminUser(LogOnDto users)                          : ResponseEntity<UserDTO>`
- `createUser(LogOnDto users)                               : ResponseEntity<UserDTO>`
- `updateName(Long id, StringRequestDTO nameDto)            : ResponseEntity<UserDTO>`
- `updateEmail(Long id, EmailUpdateDTO emailDTO)            : ResponseEntity<UserDTO>`
- `deleteUser(Long id)                                      : ResponseEntity<UserDTO>`
- `findUser(String email)                                   : ResponseEntity<UserDTO>`

</details>

</blockquote>

</details>



<details id="dir-controller-rest-v1-mapper">
<summary><strong>mapper/</strong></summary>

<blockquote>

<details id="mapdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/mapper/MapDTO.java">MapDTO.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Mapping contract from Projection to ProjectionDTO with nested AssessmentDTO list

</details>

<details><summary>methods</summary>

- `projectionDTO(Projection projection) : ProjectionDTO`

</details>

</blockquote>

</details>



<details id="mapprojectiondto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/mapper/MapProjectionDTO.java">MapProjectionDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Converts Projection (with Assessment list) to ProjectionDTO with nested AssessmentDTOs using ModelMapper

</details>

<details><summary>implements</summary>

- [`MapDTO.java`](#mapdto)

</details>

<details><summary>dependencies</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>attributes</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>methods</summary>

- `projectionDTO(Projection projection) : ProjectionDTO`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-controller-web">
<summary><strong>web/</strong></summary>

<blockquote>

<details id="assessmentgradecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/AssessmentGradeControllerWeb.java">AssessmentGradeControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for entering assessment grades via the web interface

</details>

</blockquote>

</details>



<details id="authenticationcontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/AuthenticationControllerWeb.java">AuthenticationControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for login and logout via the web interface

</details>

</blockquote>

</details>



<details id="coursecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/CourseControllerWeb.java">CourseControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for listing and managing courses via the web interface

</details>

</blockquote>

</details>



<details id="coursecreatecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/CourseCreateControllerWeb.java">CourseCreateControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for creating courses via the web interface

</details>

</blockquote>

</details>



<details id="projectioncontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/ProjectionControllerWeb.java">ProjectionControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for listing and managing projections via the web interface

</details>

</blockquote>

</details>



<details id="projectioncreatecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/ProjectionCreateControllerWeb.java">ProjectionCreateControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for creating projections via the web interface

</details>

</blockquote>

</details>



<details id="registercontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/RegisterControllerWeb.java">RegisterControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for registering new users via the web interface

</details>

<details><summary>dependencies</summary>

- `userService : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`

</details>

</blockquote>

</details>



<details id="usercontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/UserControllerWeb.java">UserControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Thymeleaf controller for viewing and editing the user profile via the web interface

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-dtos">
<summary><strong>dtos/</strong></summary>

<blockquote>

<details id="assessmentdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/AssessmentDTO.java">AssessmentDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Assessment response representation in REST responses

</details>

<details><summary>attributes</summary>

- `id            : Long`
- `identifier    : String`
- `grade         : double`
- `requiredGrade : double`

</details>

</blockquote>

</details>



<details id="authdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/AuthDto.java">AuthDto.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Login credentials (email + password) sent to the authentication endpoint

</details>

<details><summary>attributes</summary>

- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="coursedto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/CourseDTO.java">CourseDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Course response representation in REST responses

</details>

<details><summary>attributes</summary>

- `id            : Long`
- `name          : String`
- `averageMethod : String`
- `cutOffGrade   : double`

</details>

</blockquote>

</details>



<details id="doublerequestdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/DoubleRequestDTO.java">DoubleRequestDTO.java</a> [record]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Generic wrapper for updating a single double-type field in any entity

</details>

<details><summary>attributes</summary>

- `value : double`

</details>

</blockquote>

</details>



<details id="emailupdatedto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/EmailUpdateDTO.java">EmailUpdateDTO.java</a> [record]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Input data for user email update

</details>

<details><summary>attributes</summary>

- `email : String`

</details>

</blockquote>

</details>



<details id="logondto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/LogOnDto.java">LogOnDto.java</a> [record]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Input data for new user registration

</details>

<details><summary>attributes</summary>

- `name     : String`
- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="projectiondto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/ProjectionDTO.java">ProjectionDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Projection response representation with its nested assessments

</details>

<details><summary>dependencies</summary>

- `assessment : List<AssessmentDTO>`

</details>

<details><summary>attributes</summary>

- `id          : Long`
- `name        : String`
- `finalGrade  : double`
- `courseName  : String`
- `assessment  : List<AssessmentDTO>`

</details>

<details><summary>methods</summary>

- `ProjectionDTO(Long id, String name, List<AssessmentDTO> assessment, double finalGrade)`

</details>

</blockquote>

</details>



<details id="requestcoursedto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/RequestCourseDto.java">RequestCourseDto.java</a> [record]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Input data for creating a new course

</details>

<details><summary>attributes</summary>

- `name          : String`
- `averageMethod : String`
- `cutOffGrade   : double`

</details>

</blockquote>

</details>



<details id="stringrequestdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/StringRequestDTO.java">StringRequestDTO.java</a> [record]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Generic wrapper for updating a single String-type field in any entity

</details>

<details><summary>attributes</summary>

- `string : String`

</details>

</blockquote>

</details>



<details id="userdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/UserDTO.java">UserDTO.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

User response representation in REST responses (without exposing the password)

</details>

<details><summary>attributes</summary>

- `id    : Long`
- `name  : String`
- `email : String`
- `role  : Role`

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-exception">
<summary><strong>exception/</strong></summary>

<blockquote>

<details id="assessmentnotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/AssessmentNotFoundException.java">AssessmentNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when an assessment is not found

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>methods</summary>

- `AssessmentNotFoundException(Long id, Long projectionId)`

</details>

</blockquote>

</details>



<details id="coursenotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/CourseNotFoundException.java">CourseNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a course is not found

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>methods</summary>

- `CourseNotFoundException(Long id, Long userId)`
- `CourseNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="dataintegrityexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/DataIntegrityException.java">DataIntegrityException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 400 on data integrity violations (duplicate field, constraint)

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

<details><summary>methods</summary>

- `DataIntegrityException(String attribute)`

</details>

</blockquote>

</details>



<details id="globalexceptionhandler">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/GlobalExceptionHandler.java">GlobalExceptionHandler.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Intercepts exceptions and maps them to standardized HTTP responses (@RestControllerAdvice)

</details>

<details><summary>attributes</summary>

- `logger : Logger`

</details>

<details><summary>methods</summary>

- `notFound(NotFoundArgumentException, HttpServletRequest)            : ResponseEntity<StandardError>`
- `illegalArgument(IllegalArgumentException, HttpServletRequest)      : ResponseEntity<StandardError>`
- `noSuchElement(NoSuchElementException, HttpServletRequest)          : ResponseEntity<StandardError>`
- `DataIntegrity(DataIntegrityException, HttpServletRequest)          : ResponseEntity<StandardError>`
- `Dividebyzero(InternalServerError, HttpServletRequest)              : ResponseEntity<StandardError>`
- `handleConstraintViolation(ConstraintViolationException, HttpServletRequest) : ResponseEntity<StandardError>`
- `accessDenied(AccessDeniedException, HttpServletRequest)           : ResponseEntity<StandardError>`

</details>

</blockquote>

</details>



<details id="notfoundargumentexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/NotFoundArgumentException.java">NotFoundArgumentException.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Abstract base for all resource-not-found exceptions (404)

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

<details><summary>methods</summary>

- `NotFoundArgumentException(String message)`

</details>

</blockquote>

</details>



<details id="projectionnotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/ProjectionNotFoundException.java">ProjectionNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a projection is not found

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>methods</summary>

- `ProjectionNotFoundException(Long id, Long courseId)`
- `ProjectionNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="standarderror">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/StandardError.java">StandardError.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Standardized error response DTO for HTTP errors — returned by GlobalExceptionHandler

</details>

<details><summary>attributes</summary>

- `timestamp    : LocalDateTime`
- `statusCode   : Integer`
- `error        : String`
- `path         : String`

</details>

<details><summary>methods</summary>

- `StandardError(Integer statusCode, String error, String path)`

</details>

</blockquote>

</details>



<details id="usernotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/UserNotFoundException.java">UserNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Throws 404 when a user is not found by ID or email

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>methods</summary>

- `UserNotFoundException(Long id)`
- `UserNotFoundException(String email)`

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-model">
<summary><strong>model/</strong></summary>

<blockquote>

<details id="assessment">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Assessment.java">Assessment.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Individual assessment — stores grade, maxValue and minimum required grade; protects the grade field via invariant

</details>

<details><summary>dependencies</summary>

- `projection : `[`Projection`](#projection)

</details>

<details><summary>attributes</summary>

- `id            : Long`
- `identifier    : String`
- `grade         : double   [no setter — protected by applyGrade()]`
- `maxValue      : double`
- `requiredGrade : double`
- `fixed         : boolean  [no setter — set by applyGrade()]`
- `projection    : Projection`

</details>

<details><summary>methods</summary>

- `Assessment(String identifier, double maxValue, Projection projection)`
- `Assessment(String identifier, double grade, double maxValue, Projection projection)`
- `applyGrade(double grade) : void   [sole entry point for grade/fixed]`

</details>

</blockquote>

</details>



<details id="course">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Course.java">Course.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Course entity — defines the average calculation method and cut-off grade; contains grade projections

</details>

<details><summary>dependencies</summary>

- `user       : `[`Users`](#users)
- `projection : List<Projection>`

</details>

<details><summary>attributes</summary>

- `id            : Long`
- `name          : String`
- `averageMethod : String`
- `cutOffGrade   : double`
- `user          : Users`
- `projection    : List<Projection>`

</details>

</blockquote>

</details>



<details id="projection">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Projection.java">Projection.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Grade calculation scenario within a course; groups assessments and stores the final grade

</details>

<details><summary>dependencies</summary>

- `course      : `[`Course`](#course)
- `assessment  : List<Assessment>`

</details>

<details><summary>attributes</summary>

- `id          : Long`
- `name        : String`
- `finalGrade  : double`
- `course      : Course`
- `assessment  : List<Assessment>`

</details>

<details><summary>methods</summary>

- `Projection(Course course, String name)`
- `addAssessment(Assessment assessment) : void`

</details>

</blockquote>

</details>



<details id="role">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Role.java">Role.java</a> [enum]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Defines the application access roles

</details>

<details><summary>values</summary>

- `ADMIN`
- `USER`

</details>

</blockquote>

</details>



<details id="users">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Users.java">Users.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Main entity — owner of courses; root of the user data aggregate

</details>

<details><summary>dependencies</summary>

- `course : List<Course>`

</details>

<details><summary>attributes</summary>

- `id           : Long`
- `name         : String`
- `email        : String`
- `password     : String`
- `role         : Role`
- `course       : List<Course>`

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-repository">
<summary><strong>repository/</strong></summary>

<blockquote>

<details id="assessmentrepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/AssessmentRepository.java">AssessmentRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Assessment data access — specialized queries for the RPN pipeline (largest maxValue, lookup by identifier)

</details>

<details><summary>extends</summary>

- `JpaRepository<Assessment, Long>`

</details>

<details><summary>methods</summary>

- `findByProjection(Projection projection)                           : List<Assessment>`
- `findByIndentifier(String identifier, Long projectionId)           : Assessment  [@Query native]`
- `getBiggerMaxValue(Long projectionId)                              : Double  [@Query native]`
- `findByProjectionIdAndId(Long projectionId, Long id)              : Optional<Assessment>`
- `existsByProjectionAndIdentifier(Projection projection, String id) : boolean`

</details>

</blockquote>

</details>



<details id="courserepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/CourseRepository.java">CourseRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Course data access — filters by user and native queries for safe deletion

</details>

<details><summary>extends</summary>

- `JpaRepository<Course, Long>`

</details>

<details><summary>methods</summary>

- `findByUser(Users user)                        : List<Course>`
- `findByUserAndId(Users user, Long id)          : Optional<Course>`
- `existsByUserAndName(Users user, String name)  : boolean`
- `deleteCourse(Long id, Long userId)            : void  [@Query native]`

</details>

</blockquote>

</details>



<details id="projectionrepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/ProjectionRepository.java">ProjectionRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Projection data access — supports cascade deletion and listing by user via native query

</details>

<details><summary>extends</summary>

- `JpaRepository<Projection, Long>`

</details>

<details><summary>methods</summary>

- `findByCourse(Course course)                                     : List<Projection>`
- `findByCourseAndId(Course course, Long id)                       : Optional<Projection>`
- `existsByCourseAndName(Course course, String name)               : boolean`
- `deleteAllByCourse(Long courseId, Long userId)                   : void  [@Query native]`
- `deleteProjection(Long id, Long courseId)                        : void  [@Query native]`
- `findAllByUserId(Long userId)                                    : List<Projection>  [@Query native]`

</details>

</blockquote>

</details>



<details id="userrepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/UserRepository.java">UserRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

User data access — custom queries by email for authentication and validation

</details>

<details><summary>extends</summary>

- `JpaRepository<Users, Long>`

</details>

<details><summary>methods</summary>

- `existsByEmail(String email)      : boolean`
- `findByEmail(String email)        : Optional<Users>`

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-service">
<summary><strong>service/</strong></summary>

<blockquote>

<details id="formulatokens">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/FormulaTokens.java">FormulaTokens.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Token classification utility — determines whether a token is a number, operator, function or assessment identifier

</details>

<details><summary>attributes</summary>

- `FORMULA_IDENTIFIER_REGEX : String  [final static]`

</details>

<details><summary>methods</summary>

- `isIdentifier(String token) : boolean`
- `isNumber(String token)     : boolean`
- `isOperator(String token)   : boolean`
- `isFunction(String token)   : boolean`
- `cleanBrackets(String token): String`

</details>

</blockquote>

</details>



<details id="ownershipvalidationservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/OwnershipValidationService.java">OwnershipValidationService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Identity verification contract — compares the owner ID with the authenticated user's ID

</details>

<details><summary>methods</summary>

- `validate(Long resourceOwnerId) : void`

</details>

</blockquote>

</details>



<details id="rpnevaluator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/RpnEvaluator.java">RpnEvaluator.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Evaluates an RPN expression using a stack — resolves identifiers via IdentifierResolver injected as a lambda

</details>

<details><summary>dependencies</summary>

- `resolver : IdentifierResolver   [@FunctionalInterface — passed as parameter]`

</details>

<details><summary>methods</summary>

- `evaluate(List<String> tokens, Long projectionId, IdentifierResolver resolver) : double`

</details>

</blockquote>

</details>



<details id="dir-service-impl">
<summary><strong>Impl/</strong></summary>

<blockquote>

<details id="assessmentserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/AssessmentServiceImpl.java">AssessmentServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Manages assessments — creates them from the projection formula, lists and inserts grades triggering recalculation of the final grade and minimum required grade via the RPN pipeline

</details>

<details><summary>extends</summary>

- [`OwnedResourceService.java`](#ownedresourceservice)
  - `implements/`
  - `│   └── OwnershipValidator.java`
  - `dependencies/`
  - `    └── ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>implements</summary>

- [`AssessmentService.java`](#assessmentservice)
  - `extends/`
  - `    └── OwnershipValidator.java`

</details>

<details><summary>dependencies</summary>

- `projectionRepository   : `[`ProjectionRepository`](#projectionrepository)
  - `impl/ JPA`
- `assessmentRepository   : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `identifiersDefinition  : `[`IIdentifiersDefinition`](#iidentifiersdefinition)
  - `impl/ IdentifiersDefinitionImpl.java`
- `calculateFinalGrade    : `[`ICalculateFinalGrade`](#icalculatefinalgrade)
  - `impl/ CalculateFinalGradeImpl.java`
- `calculateRequiredGrade : `[`ICalculateRequiredGrade`](#icalculaterequiredgrade)
  - `impl/ CalculateRequiredGradeImpl.java`

</details>

<details><summary>attributes</summary>

- `projectionRepository   : ProjectionRepository`
- `assessmentRepository   : AssessmentRepository`
- `identifiersDefinition  : IIdentifiersDefinition`
- `calculateFinalGrade    : ICalculateFinalGrade`
- `calculateRequiredGrade : ICalculateRequiredGrade`

</details>

<details><summary>methods</summary>

- `createAssessment(Projection projection)                       : void`
- `listAssessment(Long projectionId)                             : List<Assessment>`
- `insertGrade(Long projectionId, Long id, DoubleRequestDTO dto) : Assessment`
- `resolveOwnerId(Long projectionId)                             : Long    [@Override — retrieves userId from projectionId]`
- `validateProjection(Long projectionId)                         : void`

</details>

</blockquote>

</details>



<details id="calculatefinalgradeimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateFinalGradeImpl.java">CalculateFinalGradeImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

4th step — executes the full RPN pipeline and persists the calculated final grade on the projection

</details>

<details><summary>implements</summary>

- [`ICalculateFinalGrade.java`](#icalculatefinalgrade)

</details>

<details><summary>dependencies</summary>

- `projectionRepository    : `[`ProjectionRepository`](#projectionrepository)
  - `impl/ JPA`
- `assessmentRepository    : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `convertToPolishNotation : `[`IConvertToPolishNotation`](#iconverttopolishnotation)
  - `impl/ ConvertToPolishNotationReverseImpl.java`
- `rpnEvaluator            : `[`RpnEvaluator`](#rpnevaluator)

</details>

<details><summary>attributes</summary>

- `projectionRepository    : ProjectionRepository`
- `assessmentRepository    : AssessmentRepository`
- `convertToPolishNotation : IConvertToPolishNotation`
- `rpnEvaluator            : RpnEvaluator`

</details>

<details><summary>methods</summary>

- `calculateResult(Projection projection, String averageMethod) : void`

</details>

</blockquote>

</details>



<details id="calculaterequiredgradeimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateRequiredGradeImpl.java">CalculateRequiredGradeImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

5th step — for each assessment without a grade, uses binary simulation to find and persist the minimum required grade to reach the course cut-off grade

</details>

<details><summary>implements</summary>

- [`ICalculateRequiredGrade.java`](#icalculaterequiredgrade)

</details>

<details><summary>dependencies</summary>

- `assessmentRepository    : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `convertToPolishNotation : `[`IConvertToPolishNotation`](#iconverttopolishnotation)
  - `impl/ ConvertToPolishNotationReverseImpl.java`
- `simulationResult        : `[`ISimulationResult`](#isimulationresult)
  - `impl/ SimulationImpl.java`

</details>

<details><summary>attributes</summary>

- `assessmentRepository    : AssessmentRepository`
- `convertToPolishNotation : IConvertToPolishNotation`
- `simulationResult        : ISimulationResult`

</details>

<details><summary>methods</summary>

- `calculateRequiredGrade(Projection projection, Course course) : void`

</details>

</blockquote>

</details>



<details id="converttopolishnotationreverseimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/ConvertToPolishNotationReverseImpl.java">ConvertToPolishNotationReverseImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

3rd step — converts the infix token list to reverse Polish notation (RPN) via the Shunting-yard algorithm

</details>

<details><summary>implements</summary>

- [`IConvertToPolishNotation.java`](#iconverttopolishnotation)

</details>

<details><summary>dependencies</summary>

- `regularExpressionProcessor : `[`IRegularExpressionProcessor`](#iregularexpressionprocessor)
  - `impl/ RegularExpressionProcessorImpl.java`

</details>

<details><summary>attributes</summary>

- `regularExpressionProcessor : IRegularExpressionProcessor`

</details>

<details><summary>methods</summary>

- `convertToPolishNotation(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="courseserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/CourseServiceImpl.java">CourseServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Manages the course lifecycle — CRUD with ownership validation via OwnedResourceService; when deleting a course, delegates cascade deletion to ProjectionService

</details>

<details><summary>extends</summary>

- [`OwnedResourceService.java`](#ownedresourceservice)
  - `implements/`
  - `│   └── OwnershipValidator.java`
  - `dependencies/`
  - `    └── ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>implements</summary>

- [`CourseService.java`](#courseservice)
  - `extends/`
  - `    └── OwnershipValidator.java`

</details>

<details><summary>dependencies</summary>

- `projectionService  : `[`ProjectionService`](#projectionservice)
  - `impl/ ProjectionServiceImpl.java`
- `courseRepository   : `[`CourseRepository`](#courserepository)
  - `impl/ JPA`
- `userRepository     : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `projectionService : ProjectionService`
- `courseRepository  : CourseRepository`
- `userRepository    : UserRepository`

</details>

<details><summary>methods</summary>

- `createCourse(Long userId, RequestCourseDto courseDto)                    : Course`
- `listCourses(Long userId)                                                 : List<Course>`
- `updateCourseName(Long userId, Long id, StringRequestDTO nameDto)         : Course`
- `updateCourseAverageMethod(Long userId, Long id, StringRequestDTO dto)    : Course`
- `updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)      : Course`
- `deleteCourse(Long userId, Long id)                                       : Course`
- `resolveOwnerId(Long userId)                                              : Long    [@Override — returns userId directly]`
- `validateUser(Long userId)                                                : void`

</details>

</blockquote>

</details>



<details id="identifiersdefinitionimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/IdentifiersDefinitionImpl.java">IdentifiersDefinitionImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

2nd step — iterates over the formula tokens and populates each Assessment with the actual values from the database

</details>

<details><summary>implements</summary>

- [`IIdentifiersDefinition.java`](#iidentifiersdefinition)

</details>

<details><summary>dependencies</summary>

- `assessmentRepository : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `assessmentRepository : AssessmentRepository`

</details>

<details><summary>methods</summary>

- `defineIdentifiers(String averageMethod, Projection projection) : void`

</details>

</blockquote>

</details>



<details id="ownedresourceservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/OwnedResourceService.java">OwnedResourceService.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

[Template Method] — defines the fixed ownership validation algorithm (validateOwnership) and delegates owner ID resolution to each subclass (resolveOwnerId)

</details>

<details><summary>implements</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>dependencies</summary>

- `ownershipValidationService : `[`OwnershipValidationService`](#ownershipvalidationservice)
  - `impl/ OwnershipValidationServiceImpl.java`

</details>

<details><summary>attributes</summary>

- `ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>methods</summary>

- `resolveOwnerId(Long resourceId) : Long   [abstract — subclass defines how to obtain the owner ID]`
- `validateOwnership(Long resourceId) : void  [final — calls resolveOwnerId() and delegates to validate()]`

</details>

</blockquote>

</details>



<details id="ownershipvalidationserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/OwnershipValidationServiceImpl.java">OwnershipValidationServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Verifies that the authenticated user (SecurityContext) owns the resource by comparing IDs; throws AccessDeniedException if not

</details>

<details><summary>implements</summary>

- [`OwnershipValidationService.java`](#ownershipvalidationservice)

</details>

<details><summary>dependencies</summary>

- `userService : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`

</details>

<details><summary>attributes</summary>

- `userService : UserService`

</details>

<details><summary>methods</summary>

- `validate(Long resourceOwnerId) : void`

</details>

</blockquote>

</details>



<details id="projectionserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/ProjectionServiceImpl.java">ProjectionServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Manages the projection lifecycle — CRUD with ownership validation; when creating, delegates assessment creation to AssessmentService

</details>

<details><summary>extends</summary>

- [`OwnedResourceService.java`](#ownedresourceservice)
  - `implements/`
  - `│   └── OwnershipValidator.java`
  - `dependencies/`
  - `    └── ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>implements</summary>

- [`ProjectionService.java`](#projectionservice)
  - `extends/`
  - `    └── OwnershipValidator.java`

</details>

<details><summary>dependencies</summary>

- `assessmentService    : `[`AssessmentService`](#assessmentservice)
  - `impl/ AssessmentServiceImpl.java`
- `userRepository       : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `courseRepository     : `[`CourseRepository`](#courserepository)
  - `impl/ JPA`
- `projectionRepository : `[`ProjectionRepository`](#projectionrepository)
  - `impl/ JPA`

</details>

<details><summary>attributes</summary>

- `assessmentService    : AssessmentService`
- `userRepository       : UserRepository`
- `courseRepository     : CourseRepository`
- `projectionRepository : ProjectionRepository`

</details>

<details><summary>methods</summary>

- `createProjection(Long courseId, StringRequestDTO projectionName)            : Projection`
- `listProjection(Long courseId)                                               : List<Projection>`
- `updateProjectionName(Long courseId, Long id, StringRequestDTO newName)      : Projection`
- `deleteProjection(Long courseId, Long id)                                    : Projection`
- `deleteAllProjections(Long courseId, Long userId)                            : void`
- `listAllProjection(Long userId)                                              : List<Projection>`
- `resolveOwnerId(Long courseId)                                               : Long    [@Override — retrieves userId from courseId]`
- `validateCourse(Long courseId)                                               : void`

</details>

</blockquote>

</details>



<details id="regularexpressionprocessorimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/RegularExpressionProcessorImpl.java">RegularExpressionProcessorImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

1st step — tokenizes the formula string (e.g.: "A1*0.4+B2*0.6") into an ordered list of tokens

</details>

<details><summary>implements</summary>

- [`IRegularExpressionProcessor.java`](#iregularexpressionprocessor)

</details>

<details><summary>methods</summary>

- `compileRegex(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="simulationimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/SimulationImpl.java">SimulationImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Simulates the formula result by substituting an assessment with a hypothetical value — basis for the minimum required grade binary search

</details>

<details><summary>implements</summary>

- [`ISimulationResult.java`](#isimulationresult)

</details>

<details><summary>dependencies</summary>

- `assessmentRepository : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `rpnEvaluator         : `[`RpnEvaluator`](#rpnevaluator)

</details>

<details><summary>attributes</summary>

- `assessmentRepository : AssessmentRepository`
- `rpnEvaluator         : RpnEvaluator`

</details>

<details><summary>methods</summary>

- `simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) : double`

</details>

</blockquote>

</details>



<details id="userserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/UserServiceImpl.java">UserServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Manages the user lifecycle — CRUD with password encryption and access to the authenticated user via SecurityContext

</details>

<details><summary>implements</summary>

- [`UserService.java`](#userservice)

</details>

<details><summary>dependencies</summary>

- `userRepository   : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `passwordEncoder  : PasswordEncoder`

</details>

<details><summary>attributes</summary>

- `userRepository  : UserRepository`
- `passwordEncoder : PasswordEncoder`

</details>

<details><summary>methods</summary>

- `create(LogOnDto logOnDto)                             : Users`
- `createAdminUser(LogOnDto logOnDto)                    : Users`
- `updateName(Long id, StringRequestDTO nameDto)         : Users`
- `updateEmail(Long id, EmailUpdateDTO emailDTO)         : Users`
- `deleteUser(Long id)                                   : Users`
- `listUsers()                                           : List<Users>`
- `findusers(String email)                               : Users`
- `getAuthenticatedUser()                                : Users`

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-service-interfaces">
<summary><strong>Interfaces/</strong></summary>

<blockquote>

<details id="assessmentservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/AssessmentService.java">AssessmentService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Assessment CRUD contract with ownership validation inherited from OwnershipValidator

</details>

<details><summary>extends</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>methods</summary>

- `createAssessment(Projection projection)                       : void`
- `listAssessment(Long projectionId)                             : List<Assessment>`
- `insertGrade(Long projectionId, Long id, DoubleRequestDTO dto) : Assessment`

</details>

</blockquote>

</details>



<details id="courseservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/CourseService.java">CourseService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Course CRUD contract with ownership validation inherited from OwnershipValidator

</details>

<details><summary>extends</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>methods</summary>

- `createCourse(Long userId, RequestCourseDto course)                       : Course`
- `listCourses(Long userId)                                                 : List<Course>`
- `updateCourseName(Long userId, Long id, StringRequestDTO nameDto)         : Course`
- `updateCourseAverageMethod(Long userId, Long id, StringRequestDTO dto)    : Course`
- `updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)      : Course`
- `deleteCourse(Long userId, Long id)                                       : Course`

</details>

</blockquote>

</details>



<details id="icalculatefinalgrade">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ICalculateFinalGrade.java">ICalculateFinalGrade.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Contract for the 4th step of the RPN pipeline — executes the RPN expression and persists the projection's final grade

</details>

<details><summary>methods</summary>

- `calculateResult(Projection projection, String averageMethod) : void`

</details>

</blockquote>

</details>



<details id="icalculaterequiredgrade">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ICalculateRequiredGrade.java">ICalculateRequiredGrade.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Contract for the 5th step of the RPN pipeline — calculates the minimum required grade for each assessment without a grade

</details>

<details><summary>methods</summary>

- `calculateRequiredGrade(Projection projection, Course course) : void`

</details>

</blockquote>

</details>



<details id="iconverttopolishnotation">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IConvertToPolishNotation.java">IConvertToPolishNotation.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Contract for the 3rd step of the RPN pipeline — converts infix tokens to reverse Polish notation

</details>

<details><summary>methods</summary>

- `convertToPolishNotation(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="identifierresolver">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IdentifierResolver.java">IdentifierResolver.java</a> [interface — @FunctionalInterface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Functional contract injected into RpnEvaluator — resolves an identifier token to its numeric value

</details>

<details><summary>methods</summary>

- `resolve(String identifier, Long projectionId) : double`

</details>

</blockquote>

</details>



<details id="iidentifiersdefinition">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IIdentifiersDefinition.java">IIdentifiersDefinition.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Contract for the 2nd step of the RPN pipeline — maps formula identifiers to actual assessment values

</details>

<details><summary>methods</summary>

- `defineIdentifiers(String averageMethod, Projection projection) : void`

</details>

</blockquote>

</details>



<details id="iregularexpressionprocessor">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IRegularExpressionProcessor.java">IRegularExpressionProcessor.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Contract for the 1st step of the RPN pipeline — tokenizes the formula string into a token list

</details>

<details><summary>methods</summary>

- `compileRegex(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="isimulationresult">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ISimulationResult.java">ISimulationResult.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Binary simulation contract — tests hypothetical grade values to find the minimum required

</details>

<details><summary>methods</summary>

- `simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) : double`

</details>

</blockquote>

</details>



<details id="ownershipvalidator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/OwnershipValidator.java">OwnershipValidator.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Ownership validation contract — ensures the authenticated user owns the resource

</details>

<details><summary>methods</summary>

- `validateOwnership(Long id) : void`

</details>

</blockquote>

</details>



<details id="projectionservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ProjectionService.java">ProjectionService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

Projection CRUD contract with ownership validation inherited from OwnershipValidator

</details>

<details><summary>extends</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>methods</summary>

- `createProjection(Long courseId, StringRequestDTO projectionName)             : Projection`
- `listProjection(Long courseId)                                                : List<Projection>`
- `updateProjectionName(Long courseId, Long id, StringRequestDTO newName)       : Projection`
- `deleteProjection(Long courseId, Long id)                                     : Projection`
- `deleteAllProjections(Long courseId, Long userId)                             : void`
- `listAllProjection(Long userId)                                               : List<Projection>`

</details>

</blockquote>

</details>



<details id="userservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/UserService.java">UserService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>purpose</summary>

User CRUD and retrieval contract, including access to the authenticated user

</details>

<details><summary>methods</summary>

- `create(LogOnDto users)                              : Users`
- `createAdminUser(LogOnDto logOnDto)                  : Users`
- `updateName(Long id, StringRequestDTO nameDto)       : Users`
- `updateEmail(Long id, EmailUpdateDTO emailDTO)       : Users`
- `deleteUser(Long id)                                 : Users`
- `listUsers()                                         : List<Users>`
- `findusers(String email)                             : Users`
- `getAuthenticatedUser()                              : Users`

</details>

</blockquote>

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-main-resources">
<summary><strong>resources/</strong></summary>

<blockquote>

- [application.properties](src/main/resources/application.properties) — main configuration (profiles, datasource, JWT, CORS, Flyway, Actuator)
- [application-prod.yml](src/main/resources/application-prod.yml) — production overrides (reads secrets from environment variables)
- [application.properties.example](src/main/resources/application.properties.example) — local configuration template
- [logback-spring.xml](src/main/resources/logback-spring.xml) — structured JSON log configuration for Logstash (TCP port 5000)
- [app.key](src/main/resources/app.key) — RSA private key for JWT signing (local dev)
- [app.pub](src/main/resources/app.pub) — RSA public key for JWT validation (local dev)

<details id="dir-main-resources-db">
<summary><strong>db/migration/</strong></summary>

<blockquote>

- [V1__baseline_schema.sql](src/main/resources/db/migration/V1__baseline_schema.sql) — initial schema: tables `users`, `courses`, `projections`, `assessments`
- [V2__add_role_column_to_users.sql](src/main/resources/db/migration/V2__add_role_column_to_users.sql) — adds `role` column to the `users` table
- [README.md](src/main/resources/db/migration/README.md) — best practices guide for creating new migrations

</blockquote>

</details>

<details id="dir-main-resources-templates">
<summary><strong>templates/</strong></summary>

<blockquote>

- [login.html](src/main/resources/templates/login.html) — login screen of the Thymeleaf web interface
- [register.html](src/main/resources/templates/register.html) — user registration screen
- [courses.html](src/main/resources/templates/courses.html) — user course listing
- [courseCreate.html](src/main/resources/templates/courseCreate.html) — course creation form
- [projections.html](src/main/resources/templates/projections.html) — projection listing for a course
- [projectionCreate.html](src/main/resources/templates/projectionCreate.html) — projection creation form
- [userProjections.html](src/main/resources/templates/userProjections.html) — consolidated view of user projections
- [assessmentGrade.html](src/main/resources/templates/assessmentGrade.html) — grade entry form for an assessment

</blockquote>

</details>

</blockquote>

</details>


---

## src/test


<details id="dir-test-root">
<summary><strong>(root)</strong></summary>

<blockquote>

<details id="actuatorendpointstest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/ActuatorEndpointsTest.java">ActuatorEndpointsTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Tests Spring Boot Actuator endpoints (health, liveness, readiness)

</details>

</blockquote>

</details>



<details id="mediasapiapplicationtests">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/MediasApiApplicationTests.java">MediasApiApplicationTests.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Smoke test — verifies that the Spring context starts without errors

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-test-controller-rest-v1">
<summary><strong>controller/rest/v1/</strong></summary>

<blockquote>

<details id="assessmentcontrollertest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/AssessmentControllerTest.java">AssessmentControllerTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Integration tests for the AssessmentController REST endpoints

</details>

</blockquote>

</details>



<details id="coursecontrollertest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/CourseControllerTest.java">CourseControllerTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Integration tests for the CourseController REST endpoints

</details>

</blockquote>

</details>



<details id="projectioncontrollertest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/ProjectionControllerTest.java">ProjectionControllerTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Integration tests for the ProjectionController REST endpoints

</details>

</blockquote>

</details>

</blockquote>

</details>



<details id="dir-test-service-impl">
<summary><strong>service/Impl/</strong></summary>

<blockquote>

<details id="assessmentserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/AssessmentServiceImplTest.java">AssessmentServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for AssessmentServiceImpl with Mockito

</details>

</blockquote>

</details>



<details id="calculatefinalgradeimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateFinalGradeImplTest.java">CalculateFinalGradeImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for CalculateFinalGradeImpl — verifies correct final grade calculation via RPN

</details>

</blockquote>

</details>



<details id="calculaterequiredgradeimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateRequiredGradeImplTest.java">CalculateRequiredGradeImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for CalculateRequiredGradeImpl — verifies minimum required grade calculation

</details>

</blockquote>

</details>



<details id="converttopolishnotationreverseimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/ConvertToPolishNotationReverseImplTest.java">ConvertToPolishNotationReverseImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for ConvertToPolishNotationReverseImpl — verifies infix → RPN conversion

</details>

</blockquote>

</details>



<details id="courseserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/CourseServiceImplTest.java">CourseServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for CourseServiceImpl with Mockito

</details>

</blockquote>

</details>



<details id="identifiersdefinitionimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/IdentifiersDefinitionImplTest.java">IdentifiersDefinitionImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for IdentifiersDefinitionImpl — verifies identifier-to-value mapping

</details>

</blockquote>

</details>



<details id="projectionserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/ProjectionServiceImplTest.java">ProjectionServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for ProjectionServiceImpl with Mockito

</details>

</blockquote>

</details>



<details id="regularexpressionprocessorimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/RegularExpressionProcessorImplTest.java">RegularExpressionProcessorImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for RegularExpressionProcessorImpl — verifies formula tokenization

</details>

</blockquote>

</details>



<details id="simulationimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/SimulationImplTest.java">SimulationImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for SimulationImpl — verifies binary simulation of the minimum required grade

</details>

</blockquote>

</details>



<details id="userserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/UserServiceImplTest.java">UserServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>purpose</summary>

Unit tests for UserServiceImpl with Mockito

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-test-resources">
<summary><strong>resources/</strong></summary>

<blockquote>

- [application-test.properties](src/test/resources/application-test.properties) — test environment configuration: H2 in-memory, Flyway enabled, mocked JWT keys
- [application-local.properties.example](src/test/resources/application-local.properties.example) — template for running tests locally outside Docker

</blockquote>

</details>
