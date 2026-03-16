# MediasAPI — Mapa de Classes


<details id="dir-root">
<summary><strong>/ (raiz)</strong></summary>

<blockquote>

- [Dockerfile](Dockerfile) — imagem Docker da aplicação Spring Boot
- [docker-compose.yaml](docker-compose.yaml) — ambiente de desenvolvimento local (app + MySQL + Nginx + ELK)
- [docker-compose.prod.yml](docker-compose.prod.yml) — ambiente de produção no home server
- [pom.xml](pom.xml) — dependências e build Maven
- [mvnw](mvnw) / [mvnw.cmd](mvnw.cmd) — Maven Wrapper (execução sem Maven instalado)
- [start.sh](start.sh) — script de inicialização auxiliar
- [.env.example](.env.example) — template de variáveis de ambiente
- [README.md](README.md) / [README_EN.md](README_EN.md) — documentação do projeto (PT/EN)
- [HELP.md](HELP.md) — guia de referência Spring Boot gerado automaticamente
- [.gitignore](.gitignore) / [.gitattributes](.gitattributes) — configurações Git

</blockquote>

</details>


---


<details id="dir-github">
<summary><strong>.github/</strong></summary>

<blockquote>

- [SECRETS.md](.github/SECRETS.md) — documentação dos secrets necessários no GitHub Actions

<details id="dir-github-workflows">
<summary><strong>workflows/</strong></summary>

<blockquote>

- [ci.yml](.github/workflows/ci.yml) — pipeline CI/CD: build, testes e publicação da imagem no Docker Hub

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-images">
<summary><strong>images/</strong></summary>

<blockquote>

- [img.png](images/img.png), [img_1.png](images/img_1.png) … [img_19.png](images/img_19.png) — screenshots de endpoints e respostas da API
- [authenticate.png](images/authenticate.png) — exemplo do endpoint de autenticação
- [cadastrarusuarios.png](images/cadastrarusuarios.png) — exemplo do endpoint de cadastro
- [projeções.png](images/projeções.png) — exemplo de listagem de projeções

</blockquote>

</details>


<details id="dir-infra">
<summary><strong>infra/</strong></summary>

<blockquote>

- [main.tf](infra/main.tf) — recursos principais AWS (EC2, Security Group, Elastic IP)
- [variables.tf](infra/variables.tf) — declaração de variáveis Terraform
- [outputs.tf](infra/outputs.tf) — outputs do Terraform (IP público, IDs)
- [terraform.tfvars.example](infra/terraform.tfvars.example) — template de valores das variáveis
- [user-data.sh](infra/user-data.sh) — script de bootstrap da instância EC2
- [README.md](infra/README.md) — documentação da infraestrutura

</blockquote>

</details>


<details id="dir-logstash">
<summary><strong>logstash/</strong></summary>

<blockquote>

<details id="dir-logstash-pipeline">
<summary><strong>pipeline/</strong></summary>

<blockquote>

- [logstash.conf](logstash/pipeline/logstash.conf) — pipeline Logstash: recebe logs JSON via TCP (porta 5000) e envia ao Elasticsearch

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-nginx">
<summary><strong>nginx/</strong></summary>

<blockquote>

- [nginx.conf](nginx/nginx.conf) — configuração de desenvolvimento local (HTTPS com certificado autoassinado)
- [nginx.prod.conf](nginx/nginx.prod.conf) — configuração de produção (HTTP simples, SSL terminado pelo Cloudflare)

<details id="dir-nginx-certs">
<summary><strong>certs/</strong></summary>

<blockquote>

- [nginx.crt](nginx/certs/nginx.crt) — certificado autoassinado (apenas dev local)
- [nginx.key](nginx/certs/nginx.key) — chave privada do certificado autoassinado (apenas dev local)

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



<details><summary>funcao</summary>

Endpoint REST de autenticação — recebe credenciais e retorna o token JWT

</details>

<details><summary>dependencias</summary>

