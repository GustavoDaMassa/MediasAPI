# MediasAPI — Mapa de Classes

---

## EXCEPTIONS

---

### NotFoundArgumentException.java [abstract class]

```
NotFoundArgumentException.java
├── funcao/ Base abstrata para todas as exceções de recurso não encontrado (404)
├── extends/
│   └── RuntimeException
└── metodos/
    └── NotFoundArgumentException(String message)
```

---

### UserNotFoundException.java

```
UserNotFoundException.java
├── funcao/ Lança 404 quando usuário não é encontrado por ID ou email
├── extends/
│   └── NotFoundArgumentException.java
└── metodos/
    ├── UserNotFoundException(Long id)
    └── UserNotFoundException(String email)
```

---

### CourseNotFoundException.java

```
CourseNotFoundException.java
├── funcao/ Lança 404 quando curso não é encontrado
├── extends/
│   └── NotFoundArgumentException.java
└── metodos/
    ├── CourseNotFoundException(Long id, Long userId)
    └── CourseNotFoundException(Long id)
```

---

### ProjectionNotFoundException.java

```
ProjectionNotFoundException.java
├── funcao/ Lança 404 quando projeção não é encontrada
├── extends/
│   └── NotFoundArgumentException.java
└── metodos/
    ├── ProjectionNotFoundException(Long id, Long courseId)
    └── ProjectionNotFoundException(Long id)
```

---

### AssessmentNotFoundException.java

```
AssessmentNotFoundException.java
├── funcao/ Lança 404 quando avaliação não é encontrada
├── extends/
│   └── NotFoundArgumentException.java
└── metodos/
    └── AssessmentNotFoundException(Long id, Long projectionId)
```

---

### DataIntegrityException.java

```
DataIntegrityException.java
├── funcao/ Lança 400 em violações de integridade de dados (campo duplicado, constraint)
├── extends/
│   └── RuntimeException
└── metodos/
    └── DataIntegrityException(String attribute)
```

---

### StandardError.java

```
StandardError.java
├── funcao/ DTO de resposta padronizado para erros HTTP — retornado pelo GlobalExceptionHandler
├── atributos/
│   ├── timestamp    : LocalDateTime
│   ├── statusCode   : Integer
│   ├── error        : String
│   └── path         : String
└── metodos/
    └── StandardError(Integer statusCode, String error, String path)
```

---

### GlobalExceptionHandler.java

```
GlobalExceptionHandler.java
├── funcao/ Intercepta exceções e as mapeia para respostas HTTP padronizadas (@RestControllerAdvice)
├── atributos/
│   └── logger : Logger
└── metodos/
    ├── notFound(NotFoundArgumentException, HttpServletRequest)            : ResponseEntity<StandardError>
    ├── illegalArgument(IllegalArgumentException, HttpServletRequest)      : ResponseEntity<StandardError>
    ├── noSuchElement(NoSuchElementException, HttpServletRequest)          : ResponseEntity<StandardError>
    ├── DataIntegrity(DataIntegrityException, HttpServletRequest)          : ResponseEntity<StandardError>
    ├── Dividebyzero(InternalServerError, HttpServletRequest)              : ResponseEntity<StandardError>
    ├── handleConstraintViolation(ConstraintViolationException, HttpServletRequest) : ResponseEntity<StandardError>
    └── accessDenied(AccessDeniedException, HttpServletRequest)           : ResponseEntity<StandardError>
```

---

## MODELS

---

### Role.java [enum]

```
Role.java
├── funcao/ Define os perfis de acesso da aplicação
└── values/
    ├── ADMIN
    └── USER
```

---

### Users.java

```
Users.java
├── funcao/ Entidade principal — dono de cursos; raiz do agregado de dados do usuário
├── dependencias/
│   └── course : List<Course>
└── atributos/
    ├── id           : Long
    ├── name         : String
    ├── email        : String
    ├── password     : String
    ├── role         : Role
    └── course       : List<Course>
```

---

### Course.java

```
Course.java
├── funcao/ Entidade de curso — define método de média e nota de corte; contém projeções de cálculo
├── dependencias/
│   ├── user       : Users
│   └── projection : List<Projection>
└── atributos/
    ├── id            : Long
    ├── name          : String
    ├── averageMethod : String
    ├── cutOffGrade   : double
    ├── user          : Users
    └── projection    : List<Projection>
```

---

### Projection.java

```
Projection.java
├── funcao/ Cenário de cálculo de nota dentro de um curso; agrupa as avaliações e armazena a nota final
├── dependencias/
│   ├── course      : Course
│   └── assessment  : List<Assessment>
├── atributos/
│   ├── id          : Long
│   ├── name        : String
│   ├── finalGrade  : double
│   ├── course      : Course
│   └── assessment  : List<Assessment>
└── metodos/
    ├── Projection(Course course, String name)
    └── addAssessment(Assessment assessment) : void
```

---

### Assessment.java

```
Assessment.java
├── funcao/ Avaliação individual — guarda nota, maxValue e grade mínima necessária; protege o campo grade por invariante
├── dependencias/
│   └── projection : Projection
├── atributos/
│   ├── id            : Long
│   ├── identifier    : String
│   ├── grade         : double   [sem setter — protegido por applyGrade()]
│   ├── maxValue      : double
│   ├── requiredGrade : double
│   ├── fixed         : boolean  [sem setter — setado por applyGrade()]
│   └── projection    : Projection
└── metodos/
    ├── Assessment(String identifier, double maxValue, Projection projection)
    ├── Assessment(String identifier, double grade, double maxValue, Projection projection)
    └── applyGrade(double grade) : void   [única porta de entrada para grade/fixed]
```

---

## DTOs

---

### LogOnDto.java [record]

```
LogOnDto.java
├── funcao/ Dados de entrada para cadastro de novo usuário
└── atributos/
    ├── name     : String
    ├── email    : String
    └── password : String
```

---

### AuthDto.java

```
AuthDto.java
├── funcao/ Credenciais de login (email + senha) enviadas ao endpoint de autenticação
└── atributos/
    ├── email    : String
    └── password : String
```

---

### StringRequestDTO.java [record]

```
StringRequestDTO.java
├── funcao/ Wrapper genérico para atualizar um único campo do tipo String em qualquer entidade
└── atributos/
    └── string : String
```

---

### DoubleRequestDTO.java [record]

```
DoubleRequestDTO.java
├── funcao/ Wrapper genérico para atualizar um único campo do tipo double em qualquer entidade
└── atributos/
    └── value : double
```

---

### RequestCourseDto.java [record]

```
RequestCourseDto.java
├── funcao/ Dados de entrada para criação de um novo curso
└── atributos/
    ├── name          : String
    ├── averageMethod : String
    └── cutOffGrade   : double
```

---

### CourseDTO.java

```
CourseDTO.java
├── funcao/ Representação de resposta de curso nas respostas REST
└── atributos/
    ├── id            : Long
    ├── name          : String
    ├── averageMethod : String
    └── cutOffGrade   : double
```

---

### ProjectionDTO.java

```
ProjectionDTO.java
├── funcao/ Representação de resposta de projeção com suas avaliações aninhadas
├── dependencias/
│   └── assessment : List<AssessmentDTO>
├── atributos/
│   ├── id          : Long
│   ├── name        : String
│   ├── finalGrade  : double
│   ├── courseName  : String
│   └── assessment  : List<AssessmentDTO>
└── metodos/
    └── ProjectionDTO(Long id, String name, List<AssessmentDTO> assessment, double finalGrade)
```

---

### AssessmentDTO.java

```
AssessmentDTO.java
├── funcao/ Representação de resposta de avaliação nas respostas REST
└── atributos/
    ├── id            : Long
    ├── identifier    : String
    ├── grade         : double
    └── requiredGrade : double
```

---

### UserDTO.java

```
UserDTO.java
├── funcao/ Representação de resposta de usuário nas respostas REST (sem expor senha)
└── atributos/
    ├── id    : Long
    ├── name  : String
    ├── email : String
    └── role  : Role
```

---

### EmailUpdateDTO.java [record]

```
EmailUpdateDTO.java
├── funcao/ Dado de entrada para atualização de email do usuário
└── atributos/
    └── email : String
```

---

## REPOSITORIES

---

### UserRepository.java [interface]

```
UserRepository.java
├── funcao/ Acesso a dados de Users — queries customizadas por email para autenticação e validação
├── extends/
│   └── JpaRepository<Users, Long>
└── metodos/
    ├── existsByEmail(String email)      : boolean
    └── findByEmail(String email)        : Optional<Users>
```

---

### CourseRepository.java [interface]

```
CourseRepository.java
├── funcao/ Acesso a dados de Course — filtros por usuário e queries nativas para deleção segura
├── extends/
│   └── JpaRepository<Course, Long>
└── metodos/
    ├── findByUser(Users user)                        : List<Course>
    ├── findByUserAndId(Users user, Long id)          : Optional<Course>
    ├── existsByUserAndName(Users user, String name)  : boolean
    └── deleteCourse(Long id, Long userId)            : void  [@Query nativa]
```