- `authenticationService : `[`AuthenticationService`](#authenticationservice)

</details>

<details><summary>atributos</summary>

- `logger                : Logger`
- `authenticationService : AuthenticationService`

</details>

<details><summary>metodos</summary>

- `authenticate(AuthDto user) : String`

</details>

</blockquote>

</details>



<details id="authenticationservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/AuthenticationService.java">AuthenticationService.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Autentica credenciais via AuthenticationManager e retorna um token JWT assinado

</details>

<details><summary>dependencias</summary>

- `jwtService            : `[`JwtService`](#jwtservice)
- `authenticationManager : AuthenticationManager`

</details>

<details><summary>atributos</summary>

- `jwtService            : JwtService`
- `authenticationManager : AuthenticationManager`

</details>

<details><summary>metodos</summary>

- `authenticate(AuthDto authDto) : String`

</details>

</blockquote>

</details>



<details id="jwtservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/JwtService.java">JwtService.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Gera tokens JWT assinados com chave RSA usando Spring Security OAuth2 Resource Server

</details>

<details><summary>dependencias</summary>

- `encoder : JwtEncoder   [Spring Security OAuth2]`

</details>

<details><summary>atributos</summary>

- `encoder : JwtEncoder`

</details>

<details><summary>metodos</summary>

- `generateToken(Authentication authentication) : String`

</details>

</blockquote>

</details>



<details id="mdcfilter">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/MdcFilter.java">MdcFilter.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Popula o MDC (Mapped Diagnostic Context) com requestId por request para rastreabilidade nos logs (ELK Stack)

</details>

<details><summary>extends</summary>

- `OncePerRequestFilter   [Spring Web]`

</details>

<details><summary>metodos</summary>

- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void`

</details>

</blockquote>

</details>



<details id="securityconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/SecurityConfig.java">SecurityConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Configura a cadeia de segurança para a API REST — JWT Bearer Token, endpoints públicos e OAuth2 Resource Server com chaves RSA

</details>

</blockquote>

</details>



<details id="userauthenticated">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/UserAuthenticated.java">UserAuthenticated.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Adapta a entidade Users para a interface UserDetails do Spring Security (padrão Adapter)

</details>

<details><summary>implements</summary>

- `UserDetails   [Spring Security]`

</details>

<details><summary>dependencias</summary>

- `user : `[`Users`](#users)

</details>

<details><summary>atributos</summary>

- `user : Users`

</details>

<details><summary>metodos</summary>

- `getAuthorities() : Collection<? extends GrantedAuthority>`
- `getPassword()    : String`
- `getUsername()    : String`

</details>

</blockquote>

</details>



<details id="userdetailsserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/UserDetailsServiceImpl.java">UserDetailsServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Carrega usuário do banco pelo email para o Spring Security durante a autenticação

</details>

<details><summary>implements</summary>

- `UserDetailsService   [Spring Security]`

</details>

<details><summary>dependencias</summary>

- `userRepository : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `userRepository : UserRepository`

</details>

<details><summary>metodos</summary>

- `loadUserByUsername(String email) : UserDetails`

</details>

</blockquote>

</details>



<details id="websecurityconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/authentication/WebSecurityConfig.java">WebSecurityConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Configura a cadeia de segurança para a interface Web Thymeleaf — autenticação stateful via form login, sessão e proteção de rotas `/web/**`

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



<details><summary>funcao</summary>

Configura CORS globalmente para a aplicação via `${cors.allowed-origins}`

</details>

</blockquote>

</details>



<details id="modelmapperconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/config/modelMapperConfig.java">modelMapperConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Registra o bean ModelMapper no contexto Spring

</details>

</blockquote>

</details>



<details id="swaggerconfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/config/SwaggerConfig.java">SwaggerConfig.java</a> [@Configuration]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Configura a documentação OpenAPI/Swagger UI da API REST

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



<details><summary>funcao</summary>

Endpoints REST para inserção de notas e listagem de avaliações de uma projeção

</details>

<details><summary>dependencias</summary>

- `assessmentService : `[`AssessmentService`](#assessmentservice)
  - `impl/ AssessmentServiceImpl.java`
- `modelMapper       : ModelMapper`

</details>

<details><summary>atributos</summary>

- `logger            : Logger`
- `assessmentService : AssessmentService`
- `modelMapper       : ModelMapper`

</details>

<details><summary>metodos</summary>

- `insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto) : ResponseEntity<AssessmentDTO>`
- `showAssessment(Long projectionId)                                  : ResponseEntity<List<AssessmentDTO>>`

</details>

</blockquote>

</details>



<details id="coursecontroller">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/CourseController.java">CourseController.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Endpoints REST de CRUD de cursos e listagem de projeções por usuário

</details>

<details><summary>dependencias</summary>

- `courseService     : `[`CourseService`](#courseservice)
  - `impl/ CourseServiceImpl.java`
- `projectionService : `[`ProjectionService`](#projectionservice)
  - `impl/ ProjectionServiceImpl.java`
- `modelMapper       : ModelMapper`

</details>

<details><summary>atributos</summary>

- `logger            : Logger`
- `courseService     : CourseService`
- `projectionService : ProjectionService`
- `modelMapper       : ModelMapper`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Endpoints REST de CRUD de projeções dentro de um curso

</details>

<details><summary>dependencias</summary>

- `projectionService : `[`ProjectionService`](#projectionservice)
  - `impl/ ProjectionServiceImpl.java`
- `userService       : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `modelMapper       : ModelMapper`
- `mapDTO            : `[`MapDTO`](#mapdto)
  - `impl/ MapProjectionDTO.java`

</details>

<details><summary>atributos</summary>

- `logger            : Logger`
- `projectionService : ProjectionService`
- `userService       : UserService`
- `modelMapper       : ModelMapper`
- `mapDTO            : MapDTO`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Endpoints REST de CRUD de usuários — cria, lista, atualiza, deleta e busca usuários

</details>

<details><summary>dependencias</summary>

- `userService  : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`
- `modelMapper  : ModelMapper`

</details>

<details><summary>atributos</summary>

- `logger      : Logger`
- `userService : UserService`
- `modelMapper : ModelMapper`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Contrato de mapeamento de Projection para ProjectionDTO com lista de AssessmentDTO aninhada

</details>

<details><summary>metodos</summary>

- `projectionDTO(Projection projection) : ProjectionDTO`

</details>

</blockquote>

</details>



<details id="mapprojectiondto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/mapper/MapProjectionDTO.java">MapProjectionDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Converte Projection (com lista de Assessment) para ProjectionDTO com AssessmentDTOs aninhados usando ModelMapper

</details>

<details><summary>implements</summary>

- [`MapDTO.java`](#mapdto)

</details>

<details><summary>dependencias</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>atributos</summary>

- `modelMapper : ModelMapper`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Controller Thymeleaf para inserção de notas de avaliações via interface web

</details>

</blockquote>

</details>



<details id="authenticationcontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/AuthenticationControllerWeb.java">AuthenticationControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para login e logout via interface web

</details>

</blockquote>

</details>



<details id="coursecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/CourseControllerWeb.java">CourseControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para listagem e gerenciamento de cursos via interface web

</details>

</blockquote>

</details>



<details id="coursecreatecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/CourseCreateControllerWeb.java">CourseCreateControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para criação de cursos via interface web

</details>

</blockquote>

</details>



<details id="projectioncontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/ProjectionControllerWeb.java">ProjectionControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para listagem e gerenciamento de projeções via interface web

</details>

</blockquote>

</details>



<details id="projectioncreatecontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/ProjectionCreateControllerWeb.java">ProjectionCreateControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para criação de projeções via interface web

</details>

</blockquote>

</details>



<details id="registercontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/RegisterControllerWeb.java">RegisterControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para cadastro de novos usuários via interface web

</details>

<details><summary>dependencias</summary>

- `userService : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`

</details>

</blockquote>

</details>



<details id="usercontrollerweb">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/controller/web/UserControllerWeb.java">UserControllerWeb.java</a> [@Controller]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Controller Thymeleaf para visualização e edição do perfil do usuário via interface web

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



<details><summary>funcao</summary>

Representação de resposta de avaliação nas respostas REST

</details>

<details><summary>atributos</summary>

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



<details><summary>funcao</summary>

Credenciais de login (email + senha) enviadas ao endpoint de autenticação

</details>

<details><summary>atributos</summary>

- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="coursedto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/CourseDTO.java">CourseDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Representação de resposta de curso nas respostas REST

</details>

<details><summary>atributos</summary>

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



<details><summary>funcao</summary>

Wrapper genérico para atualizar um único campo do tipo double em qualquer entidade

</details>

<details><summary>atributos</summary>

- `value : double`

</details>

</blockquote>

</details>



<details id="emailupdatedto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/EmailUpdateDTO.java">EmailUpdateDTO.java</a> [record]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dado de entrada para atualização de email do usuário

</details>

<details><summary>atributos</summary>

- `email : String`

</details>

</blockquote>

</details>



<details id="logondto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/LogOnDto.java">LogOnDto.java</a> [record]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada para cadastro de novo usuário

</details>

<details><summary>atributos</summary>

- `name     : String`
- `email    : String`
- `password : String`

</details>

</blockquote>

</details>



<details id="projectiondto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/ProjectionDTO.java">ProjectionDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Representação de resposta de projeção com suas avaliações aninhadas

</details>

<details><summary>dependencias</summary>

- `assessment : List<AssessmentDTO>`

</details>

<details><summary>atributos</summary>

- `id          : Long`
- `name        : String`
- `finalGrade  : double`
- `courseName  : String`
- `assessment  : List<AssessmentDTO>`

</details>

<details><summary>metodos</summary>

- `ProjectionDTO(Long id, String name, List<AssessmentDTO> assessment, double finalGrade)`

</details>

</blockquote>

</details>



<details id="requestcoursedto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/RequestCourseDto.java">RequestCourseDto.java</a> [record]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Dados de entrada para criação de um novo curso

</details>

<details><summary>atributos</summary>

- `name          : String`
- `averageMethod : String`
- `cutOffGrade   : double`

</details>

</blockquote>

</details>



<details id="stringrequestdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/StringRequestDTO.java">StringRequestDTO.java</a> [record]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Wrapper genérico para atualizar um único campo do tipo String em qualquer entidade

</details>

<details><summary>atributos</summary>

- `string : String`

</details>

</blockquote>

</details>



<details id="userdto">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/dtos/UserDTO.java">UserDTO.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Representação de resposta de usuário nas respostas REST (sem expor senha)

</details>

<details><summary>atributos</summary>

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



<details><summary>funcao</summary>

Lança 404 quando avaliação não é encontrada

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>metodos</summary>

- `AssessmentNotFoundException(Long id, Long projectionId)`

</details>

</blockquote>

</details>



<details id="coursenotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/CourseNotFoundException.java">CourseNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando curso não é encontrado

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>metodos</summary>

- `CourseNotFoundException(Long id, Long userId)`
- `CourseNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="dataintegrityexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/DataIntegrityException.java">DataIntegrityException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 400 em violações de integridade de dados (campo duplicado, constraint)

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

<details><summary>metodos</summary>

- `DataIntegrityException(String attribute)`

</details>

</blockquote>

</details>



<details id="globalexceptionhandler">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/GlobalExceptionHandler.java">GlobalExceptionHandler.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Intercepta exceções e as mapeia para respostas HTTP padronizadas (@RestControllerAdvice)

</details>

<details><summary>atributos</summary>

- `logger : Logger`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Base abstrata para todas as exceções de recurso não encontrado (404)

</details>

<details><summary>extends</summary>

- `RuntimeException`

</details>

<details><summary>metodos</summary>

- `NotFoundArgumentException(String message)`

</details>

</blockquote>

</details>



<details id="projectionnotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/ProjectionNotFoundException.java">ProjectionNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando projeção não é encontrada

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>metodos</summary>

- `ProjectionNotFoundException(Long id, Long courseId)`
- `ProjectionNotFoundException(Long id)`

</details>

</blockquote>

</details>



<details id="standarderror">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/StandardError.java">StandardError.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

DTO de resposta padronizado para erros HTTP — retornado pelo GlobalExceptionHandler

</details>

<details><summary>atributos</summary>

- `timestamp    : LocalDateTime`
- `statusCode   : Integer`
- `error        : String`
- `path         : String`

</details>

<details><summary>metodos</summary>

- `StandardError(Integer statusCode, String error, String path)`

</details>

</blockquote>

</details>



<details id="usernotfoundexception">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/exception/UserNotFoundException.java">UserNotFoundException.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Lança 404 quando usuário não é encontrado por ID ou email

</details>

<details><summary>extends</summary>

- [`NotFoundArgumentException.java`](#notfoundargumentexception)

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Avaliação individual — guarda nota, maxValue e grade mínima necessária; protege o campo grade por invariante

</details>

<details><summary>dependencias</summary>

- `projection : `[`Projection`](#projection)

</details>

<details><summary>atributos</summary>

- `id            : Long`
- `identifier    : String`
- `grade         : double   [sem setter — protegido por applyGrade()]`
- `maxValue      : double`
- `requiredGrade : double`
- `fixed         : boolean  [sem setter — setado por applyGrade()]`
- `projection    : Projection`

</details>

<details><summary>metodos</summary>

- `Assessment(String identifier, double maxValue, Projection projection)`
- `Assessment(String identifier, double grade, double maxValue, Projection projection)`
- `applyGrade(double grade) : void   [única porta de entrada para grade/fixed]`

</details>

</blockquote>

</details>



<details id="course">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Course.java">Course.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Entidade de curso — define método de média e nota de corte; contém projeções de cálculo

</details>

<details><summary>dependencias</summary>

- `user       : `[`Users`](#users)
- `projection : List<Projection>`

</details>

<details><summary>atributos</summary>

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



<details><summary>funcao</summary>

Cenário de cálculo de nota dentro de um curso; agrupa as avaliações e armazena a nota final

</details>

<details><summary>dependencias</summary>

- `course      : `[`Course`](#course)
- `assessment  : List<Assessment>`

</details>

<details><summary>atributos</summary>

- `id          : Long`
- `name        : String`
- `finalGrade  : double`
- `course      : Course`
- `assessment  : List<Assessment>`

</details>

<details><summary>metodos</summary>

- `Projection(Course course, String name)`
- `addAssessment(Assessment assessment) : void`

</details>

</blockquote>

</details>



<details id="role">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/model/Role.java">Role.java</a> [enum]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Define os perfis de acesso da aplicação

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



<details><summary>funcao</summary>

Entidade principal — dono de cursos; raiz do agregado de dados do usuário

</details>

<details><summary>dependencias</summary>

- `course : List<Course>`

</details>

<details><summary>atributos</summary>

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



<details><summary>funcao</summary>

Acesso a dados de Assessment — queries especializadas para o pipeline RPN (maior maxValue, busca por identifier)

</details>

<details><summary>extends</summary>

- `JpaRepository<Assessment, Long>`

</details>

<details><summary>metodos</summary>

- `findByProjection(Projection projection)                           : List<Assessment>`
- `findByIndentifier(String identifier, Long projectionId)           : Assessment  [@Query nativa]`
- `getBiggerMaxValue(Long projectionId)                              : Double  [@Query nativa]`
- `findByProjectionIdAndId(Long projectionId, Long id)              : Optional<Assessment>`
- `existsByProjectionAndIdentifier(Projection projection, String id) : boolean`

</details>

</blockquote>

</details>



<details id="courserepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/CourseRepository.java">CourseRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de Course — filtros por usuário e queries nativas para deleção segura

</details>

<details><summary>extends</summary>

- `JpaRepository<Course, Long>`

</details>

<details><summary>metodos</summary>

- `findByUser(Users user)                        : List<Course>`
- `findByUserAndId(Users user, Long id)          : Optional<Course>`
- `existsByUserAndName(Users user, String name)  : boolean`
- `deleteCourse(Long id, Long userId)            : void  [@Query nativa]`

</details>

</blockquote>

</details>



<details id="projectionrepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/ProjectionRepository.java">ProjectionRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de Projection — suporta deleção em cascata e listagem por usuário via query nativa

</details>

<details><summary>extends</summary>

- `JpaRepository<Projection, Long>`

</details>

<details><summary>metodos</summary>

- `findByCourse(Course course)                                     : List<Projection>`
- `findByCourseAndId(Course course, Long id)                       : Optional<Projection>`
- `existsByCourseAndName(Course course, String name)               : boolean`
- `deleteAllByCourse(Long courseId, Long userId)                   : void  [@Query nativa]`
- `deleteProjection(Long id, Long courseId)                        : void  [@Query nativa]`
- `findAllByUserId(Long userId)                                    : List<Projection>  [@Query nativa]`

</details>

</blockquote>

</details>



<details id="userrepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/repository/UserRepository.java">UserRepository.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Acesso a dados de Users — queries customizadas por email para autenticação e validação

</details>

<details><summary>extends</summary>

- `JpaRepository<Users, Long>`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Utilitário de classificação de tokens — determina se um token é número, operador, função ou identificador de avaliação

</details>

<details><summary>atributos</summary>

- `FORMULA_IDENTIFIER_REGEX : String  [final static]`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Contrato de verificação de identidade — compara o ID do dono com o do usuário autenticado

</details>

<details><summary>metodos</summary>

- `validate(Long resourceOwnerId) : void`

</details>

</blockquote>

</details>



<details id="rpnevaluator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/RpnEvaluator.java">RpnEvaluator.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Avalia uma expressão RPN usando uma pilha — resolve identificadores via IdentifierResolver injetado como lambda

</details>

<details><summary>dependencias</summary>

- `resolver : IdentifierResolver   [@FunctionalInterface — passada como parâmetro]`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Gerencia avaliações — cria a partir da fórmula da projeção, lista e insere notas disparando o recálculo de nota final e grade mínima via pipeline RPN

</details>

<details><summary>extends</summary>

- [`OwnedResourceService.java`](#ownedresourceservice)
  - `implements/`
  - `│   └── OwnershipValidator.java`
  - `dependencias/`
  - `    └── ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>implements</summary>

- [`AssessmentService.java`](#assessmentservice)
  - `extends/`
  - `    └── OwnershipValidator.java`

</details>

<details><summary>dependencias</summary>

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

<details><summary>atributos</summary>

- `projectionRepository   : ProjectionRepository`
- `assessmentRepository   : AssessmentRepository`
- `identifiersDefinition  : IIdentifiersDefinition`
- `calculateFinalGrade    : ICalculateFinalGrade`
- `calculateRequiredGrade : ICalculateRequiredGrade`

</details>

<details><summary>metodos</summary>

- `createAssessment(Projection projection)                       : void`
- `listAssessment(Long projectionId)                             : List<Assessment>`
- `insertGrade(Long projectionId, Long id, DoubleRequestDTO dto) : Assessment`
- `resolveOwnerId(Long projectionId)                             : Long    [@Override — busca userId pelo projectionId]`
- `validateProjection(Long projectionId)                         : void`

</details>

</blockquote>

</details>



<details id="calculatefinalgradeimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateFinalGradeImpl.java">CalculateFinalGradeImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

4º passo — executa o pipeline RPN completo e persiste a nota final calculada na projeção

</details>

<details><summary>implements</summary>

- [`ICalculateFinalGrade.java`](#icalculatefinalgrade)

</details>

<details><summary>dependencias</summary>

- `projectionRepository    : `[`ProjectionRepository`](#projectionrepository)
  - `impl/ JPA`
- `assessmentRepository    : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `convertToPolishNotation : `[`IConvertToPolishNotation`](#iconverttopolishnotation)
  - `impl/ ConvertToPolishNotationReverseImpl.java`
- `rpnEvaluator            : `[`RpnEvaluator`](#rpnevaluator)

</details>

<details><summary>atributos</summary>

- `projectionRepository    : ProjectionRepository`
- `assessmentRepository    : AssessmentRepository`
- `convertToPolishNotation : IConvertToPolishNotation`
- `rpnEvaluator            : RpnEvaluator`

</details>

<details><summary>metodos</summary>

- `calculateResult(Projection projection, String averageMethod) : void`

</details>

</blockquote>

</details>



<details id="calculaterequiredgradeimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateRequiredGradeImpl.java">CalculateRequiredGradeImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

5º passo — para cada avaliação sem nota, usa simulação binária para encontrar e persistir a grade mínima necessária para atingir a nota de corte do curso

</details>

<details><summary>implements</summary>

- [`ICalculateRequiredGrade.java`](#icalculaterequiredgrade)

</details>

<details><summary>dependencias</summary>

- `assessmentRepository    : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `convertToPolishNotation : `[`IConvertToPolishNotation`](#iconverttopolishnotation)
  - `impl/ ConvertToPolishNotationReverseImpl.java`
- `simulationResult        : `[`ISimulationResult`](#isimulationresult)
  - `impl/ SimulationImpl.java`

</details>

<details><summary>atributos</summary>

- `assessmentRepository    : AssessmentRepository`
- `convertToPolishNotation : IConvertToPolishNotation`
- `simulationResult        : ISimulationResult`

</details>

<details><summary>metodos</summary>

- `calculateRequiredGrade(Projection projection, Course course) : void`

</details>

</blockquote>

</details>



<details id="converttopolishnotationreverseimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/ConvertToPolishNotationReverseImpl.java">ConvertToPolishNotationReverseImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

3º passo — converte a lista de tokens infixos para notação polonesa reversa (RPN) via algoritmo Shunting-yard

</details>

<details><summary>implements</summary>

- [`IConvertToPolishNotation.java`](#iconverttopolishnotation)

</details>

<details><summary>dependencias</summary>

- `regularExpressionProcessor : `[`IRegularExpressionProcessor`](#iregularexpressionprocessor)
  - `impl/ RegularExpressionProcessorImpl.java`

</details>

<details><summary>atributos</summary>

- `regularExpressionProcessor : IRegularExpressionProcessor`

</details>

<details><summary>metodos</summary>

- `convertToPolishNotation(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="courseserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/CourseServiceImpl.java">CourseServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Gerencia o ciclo de vida dos cursos — CRUD com validação de propriedade via OwnedResourceService; ao deletar um curso delega deleção em cascata para ProjectionService

</details>

<details><summary>extends</summary>

- [`OwnedResourceService.java`](#ownedresourceservice)
  - `implements/`
  - `│   └── OwnershipValidator.java`
  - `dependencias/`
  - `    └── ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>implements</summary>

- [`CourseService.java`](#courseservice)
  - `extends/`
  - `    └── OwnershipValidator.java`

</details>

<details><summary>dependencias</summary>

- `projectionService  : `[`ProjectionService`](#projectionservice)
  - `impl/ ProjectionServiceImpl.java`
- `courseRepository   : `[`CourseRepository`](#courserepository)
  - `impl/ JPA`
- `userRepository     : `[`UserRepository`](#userrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `projectionService : ProjectionService`
- `courseRepository  : CourseRepository`
- `userRepository    : UserRepository`

</details>

<details><summary>metodos</summary>

- `createCourse(Long userId, RequestCourseDto courseDto)                    : Course`
- `listCourses(Long userId)                                                 : List<Course>`
- `updateCourseName(Long userId, Long id, StringRequestDTO nameDto)         : Course`
- `updateCourseAverageMethod(Long userId, Long id, StringRequestDTO dto)    : Course`
- `updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)      : Course`
- `deleteCourse(Long userId, Long id)                                       : Course`
- `resolveOwnerId(Long userId)                                              : Long    [@Override — retorna userId diretamente]`
- `validateUser(Long userId)                                                : void`

</details>

</blockquote>

</details>



<details id="identifiersdefinitionimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/IdentifiersDefinitionImpl.java">IdentifiersDefinitionImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

2º passo — percorre os tokens da fórmula e preenche cada Assessment com os valores reais do banco

</details>

<details><summary>implements</summary>

- [`IIdentifiersDefinition.java`](#iidentifiersdefinition)

</details>

<details><summary>dependencias</summary>

- `assessmentRepository : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `assessmentRepository : AssessmentRepository`

</details>

<details><summary>metodos</summary>

- `defineIdentifiers(String averageMethod, Projection projection) : void`

</details>

</blockquote>

</details>



<details id="ownedresourceservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/OwnedResourceService.java">OwnedResourceService.java</a> [abstract class]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Template Method — define o algoritmo fixo de validação de propriedade (validateOwnership) e delega a resolução do ID do dono para cada subclasse (resolveOwnerId)

</details>

<details><summary>implements</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>dependencias</summary>

- `ownershipValidationService : `[`OwnershipValidationService`](#ownershipvalidationservice)
  - `impl/ OwnershipValidationServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>metodos</summary>

- `resolveOwnerId(Long resourceId) : Long   [abstract — subclasse define como obter o ID do dono]`
- `validateOwnership(Long resourceId) : void  [final — chama resolveOwnerId() e delega para validate()]`

</details>

</blockquote>

</details>



<details id="ownershipvalidationserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/OwnershipValidationServiceImpl.java">OwnershipValidationServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Verifica se o usuário autenticado (SecurityContext) é dono do recurso comparando IDs; lança AccessDeniedException se não for

</details>

<details><summary>implements</summary>

- [`OwnershipValidationService.java`](#ownershipvalidationservice)

</details>

<details><summary>dependencias</summary>

- `userService : `[`UserService`](#userservice)
  - `impl/ UserServiceImpl.java`

</details>

<details><summary>atributos</summary>

- `userService : UserService`

</details>

<details><summary>metodos</summary>

- `validate(Long resourceOwnerId) : void`

</details>

</blockquote>

</details>



<details id="projectionserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/ProjectionServiceImpl.java">ProjectionServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Gerencia o ciclo de vida das projeções — CRUD com validação de propriedade; ao criar delega criação das avaliações para AssessmentService

</details>

<details><summary>extends</summary>

- [`OwnedResourceService.java`](#ownedresourceservice)
  - `implements/`
  - `│   └── OwnershipValidator.java`
  - `dependencias/`
  - `    └── ownershipValidationService : OwnershipValidationService`

</details>

<details><summary>implements</summary>

- [`ProjectionService.java`](#projectionservice)
  - `extends/`
  - `    └── OwnershipValidator.java`

</details>

<details><summary>dependencias</summary>

- `assessmentService    : `[`AssessmentService`](#assessmentservice)
  - `impl/ AssessmentServiceImpl.java`
- `userRepository       : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `courseRepository     : `[`CourseRepository`](#courserepository)
  - `impl/ JPA`
- `projectionRepository : `[`ProjectionRepository`](#projectionrepository)
  - `impl/ JPA`

</details>

<details><summary>atributos</summary>

- `assessmentService    : AssessmentService`
- `userRepository       : UserRepository`
- `courseRepository     : CourseRepository`
- `projectionRepository : ProjectionRepository`

</details>

<details><summary>metodos</summary>

- `createProjection(Long courseId, StringRequestDTO projectionName)            : Projection`
- `listProjection(Long courseId)                                               : List<Projection>`
- `updateProjectionName(Long courseId, Long id, StringRequestDTO newName)      : Projection`
- `deleteProjection(Long courseId, Long id)                                    : Projection`
- `deleteAllProjections(Long courseId, Long userId)                            : void`
- `listAllProjection(Long userId)                                              : List<Projection>`
- `resolveOwnerId(Long courseId)                                               : Long    [@Override — busca o userId pelo courseId]`
- `validateCourse(Long courseId)                                               : void`

</details>

</blockquote>

</details>



<details id="regularexpressionprocessorimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/RegularExpressionProcessorImpl.java">RegularExpressionProcessorImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

1º passo — tokeniza a string de fórmula (ex: "A1*0.4+B2*0.6") em lista ordenada de tokens

</details>

<details><summary>implements</summary>

- [`IRegularExpressionProcessor.java`](#iregularexpressionprocessor)

</details>

<details><summary>metodos</summary>

- `compileRegex(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="simulationimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/SimulationImpl.java">SimulationImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Simula o resultado da fórmula substituindo uma avaliação por um valor hipotético — base da busca binária de grade mínima

</details>

<details><summary>implements</summary>

- [`ISimulationResult.java`](#isimulationresult)

</details>

<details><summary>dependencias</summary>

- `assessmentRepository : `[`AssessmentRepository`](#assessmentrepository)
  - `impl/ JPA`
- `rpnEvaluator         : `[`RpnEvaluator`](#rpnevaluator)

</details>

<details><summary>atributos</summary>

- `assessmentRepository : AssessmentRepository`
- `rpnEvaluator         : RpnEvaluator`

</details>

<details><summary>metodos</summary>

- `simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) : double`

</details>

</blockquote>

</details>



<details id="userserviceimpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Impl/UserServiceImpl.java">UserServiceImpl.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Gerencia o ciclo de vida dos usuários — CRUD com criptografia de senha e acesso ao usuário autenticado via SecurityContext

</details>

<details><summary>implements</summary>

- [`UserService.java`](#userservice)

</details>

<details><summary>dependencias</summary>

- `userRepository   : `[`UserRepository`](#userrepository)
  - `impl/ JPA`
- `passwordEncoder  : PasswordEncoder`

</details>

<details><summary>atributos</summary>

- `userRepository  : UserRepository`
- `passwordEncoder : PasswordEncoder`

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Contrato de CRUD de avaliações com validação de propriedade herdada de OwnershipValidator

</details>

<details><summary>extends</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>metodos</summary>

- `createAssessment(Projection projection)                       : void`
- `listAssessment(Long projectionId)                             : List<Assessment>`
- `insertGrade(Long projectionId, Long id, DoubleRequestDTO dto) : Assessment`

</details>

</blockquote>

</details>



<details id="courseservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/CourseService.java">CourseService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de CRUD de cursos com validação de propriedade herdada de OwnershipValidator

</details>

<details><summary>extends</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Contrato do 4º passo do pipeline RPN — executa a expressão RPN e persiste a nota final da projeção

</details>

<details><summary>metodos</summary>

- `calculateResult(Projection projection, String averageMethod) : void`

</details>

</blockquote>

</details>



<details id="icalculaterequiredgrade">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ICalculateRequiredGrade.java">ICalculateRequiredGrade.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato do 5º passo do pipeline RPN — calcula a nota mínima necessária em cada avaliação sem nota

</details>

<details><summary>metodos</summary>

- `calculateRequiredGrade(Projection projection, Course course) : void`

</details>

</blockquote>

</details>



<details id="iconverttopolishnotation">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IConvertToPolishNotation.java">IConvertToPolishNotation.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato do 3º passo do pipeline RPN — converte tokens infixos para notação polonesa reversa

</details>

<details><summary>metodos</summary>

- `convertToPolishNotation(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="identifierresolver">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IdentifierResolver.java">IdentifierResolver.java</a> [interface — @FunctionalInterface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato funcional injetado no RpnEvaluator — resolve um token identificador para seu valor numérico

</details>

<details><summary>metodos</summary>

- `resolve(String identifier, Long projectionId) : double`

</details>

</blockquote>

</details>



<details id="iidentifiersdefinition">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IIdentifiersDefinition.java">IIdentifiersDefinition.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato do 2º passo do pipeline RPN — mapeia identificadores da fórmula para valores reais das avaliações

</details>

<details><summary>metodos</summary>

- `defineIdentifiers(String averageMethod, Projection projection) : void`

</details>

</blockquote>

</details>



<details id="iregularexpressionprocessor">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/IRegularExpressionProcessor.java">IRegularExpressionProcessor.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato do 1º passo do pipeline RPN — tokeniza a string de fórmula em lista de tokens

</details>

<details><summary>metodos</summary>

- `compileRegex(String averageMethod) : ArrayList<String>`

</details>

</blockquote>

</details>



<details id="isimulationresult">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ISimulationResult.java">ISimulationResult.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de simulação binária — testa valores hipotéticos de grade para encontrar o mínimo necessário

</details>

<details><summary>metodos</summary>

- `simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) : double`

</details>

</blockquote>

</details>



<details id="ownershipvalidator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/OwnershipValidator.java">OwnershipValidator.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de validação de propriedade — garante que o usuário autenticado é dono do recurso

</details>

<details><summary>metodos</summary>

- `validateOwnership(Long id) : void`

</details>

</blockquote>

</details>



<details id="projectionservice">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/MediasAPI/service/Interfaces/ProjectionService.java">ProjectionService.java</a> [interface]</strong></summary>

<blockquote>



<details><summary>funcao</summary>

Contrato de CRUD de projeções com validação de propriedade herdada de OwnershipValidator

</details>

<details><summary>extends</summary>

- [`OwnershipValidator.java`](#ownershipvalidator)

</details>

<details><summary>metodos</summary>

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



<details><summary>funcao</summary>

Contrato de CRUD e recuperação de usuários, incluindo acesso ao usuário autenticado

</details>

<details><summary>metodos</summary>

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

- [application.properties](src/main/resources/application.properties) — configuração principal (perfis, datasource, JWT, CORS, Flyway, Actuator)
- [application-prod.yml](src/main/resources/application-prod.yml) — overrides de produção (lê secrets de variáveis de ambiente)
- [application.properties.example](src/main/resources/application.properties.example) — template de configuração local
- [logback-spring.xml](src/main/resources/logback-spring.xml) — configuração de logs estruturados JSON para o Logstash (TCP porta 5000)
- [app.key](src/main/resources/app.key) — chave privada RSA para assinatura do JWT (dev local)
- [app.pub](src/main/resources/app.pub) — chave pública RSA para validação do JWT (dev local)

<details id="dir-main-resources-db">
<summary><strong>db/migration/</strong></summary>

<blockquote>

- [V1__baseline_schema.sql](src/main/resources/db/migration/V1__baseline_schema.sql) — schema inicial: tabelas `users`, `courses`, `projections`, `assessments`
- [V2__add_role_column_to_users.sql](src/main/resources/db/migration/V2__add_role_column_to_users.sql) — adiciona coluna `role` à tabela `users`
- [README.md](src/main/resources/db/migration/README.md) — guia de boas práticas para criação de novas migrations

</blockquote>

</details>

<details id="dir-main-resources-templates">
<summary><strong>templates/</strong></summary>

<blockquote>

- [login.html](src/main/resources/templates/login.html) — tela de login da interface web Thymeleaf
- [register.html](src/main/resources/templates/register.html) — tela de cadastro de usuário
- [courses.html](src/main/resources/templates/courses.html) — listagem de disciplinas do usuário
- [courseCreate.html](src/main/resources/templates/courseCreate.html) — formulário de criação de disciplina
- [projections.html](src/main/resources/templates/projections.html) — listagem de projeções de uma disciplina
- [projectionCreate.html](src/main/resources/templates/projectionCreate.html) — formulário de criação de projeção
- [userProjections.html](src/main/resources/templates/userProjections.html) — visão consolidada de projeções do usuário
- [assessmentGrade.html](src/main/resources/templates/assessmentGrade.html) — formulário de lançamento de nota em avaliação

</blockquote>

</details>

</blockquote>

</details>


---

## src/test


<details id="dir-test-root">
<summary><strong>(raiz)</strong></summary>

<blockquote>

<details id="actuatorendpointstest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/ActuatorEndpointsTest.java">ActuatorEndpointsTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testa os endpoints do Spring Boot Actuator (health, liveness, readiness)

</details>

</blockquote>

</details>



<details id="mediasapiapplicationtests">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/MediasApiApplicationTests.java">MediasApiApplicationTests.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Smoke test — verifica que o contexto Spring sobe sem erros

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



<details><summary>funcao</summary>

Testes de integração para os endpoints REST de AssessmentController

</details>

</blockquote>

</details>



<details id="coursecontrollertest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/CourseControllerTest.java">CourseControllerTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes de integração para os endpoints REST de CourseController

</details>

</blockquote>

</details>



<details id="projectioncontrollertest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/controller/rest/v1/ProjectionControllerTest.java">ProjectionControllerTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes de integração para os endpoints REST de ProjectionController

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



<details><summary>funcao</summary>

Testes unitários de AssessmentServiceImpl com Mockito

</details>

</blockquote>

</details>



<details id="calculatefinalgradeimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateFinalGradeImplTest.java">CalculateFinalGradeImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de CalculateFinalGradeImpl — verifica o cálculo correto da nota final via RPN

</details>

</blockquote>

</details>



<details id="calculaterequiredgradeimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/CalculateRequiredGradeImplTest.java">CalculateRequiredGradeImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de CalculateRequiredGradeImpl — verifica o cálculo da grade mínima necessária

</details>

</blockquote>

</details>



<details id="converttopolishnotationreverseimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/ConvertToPolishNotationReverseImplTest.java">ConvertToPolishNotationReverseImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de ConvertToPolishNotationReverseImpl — verifica a conversão infix → RPN

</details>

</blockquote>

</details>



<details id="courseserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/CourseServiceImplTest.java">CourseServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de CourseServiceImpl com Mockito

</details>

</blockquote>

</details>



<details id="identifiersdefinitionimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/IdentifiersDefinitionImplTest.java">IdentifiersDefinitionImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de IdentifiersDefinitionImpl — verifica o mapeamento de identificadores para valores

</details>

</blockquote>

</details>



<details id="projectionserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/ProjectionServiceImplTest.java">ProjectionServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de ProjectionServiceImpl com Mockito

</details>

</blockquote>

</details>



<details id="regularexpressionprocessorimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/RegularExpressionProcessorImplTest.java">RegularExpressionProcessorImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de RegularExpressionProcessorImpl — verifica a tokenização da fórmula

</details>

</blockquote>

</details>



<details id="simulationimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/SimulationImplTest.java">SimulationImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de SimulationImpl — verifica a simulação binária de grade mínima

</details>

</blockquote>

</details>



<details id="userserviceimpltest">
<summary><strong><a href="src/test/java/br/com/gustavohenrique/MediasAPI/service/Impl/UserServiceImplTest.java">UserServiceImplTest.java</a></strong></summary>

<blockquote>



<details><summary>funcao</summary>

Testes unitários de UserServiceImpl com Mockito

</details>

</blockquote>

</details>

</blockquote>

</details>


<details id="dir-test-resources">
<summary><strong>resources/</strong></summary>

<blockquote>

- [application-test.properties](src/test/resources/application-test.properties) — configuração do ambiente de teste: H2 in-memory, Flyway habilitado, JWT keys mockadas
- [application-local.properties.example](src/test/resources/application-local.properties.example) — template para rodar testes localmente fora do Docker

</blockquote>

</details>