---

### ProjectionRepository.java [interface]

```
ProjectionRepository.java
├── funcao/ Acesso a dados de Projection — suporta deleção em cascata e listagem por usuário via query nativa
├── extends/
│   └── JpaRepository<Projection, Long>
└── metodos/
    ├── findByCourse(Course course)                                     : List<Projection>
    ├── findByCourseAndId(Course course, Long id)                       : Optional<Projection>
    ├── existsByCourseAndName(Course course, String name)               : boolean
    ├── deleteAllByCourse(Long courseId, Long userId)                   : void  [@Query nativa]
    ├── deleteProjection(Long id, Long courseId)                        : void  [@Query nativa]
    └── findAllByUserId(Long userId)                                    : List<Projection>  [@Query nativa]
```

---

### AssessmentRepository.java [interface]

```
AssessmentRepository.java
├── funcao/ Acesso a dados de Assessment — queries especializadas para o pipeline RPN (maior maxValue, busca por identifier)
├── extends/
│   └── JpaRepository<Assessment, Long>
└── metodos/
    ├── findByProjection(Projection projection)                           : List<Assessment>
    ├── findByIndentifier(String identifier, Long projectionId)           : Assessment  [@Query nativa]
    ├── getBiggerMaxValue(Long projectionId)                              : Double  [@Query nativa]
    ├── findByProjectionIdAndId(Long projectionId, Long id)              : Optional<Assessment>
    └── existsByProjectionAndIdentifier(Projection projection, String id) : boolean
```

---

## SERVICE INTERFACES

---

### OwnershipValidator.java [interface]

```
OwnershipValidator.java
├── funcao/ Contrato de validação de propriedade — garante que o usuário autenticado é dono do recurso
└── metodos/
    └── validateOwnership(Long id) : void
```

---

### OwnershipValidationService.java [interface]

```
OwnershipValidationService.java
├── funcao/ Contrato de verificação de identidade — compara o ID do dono com o do usuário autenticado
└── metodos/
    └── validate(Long resourceOwnerId) : void
```

---

### UserService.java [interface]

```
UserService.java
├── funcao/ Contrato de CRUD e recuperação de usuários, incluindo acesso ao usuário autenticado
└── metodos/
    ├── create(LogOnDto users)                              : Users
    ├── createAdminUser(LogOnDto logOnDto)                  : Users
    ├── updateName(Long id, StringRequestDTO nameDto)       : Users
    ├── updateEmail(Long id, EmailUpdateDTO emailDTO)       : Users
    ├── deleteUser(Long id)                                 : Users
    ├── listUsers()                                         : List<Users>
    ├── findusers(String email)                             : Users
    └── getAuthenticatedUser()                              : Users
```

---

### CourseService.java [interface]

```
CourseService.java
├── funcao/ Contrato de CRUD de cursos com validação de propriedade herdada de OwnershipValidator
├── extends/
│   └── OwnershipValidator.java
└── metodos/
    ├── createCourse(Long userId, RequestCourseDto course)                       : Course
    ├── listCourses(Long userId)                                                 : List<Course>
    ├── updateCourseName(Long userId, Long id, StringRequestDTO nameDto)         : Course
    ├── updateCourseAverageMethod(Long userId, Long id, StringRequestDTO dto)    : Course
    ├── updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)      : Course
    └── deleteCourse(Long userId, Long id)                                       : Course
```

---

### ProjectionService.java [interface]

```
ProjectionService.java
├── funcao/ Contrato de CRUD de projeções com validação de propriedade herdada de OwnershipValidator
├── extends/
│   └── OwnershipValidator.java
└── metodos/
    ├── createProjection(Long courseId, StringRequestDTO projectionName)             : Projection
    ├── listProjection(Long courseId)                                                : List<Projection>
    ├── updateProjectionName(Long courseId, Long id, StringRequestDTO newName)       : Projection
    ├── deleteProjection(Long courseId, Long id)                                     : Projection
    ├── deleteAllProjections(Long courseId, Long userId)                             : void
    └── listAllProjection(Long userId)                                               : List<Projection>
```

---

### AssessmentService.java [interface]

```
AssessmentService.java
├── funcao/ Contrato de CRUD de avaliações com validação de propriedade herdada de OwnershipValidator
├── extends/
│   └── OwnershipValidator.java
└── metodos/
    ├── createAssessment(Projection projection)                       : void
    ├── listAssessment(Long projectionId)                             : List<Assessment>
    └── insertGrade(Long projectionId, Long id, DoubleRequestDTO dto) : Assessment
```

---

### IRegularExpressionProcessor.java [interface]

```
IRegularExpressionProcessor.java
├── funcao/ Contrato do 1º passo do pipeline RPN — tokeniza a string de fórmula em lista de tokens
└── metodos/
    └── compileRegex(String averageMethod) : ArrayList<String>
```

---

### IIdentifiersDefinition.java [interface]

```
IIdentifiersDefinition.java
├── funcao/ Contrato do 2º passo do pipeline RPN — mapeia identificadores da fórmula para valores reais das avaliações
└── metodos/
    └── defineIdentifiers(String averageMethod, Projection projection) : void
```

---

### IConvertToPolishNotation.java [interface]

```
IConvertToPolishNotation.java
├── funcao/ Contrato do 3º passo do pipeline RPN — converte tokens infixos para notação polonesa reversa
└── metodos/
    └── convertToPolishNotation(String averageMethod) : ArrayList<String>
```

---

### ICalculateFinalGrade.java [interface]

```
ICalculateFinalGrade.java
├── funcao/ Contrato do 4º passo do pipeline RPN — executa a expressão RPN e persiste a nota final da projeção
└── metodos/
    └── calculateResult(Projection projection, String averageMethod) : void
```

---

### ICalculateRequiredGrade.java [interface]

```
ICalculateRequiredGrade.java
├── funcao/ Contrato do 5º passo do pipeline RPN — calcula a nota mínima necessária em cada avaliação sem nota
└── metodos/
    └── calculateRequiredGrade(Projection projection, Course course) : void
```

---

### ISimulationResult.java [interface]

```
ISimulationResult.java
├── funcao/ Contrato de simulação binária — testa valores hipotéticos de grade para encontrar o mínimo necessário
└── metodos/
    └── simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) : double
```

---

### IdentifierResolver.java [interface — @FunctionalInterface]

```
IdentifierResolver.java
├── funcao/ Contrato funcional injetado no RpnEvaluator — resolve um token identificador para seu valor numérico
└── metodos/
    └── resolve(String identifier, Long projectionId) : double
```

---

### MapDTO.java [interface]

```
MapDTO.java
├── funcao/ Contrato de mapeamento de Projection para ProjectionDTO com lista de AssessmentDTO aninhada
└── metodos/
    └── projectionDTO(Projection projection) : ProjectionDTO
```

---

## SERVICE IMPLEMENTATIONS

---

### OwnedResourceService.java [abstract class]

```
OwnedResourceService.java
├── funcao/ Template Method — define o algoritmo fixo de validação de propriedade (validateOwnership) e delega a resolução do ID do dono para cada subclasse (resolveOwnerId)
├── implements/
│   └── OwnershipValidator.java
├── dependencias/
│   └── ownershipValidationService : OwnershipValidationService
│       └── impl/ OwnershipValidationServiceImpl.java
├── atributos/
│   └── ownershipValidationService : OwnershipValidationService
└── metodos/
    ├── resolveOwnerId(Long resourceId) : Long   [abstract — subclasse define como obter o ID do dono]
    └── validateOwnership(Long resourceId) : void  [final — chama resolveOwnerId() e delega para validate()]
```

---

### OwnershipValidationServiceImpl.java

```
OwnershipValidationServiceImpl.java
├── funcao/ Verifica se o usuário autenticado (SecurityContext) é dono do recurso comparando IDs; lança AccessDeniedException se não for
├── implements/
│   └── OwnershipValidationService.java
├── dependencias/
│   └── userService : UserService
│       └── impl/ UserServiceImpl.java
├── atributos/
│   └── userService : UserService
└── metodos/
    └── validate(Long resourceOwnerId) : void
```

---

### UserServiceImpl.java

```
UserServiceImpl.java
├── funcao/ Gerencia o ciclo de vida dos usuários — CRUD com criptografia de senha e acesso ao usuário autenticado via SecurityContext
├── implements/
│   └── UserService.java
├── dependencias/
│   ├── userRepository   : UserRepository
│   │   └── impl/ JPA
│   └── passwordEncoder  : PasswordEncoder
├── atributos/
│   ├── userRepository  : UserRepository
│   └── passwordEncoder : PasswordEncoder
└── metodos/
    ├── create(LogOnDto logOnDto)                             : Users
    ├── createAdminUser(LogOnDto logOnDto)                    : Users
    ├── updateName(Long id, StringRequestDTO nameDto)         : Users
    ├── updateEmail(Long id, EmailUpdateDTO emailDTO)         : Users
    ├── deleteUser(Long id)                                   : Users
    ├── listUsers()                                           : List<Users>
    ├── findusers(String email)                               : Users
    └── getAuthenticatedUser()                                : Users
```

---

### CourseServiceImpl.java

```
CourseServiceImpl.java
├── funcao/ Gerencia o ciclo de vida dos cursos — CRUD com validação de propriedade via OwnedResourceService; ao deletar um curso delega deleção em cascata para ProjectionService
├── extends/
│   └── OwnedResourceService.java
│       ├── implements/
│       │   └── OwnershipValidator.java
│       └── dependencias/
│           └── ownershipValidationService : OwnershipValidationService
├── implements/
│   └── CourseService.java
│       └── extends/
│           └── OwnershipValidator.java
├── dependencias/
│   ├── projectionService  : ProjectionService
│   │   └── impl/ ProjectionServiceImpl.java
│   ├── courseRepository   : CourseRepository
│   │   └── impl/ JPA
│   └── userRepository     : UserRepository
│       └── impl/ JPA
├── atributos/
│   ├── projectionService : ProjectionService
│   ├── courseRepository  : CourseRepository
│   └── userRepository    : UserRepository
└── metodos/
    ├── createCourse(Long userId, RequestCourseDto courseDto)                    : Course
    ├── listCourses(Long userId)                                                 : List<Course>
    ├── updateCourseName(Long userId, Long id, StringRequestDTO nameDto)         : Course
    ├── updateCourseAverageMethod(Long userId, Long id, StringRequestDTO dto)    : Course
    ├── updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)      : Course
    ├── deleteCourse(Long userId, Long id)                                       : Course
    ├── resolveOwnerId(Long userId)                                              : Long    [@Override — retorna userId diretamente]
    └── validateUser(Long userId)                                                : void
```

---

### ProjectionServiceImpl.java

```
ProjectionServiceImpl.java
├── funcao/ Gerencia o ciclo de vida das projeções — CRUD com validação de propriedade; ao criar delega criação das avaliações para AssessmentService
├── extends/
│   └── OwnedResourceService.java
│       ├── implements/
│       │   └── OwnershipValidator.java
│       └── dependencias/
│           └── ownershipValidationService : OwnershipValidationService
├── implements/
│   └── ProjectionService.java
│       └── extends/
│           └── OwnershipValidator.java
├── dependencias/
│   ├── assessmentService    : AssessmentService
│   │   └── impl/ AssessmentServiceImpl.java
│   ├── userRepository       : UserRepository
│   │   └── impl/ JPA
│   ├── courseRepository     : CourseRepository
│   │   └── impl/ JPA
│   └── projectionRepository : ProjectionRepository
│       └── impl/ JPA
├── atributos/
│   ├── assessmentService    : AssessmentService
│   ├── userRepository       : UserRepository
│   ├── courseRepository     : CourseRepository
│   └── projectionRepository : ProjectionRepository
└── metodos/
    ├── createProjection(Long courseId, StringRequestDTO projectionName)            : Projection
    ├── listProjection(Long courseId)                                               : List<Projection>
    ├── updateProjectionName(Long courseId, Long id, StringRequestDTO newName)      : Projection
    ├── deleteProjection(Long courseId, Long id)                                    : Projection
    ├── deleteAllProjections(Long courseId, Long userId)                            : void
    ├── listAllProjection(Long userId)                                              : List<Projection>
    ├── resolveOwnerId(Long courseId)                                               : Long    [@Override — busca o userId pelo courseId]
    └── validateCourse(Long courseId)                                               : void
```

---

### AssessmentServiceImpl.java

```
AssessmentServiceImpl.java
├── funcao/ Gerencia avaliações — cria a partir da fórmula da projeção, lista e insere notas disparando o recálculo de nota final e grade mínima via pipeline RPN
├── extends/
│   └── OwnedResourceService.java
│       ├── implements/
│       │   └── OwnershipValidator.java
│       └── dependencias/
│           └── ownershipValidationService : OwnershipValidationService
├── implements/
│   └── AssessmentService.java
│       └── extends/
│           └── OwnershipValidator.java
├── dependencias/
│   ├── projectionRepository   : ProjectionRepository
│   │   └── impl/ JPA
│   ├── assessmentRepository   : AssessmentRepository
│   │   └── impl/ JPA
│   ├── identifiersDefinition  : IIdentifiersDefinition
│   │   └── impl/ IdentifiersDefinitionImpl.java
│   ├── calculateFinalGrade    : ICalculateFinalGrade
│   │   └── impl/ CalculateFinalGradeImpl.java
│   └── calculateRequiredGrade : ICalculateRequiredGrade
│       └── impl/ CalculateRequiredGradeImpl.java
├── atributos/
│   ├── projectionRepository   : ProjectionRepository
│   ├── assessmentRepository   : AssessmentRepository
│   ├── identifiersDefinition  : IIdentifiersDefinition
│   ├── calculateFinalGrade    : ICalculateFinalGrade
│   └── calculateRequiredGrade : ICalculateRequiredGrade
└── metodos/
    ├── createAssessment(Projection projection)                       : void
    ├── listAssessment(Long projectionId)                             : List<Assessment>
    ├── insertGrade(Long projectionId, Long id, DoubleRequestDTO dto) : Assessment
    ├── resolveOwnerId(Long projectionId)                             : Long    [@Override — busca userId pelo projectionId]
    └── validateProjection(Long projectionId)                         : void
```

---

## RPN PIPELINE

> Fluxo: RegexProcessor → IdentifiersDefinition → ConvertToPolishNotation → RpnEvaluator → CalculateFinalGrade → CalculateRequiredGrade

---

### RegularExpressionProcessorImpl.java

```
RegularExpressionProcessorImpl.java
├── funcao/ 1º passo — tokeniza a string de fórmula (ex: "A1*0.4+B2*0.6") em lista ordenada de tokens
├── implements/
│   └── IRegularExpressionProcessor.java
└── metodos/
    └── compileRegex(String averageMethod) : ArrayList<String>
```

---

### IdentifiersDefinitionImpl.java

```
IdentifiersDefinitionImpl.java
├── funcao/ 2º passo — percorre os tokens da fórmula e preenche cada Assessment com os valores reais do banco
├── implements/
│   └── IIdentifiersDefinition.java
├── dependencias/
│   └── assessmentRepository : AssessmentRepository
│       └── impl/ JPA
├── atributos/
│   └── assessmentRepository : AssessmentRepository
└── metodos/
    └── defineIdentifiers(String averageMethod, Projection projection) : void
```

---

### ConvertToPolishNotationReverseImpl.java

```
ConvertToPolishNotationReverseImpl.java
├── funcao/ 3º passo — converte a lista de tokens infixos para notação polonesa reversa (RPN) via algoritmo Shunting-yard
├── implements/
│   └── IConvertToPolishNotation.java
├── dependencias/
│   └── regularExpressionProcessor : IRegularExpressionProcessor
│       └── impl/ RegularExpressionProcessorImpl.java
├── atributos/
│   └── regularExpressionProcessor : IRegularExpressionProcessor
└── metodos/
    └── convertToPolishNotation(String averageMethod) : ArrayList<String>
```

---

### FormulaTokens.java

```
FormulaTokens.java
├── funcao/ Utilitário de classificação de tokens — determina se um token é número, operador, função ou identificador de avaliação
├── atributos/
│   └── FORMULA_IDENTIFIER_REGEX : String  [final static]
└── metodos/
    ├── isIdentifier(String token) : boolean
    ├── isNumber(String token)     : boolean
    ├── isOperator(String token)   : boolean
    ├── isFunction(String token)   : boolean
    └── cleanBrackets(String token): String
```

---

### RpnEvaluator.java

```
RpnEvaluator.java
├── funcao/ Avalia uma expressão RPN usando uma pilha — resolve identificadores via IdentifierResolver injetado como lambda
├── dependencias/
│   └── resolver : IdentifierResolver   [@FunctionalInterface — passada como parâmetro]
└── metodos/
    └── evaluate(List<String> tokens, Long projectionId, IdentifierResolver resolver) : double
```

---

### CalculateFinalGradeImpl.java

```
CalculateFinalGradeImpl.java
├── funcao/ 4º passo — executa o pipeline RPN completo e persiste a nota final calculada na projeção
├── implements/
│   └── ICalculateFinalGrade.java
├── dependencias/
│   ├── projectionRepository    : ProjectionRepository
│   │   └── impl/ JPA
│   ├── assessmentRepository    : AssessmentRepository
│   │   └── impl/ JPA
│   ├── convertToPolishNotation : IConvertToPolishNotation
│   │   └── impl/ ConvertToPolishNotationReverseImpl.java
│   └── rpnEvaluator            : RpnEvaluator
├── atributos/
│   ├── projectionRepository    : ProjectionRepository
│   ├── assessmentRepository    : AssessmentRepository
│   ├── convertToPolishNotation : IConvertToPolishNotation
│   └── rpnEvaluator            : RpnEvaluator
└── metodos/
    └── calculateResult(Projection projection, String averageMethod) : void
```

---

### CalculateRequiredGradeImpl.java

```
CalculateRequiredGradeImpl.java
├── funcao/ 5º passo — para cada avaliação sem nota, usa simulação binária para encontrar e persistir a grade mínima necessária para atingir a nota de corte do curso
├── implements/
│   └── ICalculateRequiredGrade.java
├── dependencias/
│   ├── assessmentRepository    : AssessmentRepository
│   │   └── impl/ JPA
│   ├── convertToPolishNotation : IConvertToPolishNotation
│   │   └── impl/ ConvertToPolishNotationReverseImpl.java
│   └── simulationResult        : ISimulationResult
│       └── impl/ SimulationImpl.java
├── atributos/
│   ├── assessmentRepository    : AssessmentRepository
│   ├── convertToPolishNotation : IConvertToPolishNotation
│   └── simulationResult        : ISimulationResult
└── metodos/
    └── calculateRequiredGrade(Projection projection, Course course) : void
```

---

### SimulationImpl.java

```
SimulationImpl.java
├── funcao/ Simula o resultado da fórmula substituindo uma avaliação por um valor hipotético — base da busca binária de grade mínima
├── implements/
│   └── ISimulationResult.java
├── dependencias/
│   ├── assessmentRepository : AssessmentRepository
│   │   └── impl/ JPA
│   └── rpnEvaluator         : RpnEvaluator
├── atributos/
│   ├── assessmentRepository : AssessmentRepository
│   └── rpnEvaluator         : RpnEvaluator
└── metodos/
    └── simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) : double
```

---

## AUTHENTICATION & SECURITY

---

### JwtService.java

```
JwtService.java
├── funcao/ Gera tokens JWT assinados com chave RSA usando Spring Security OAuth2 Resource Server
├── dependencias/
│   └── encoder : JwtEncoder   [Spring Security OAuth2]
├── atributos/
│   └── encoder : JwtEncoder
└── metodos/
    └── generateToken(Authentication authentication) : String
```

---

### AuthenticationService.java

```
AuthenticationService.java
├── funcao/ Autentica credenciais via AuthenticationManager e retorna um token JWT assinado
├── dependencias/
│   ├── jwtService            : JwtService
│   └── authenticationManager : AuthenticationManager
├── atributos/
│   ├── jwtService            : JwtService
│   └── authenticationManager : AuthenticationManager
└── metodos/
    └── authenticate(AuthDto authDto) : String
```

---

### UserDetailsServiceImpl.java

```
UserDetailsServiceImpl.java
├── funcao/ Carrega usuário do banco pelo email para o Spring Security durante a autenticação
├── implements/
│   └── UserDetailsService   [Spring Security]
├── dependencias/
│   └── userRepository : UserRepository
│       └── impl/ JPA
├── atributos/
│   └── userRepository : UserRepository
└── metodos/
    └── loadUserByUsername(String email) : UserDetails
```

---

### UserAuthenticated.java

```
UserAuthenticated.java
├── funcao/ Adapta a entidade Users para a interface UserDetails do Spring Security (padrão Adapter)
├── implements/
│   └── UserDetails   [Spring Security]
├── dependencias/
│   └── user : Users
├── atributos/
│   └── user : Users
└── metodos/
    ├── getAuthorities() : Collection<? extends GrantedAuthority>
    ├── getPassword()    : String
    └── getUsername()    : String
```

---

### MdcFilter.java

```
MdcFilter.java
├── funcao/ Popula o MDC (Mapped Diagnostic Context) com requestId por request para rastreabilidade nos logs (ELK Stack)
├── extends/
│   └── OncePerRequestFilter   [Spring Web]
└── metodos/
    └── doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) : void
```

---

## MAPPERS

---

### MapProjectionDTO.java

```
MapProjectionDTO.java
├── funcao/ Converte Projection (com lista de Assessment) para ProjectionDTO com AssessmentDTOs aninhados usando ModelMapper
├── implements/
│   └── MapDTO.java
├── dependencias/
│   └── modelMapper : ModelMapper
├── atributos/
│   └── modelMapper : ModelMapper
└── metodos/
    └── projectionDTO(Projection projection) : ProjectionDTO
```

---

## REST CONTROLLERS

---

### AuthenticationController.java

```
AuthenticationController.java
├── funcao/ Endpoint REST de autenticação — recebe credenciais e retorna o token JWT
├── dependencias/
│   └── authenticationService : AuthenticationService
├── atributos/
│   ├── logger                : Logger
│   └── authenticationService : AuthenticationService
└── metodos/
    └── authenticate(AuthDto user) : String
```

---

### UserController.java

```
UserController.java
├── funcao/ Endpoints REST de CRUD de usuários — cria, lista, atualiza, deleta e busca usuários
├── dependencias/
│   ├── userService  : UserService
│   │   └── impl/ UserServiceImpl.java
│   └── modelMapper  : ModelMapper
├── atributos/
│   ├── logger      : Logger
│   ├── userService : UserService
│   └── modelMapper : ModelMapper
└── metodos/
    ├── showUsers()                                              : ResponseEntity<List<UserDTO>>
    ├── createAdminUser(LogOnDto users)                          : ResponseEntity<UserDTO>
    ├── createUser(LogOnDto users)                               : ResponseEntity<UserDTO>
    ├── updateName(Long id, StringRequestDTO nameDto)            : ResponseEntity<UserDTO>
    ├── updateEmail(Long id, EmailUpdateDTO emailDTO)            : ResponseEntity<UserDTO>
    ├── deleteUser(Long id)                                      : ResponseEntity<UserDTO>
    └── findUser(String email)                                   : ResponseEntity<UserDTO>
```

---

### CourseController.java

```
CourseController.java
├── funcao/ Endpoints REST de CRUD de cursos e listagem de projeções por usuário
├── dependencias/
│   ├── courseService     : CourseService
│   │   └── impl/ CourseServiceImpl.java
│   ├── projectionService : ProjectionService
│   │   └── impl/ ProjectionServiceImpl.java
│   └── modelMapper       : ModelMapper
├── atributos/
│   ├── logger            : Logger
│   ├── courseService     : CourseService
│   ├── projectionService : ProjectionService
│   └── modelMapper       : ModelMapper
└── metodos/
    ├── createCourse(Long userId, RequestCourseDto course)                    : ResponseEntity<CourseDTO>
    ├── showCourses(Long userId)                                              : ResponseEntity<List<CourseDTO>>
    ├── updateCourseName(Long userId, Long id, StringRequestDTO nameDto)      : ResponseEntity<CourseDTO>
    ├── updateCourseMethod(Long userId, Long id, StringRequestDTO dto)        : ResponseEntity<CourseDTO>
    ├── updateCourseCutOffGrade(Long userId, Long id, DoubleRequestDTO dto)   : ResponseEntity<CourseDTO>
    ├── deleteCourse(Long userId, Long id)                                    : ResponseEntity<CourseDTO>
    └── showAllProjections(Long userId)                                       : ResponseEntity<List<ProjectionDTO>>
```

---

### ProjectionController.java

```
ProjectionController.java
├── funcao/ Endpoints REST de CRUD de projeções dentro de um curso
├── dependencias/
│   ├── projectionService : ProjectionService
│   │   └── impl/ ProjectionServiceImpl.java
│   ├── userService       : UserService
│   │   └── impl/ UserServiceImpl.java
│   ├── modelMapper       : ModelMapper
│   └── mapDTO            : MapDTO
│       └── impl/ MapProjectionDTO.java
├── atributos/
│   ├── logger            : Logger
│   ├── projectionService : ProjectionService
│   ├── userService       : UserService
│   ├── modelMapper       : ModelMapper
│   └── mapDTO            : MapDTO
└── metodos/
    ├── createProjection(Long courseId, StringRequestDTO projectionName)         : ResponseEntity<ProjectionDTO>
    ├── showProjections(Long courseId)                                           : ResponseEntity<List<ProjectionDTO>>
    ├── updateProjectionName(Long courseId, Long id, StringRequestDTO newName)   : ResponseEntity<ProjectionDTO>
    ├── deleteProjection(Long courseId, Long id)                                 : ResponseEntity<ProjectionDTO>
    └── deleteAllProjections(Long courseId)                                      : void
```

---

### AssessmentController.java

```
AssessmentController.java
├── funcao/ Endpoints REST para inserção de notas e listagem de avaliações de uma projeção
├── dependencias/
│   ├── assessmentService : AssessmentService
│   │   └── impl/ AssessmentServiceImpl.java
│   └── modelMapper       : ModelMapper
├── atributos/
│   ├── logger            : Logger
│   ├── assessmentService : AssessmentService
│   └── modelMapper       : ModelMapper
└── metodos/
    ├── insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto) : ResponseEntity<AssessmentDTO>
    └── showAssessment(Long projectionId)                                  : ResponseEntity<List<AssessmentDTO>>
```
