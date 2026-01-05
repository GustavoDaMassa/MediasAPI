# 🎯 Roadmap de Produção - MediasAPI

> **Documento privado de planejamento para deploy em produção (AWS)**
> **Criado em:** 2025-12-11
> **Status:** Em Progresso
> **Objetivo:** Deploy profissional na AWS em 4 semanas

---

## 📊 Status Atual

**Progresso Geral:** 80% pronto para produção

### ✅ O que JÁ TEMOS (Fundação Sólida)

#### **1. Arquitetura e Código**
- ✅ API REST versionada (`/api/v1/`)
- ✅ Autenticação JWT com OAuth2 (chaves RSA)
- ✅ Dual security chain (REST + Web)
- ✅ Migrations automáticas (Flyway)
- ✅ Multi-stage Docker build otimizado
- ✅ Usuário não-root nos containers
- ✅ Healthchecks configurados
- ✅ Logs estruturados JSON
- ✅ Observabilidade (ELK Stack local)
- ✅ API documentada (Swagger/OpenAPI)
- ✅ 14 classes de teste
- ✅ CI básico (GitHub Actions)

#### **2. Segurança**
- ✅ Secrets em variáveis de ambiente
- ✅ `.env` no `.gitignore`
- ✅ CORS configurado
- ✅ SSL/TLS (Nginx)
- ✅ Validação de inputs (Bean Validation)
- ✅ Spring Security configurado
- ✅ SQL injection protegido (JPA + prepared statements)
- ✅ XSS protegido (Thymeleaf escapa por padrão)
- ✅ Senhas com BCrypt

---

## 🔴 BLOQUEADORES - Resolver ANTES do Deploy

### **1. Testes Quebrados**
**Status:** ❌ Pendente
**Problema:** 4 testes com erro - Profile "local" sem application-local.properties
**Prioridade:** CRÍTICA

**Solução:**
```bash
# Criar arquivo de configuração de teste
cat > src/test/resources/application-local.properties << 'EOF'
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
JWT_PUBLIC_KEY_CONTENT=dummy
JWT_PRIVATE_KEY_CONTENT=dummy
DATABASE_URL=jdbc:h2:mem:testdb
DATABASE_USERNAME=sa
DATABASE_PASSWORD=
EOF

# Validar
./mvnw test
```

**Checklist:**
- [ ] Criar `src/test/resources/application-local.properties`
- [ ] Rodar todos os testes
- [ ] Verificar se todos passam (79 testes, 0 errors)
- [ ] Commitar fix

---

### **2. Versão SNAPSHOT em Produção**
**Status:** ❌ Pendente
**Problema:** `<version>0.0.1-SNAPSHOT</version>` não é apropriado para produção
**Prioridade:** ALTA

**Solução:**
```xml
<!-- pom.xml -->
<version>1.0.0</version>
```

**Estratégia de Versionamento Semântico:**
- `1.0.0` → Primeira versão em produção
- `1.0.x` → Bug fixes
- `1.x.0` → Novas features (backward compatible)
- `x.0.0` → Breaking changes

**Checklist:**
- [ ] Alterar versão no `pom.xml` para `1.0.0`
- [ ] Documentar changelog (criar `CHANGELOG.md`)
- [ ] Criar tag git: `git tag -a v1.0.0 -m "Release v1.0.0"`
- [ ] Atualizar documentação da API

---

### **3. Logs SQL em Produção**
**Status:** ❌ Pendente
**Problema:** `spring.jpa.show-sql=true` causa vazamento de dados + overhead de performance
**Prioridade:** ALTA

**Solução:**
```bash
# Criar profile de produção
cat > src/main/resources/application-prod.properties << 'EOF'
# Performance
spring.jpa.show-sql=false
spring.jpa.open-in-view=false

# Logging
logging.level.org.springframework.web=WARN
logging.level.br.com.gustavohenrique.MediasAPI=INFO
logging.level.org.hibernate.SQL=WARN
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=WARN

# Security - Swagger desabilitado em prod
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false

# Actuator - apenas healthcheck
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=when-authorized

# Hibernate
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.use_sql_comments=false

# Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
EOF
```

**Checklist:**
- [ ] Criar `application-prod.properties`
- [ ] Remover `spring.jpa.show-sql=true` do `application.properties` (deixar false como padrão)
- [ ] Adicionar warning deprecation do MySQL dialect
- [ ] Testar localmente com profile prod: `SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run`
- [ ] Documentar profiles no README

---

### **4. Secrets Management**
**Status:** ⚠️ Parcial
**Problema:** Arquivo `.env` existe localmente (risco de commit acidental)
**Prioridade:** ALTA

**Solução:**
- **Desenvolvimento:** Continuar com `.env` (já está no .gitignore)
- **Produção:** AWS Secrets Manager ou Systems Manager Parameter Store

**Secrets necessários:**
```
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
JWT_PUBLIC_KEY_CONTENT
JWT_PRIVATE_KEY_CONTENT
MYSQL_ROOT_PASSWORD
MYSQL_DATABASE
```

**Checklist:**
- [ ] Verificar que `.env` está no `.gitignore` ✅
- [ ] Criar `.env.example` com placeholders (para documentação)
- [ ] Provisionar secrets no AWS Secrets Manager (Semana 2)
- [ ] Atualizar ECS Task Definition para usar secrets

---

## 🟡 IMPORTANTES - Resolver na Primeira Semana

### **5. CI/CD Completo**
**Status:** ⚠️ Parcial (só testa, não faz deploy)
**Prioridade:** ALTA

**Atual:**
```yaml
# .github/workflows/ci.yml
- Checkout
- Setup JDK
- Run tests
```

**Objetivo:**
```yaml
# .github/workflows/deploy.yml
- Checkout
- Setup JDK
- Run tests
- Build Docker image
- Push to AWS ECR
- Update ECS Service (rolling deployment)
- Run smoke tests
- Notify (Slack/Discord)
```

**Checklist:**
- [ ] Criar workflow `deploy.yml` separado para deploy em produção
- [ ] Configurar AWS credentials como GitHub Secrets
- [ ] Testar push para ECR
- [ ] Implementar deploy automático no ECS
- [ ] Adicionar rollback automático em caso de falha

---

### **6. Backup de Banco de Dados**
**Status:** ❌ Pendente
**Prioridade:** CRÍTICA

**Estratégia:**
1. **Automated Backups (RDS):**
   - Retenção: 7 dias (free tier) → 30 dias (produção)
   - Backup window: 3-4 AM (horário de menor uso)
   - Multi-AZ para alta disponibilidade

2. **Snapshots Manuais:**
   - Antes de cada migration importante
   - Antes de releases com mudanças de schema

3. **Point-in-Time Recovery:**
   - Habilitar para restaurar para qualquer momento nos últimos 7-35 dias

**Checklist:**
- [ ] Configurar automated backups no RDS (Semana 2)
- [ ] Documentar procedimento de restore
- [ ] Testar restore em ambiente de staging
- [ ] Criar script para snapshot manual
- [ ] Adicionar alerta quando backup falha

---

### **7. Monitoramento e Observabilidade**
**Status:** ⚠️ Parcial (ELK local, falta cloud monitoring)
**Prioridade:** ALTA

**Atual:**
- ✅ Logs estruturados JSON
- ✅ ELK Stack (local)
- ✅ Actuator healthcheck

**Objetivo:**
```
Logs: CloudWatch Logs (substituir ELK em prod)
Métricas: CloudWatch Metrics + Custom Metrics
Tracing: AWS X-Ray
Dashboards: CloudWatch Dashboards
Alertas: CloudWatch Alarms → SNS → Email/SMS
```

**Métricas Críticas:**
```
- CPU utilization (threshold: 80%)
- Memory utilization (threshold: 85%)
- HTTP 5xx errors (threshold: 10/min)
- HTTP latency P95 (threshold: 500ms)
- Database connections (threshold: 80% do pool)
- RDS storage (threshold: 80% usado)
```

**Checklist:**
- [ ] Configurar CloudWatch Logs (streaming de logs JSON)
- [ ] Criar Dashboard CloudWatch com métricas principais
- [ ] Configurar 6 alarmes críticos (CPU, memória, erros, latência, DB)
- [ ] Integrar AWS X-Ray para distributed tracing
- [ ] Criar SNS topic para notificações
- [ ] Testar alertas

---

### **8. Rate Limiting**
**Status:** ❌ Pendente
**Prioridade:** MÉDIA-ALTA

**Problema:** API vulnerável a abuse (DDoS, scrapers, brute force)

**Solução com Bucket4j:**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.1.0</version>
</dependency>
```

**Configuração:**
```java
// RateLimitingFilter.java
@Component
public class RateLimitingFilter implements Filter {
    // 100 requests por minuto por IP
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> createBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("{\"error\": \"Rate limit exceeded\"}");
        }
    }

    private Bucket createBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
            .build();
    }
}
```

**Checklist:**
- [ ] Adicionar dependência Bucket4j
- [ ] Implementar RateLimitingFilter
- [ ] Configurar limites por endpoint (login: 5/min, API: 100/min)
- [ ] Adicionar header `X-RateLimit-Remaining`
- [ ] Testar com JMeter/k6
- [ ] Documentar limites no Swagger

---

### **9. HTTPS com Certificado Real**
**Status:** ⚠️ Parcial (certificado autoassinado)
**Prioridade:** ALTA

**Solução AWS:**
```
ALB (Application Load Balancer)
  └─ HTTPS:443 (certificado ACM)
      └─ HTTP:8080 (MediasAPI container)
```

**Checklist:**
- [ ] Registrar domínio (ou usar Route 53)
- [ ] Solicitar certificado no ACM (gratuito)
- [ ] Configurar ALB com listener HTTPS:443
- [ ] Redirecionar HTTP → HTTPS no ALB
- [ ] Atualizar CORS para aceitar domínio de produção
- [ ] Testar com SSL Labs (objetivo: A+ rating)

---

## 🟢 RECOMENDADOS - Evoluir Gradualmente

### **10. Cache de Aplicação**
**Status:** ❌ Pendente
**Prioridade:** MÉDIA
**Impacto:** Performance (reduzir cálculos repetitivos)

**Problema:** Cálculos de média executados toda vez que a projeção é consultada

**Solução:**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**Implementação:**
```java
@Service
@CacheConfig(cacheNames = "projections")
public class ProjectionServiceImpl {

    @Cacheable(key = "#projectionId")
    public ProjectionDTO getProjection(Long projectionId) {
        // Cálculos pesados aqui
    }

    @CacheEvict(key = "#projectionId")
    public void updateAssessment(Long projectionId, AssessmentDTO dto) {
        // Invalida cache quando nota é atualizada
    }
}
```

**Checklist:**
- [ ] Provisionar ElastiCache Redis (t3.micro)
- [ ] Configurar Spring Cache com Redis
- [ ] Adicionar `@Cacheable` em métodos de leitura
- [ ] Adicionar `@CacheEvict` em métodos de escrita
- [ ] Definir TTL apropriado (ex: 5 minutos)
- [ ] Monitorar hit rate no CloudWatch

---

### **11. Cobertura de Testes**
**Status:** ⚠️ Parcial (14 testes, sem relatório de cobertura)
**Prioridade:** MÉDIA

**Adicionar JaCoCo:**
```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.70</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**Meta de Cobertura:**
- Controllers: 80%+
- Services: 85%+
- Repositories: 70%+
- Global: 70%+

**Checklist:**
- [ ] Adicionar plugin JaCoCo
- [ ] Rodar `./mvnw test` e verificar relatório em `target/site/jacoco/index.html`
- [ ] Identificar classes sem cobertura
- [ ] Escrever testes unitários para services
- [ ] Escrever testes de integração para fluxos críticos
- [ ] Configurar GitHub Actions para falhar se cobertura < 70%

---

### **12. Feature Flags**
**Status:** ❌ Pendente
**Prioridade:** BAIXA (importante para evolução contínua)

**Casos de Uso:**
- Lançar features incrementalmente (beta users)
- Rollback instantâneo sem deploy
- A/B testing
- Dark launches (deploy sem ativar)

**Opções:**
1. **LaunchDarkly** (SaaS, $10/mês tier gratuito)
2. **AWS AppConfig** (serverless, pay-per-use)
3. **Togglz** (open-source Java)

**Checklist:**
- [ ] Avaliar ferramentas (recomendo AppConfig por custo)
- [ ] Implementar client SDK
- [ ] Definir flags iniciais (ex: `enable_cache`, `new_ui_beta`)
- [ ] Criar dashboard de controle
- [ ] Documentar processo de rollout

---

### **13. API Versionamento e Deprecation**
**Status:** ⚠️ Parcial (v1 existe, falta estratégia de evolução)
**Prioridade:** BAIXA

**Estratégia:**
```
/api/v1/courses  → Versão atual (mantida por 12 meses após v2)
/api/v2/courses  → Nova versão com breaking changes
```

**Headers de Deprecation:**
```http
HTTP/1.1 200 OK
Deprecation: true
Sunset: Sat, 31 Dec 2026 23:59:59 GMT
Link: </api/v2/courses>; rel="successor-version"
```

**Checklist:**
- [ ] Documentar política de versionamento
- [ ] Implementar middleware de deprecation headers
- [ ] Planejar v2 quando necessário
- [ ] Comunicar breaking changes com antecedência (6+ meses)

---

## 🏗️ Arquitetura AWS

### **Opção 1: Custo-Benefício (MVP/Startup)**

```
                         Internet
                            │
                            ├─→ Route 53 (DNS)
                            │     └─→ mediasapi.com
                            │
                            └─→ Application Load Balancer (ALB)
                                  ├─ Listener HTTPS:443 (ACM cert)
                                  ├─ Target Group: ECS Tasks
                                  └─ Health Check: /actuator/health/liveness
                                        │
                                        ├─→ ECS Fargate Cluster
                                        │     ├─ Service: mediasapi
                                        │     ├─ Tasks: 1-2 (auto-scaling)
                                        │     └─ Image: ECR
                                        │
                                        ├─→ RDS MySQL 8.0
                                        │     ├─ Instance: db.t3.micro
                                        │     ├─ Multi-AZ: Sim
                                        │     ├─ Storage: 20GB SSD
                                        │     └─ Backups: 7 dias
                                        │
                                        └─→ CloudWatch Logs + Metrics
                                              └─ Alarms → SNS → Email

Secrets: AWS Secrets Manager
CI/CD: GitHub Actions → ECR → ECS
CDN: CloudFront (opcional - Fase 2)
Cache: ElastiCache Redis (opcional - Fase 2)
```

**Estimativa de Custo: $70-100/mês**

| Serviço | Configuração | Custo/mês |
|---------|--------------|-----------|
| ECS Fargate | 1 task (0.5 vCPU, 1GB RAM, 24/7) | $15 |
| RDS MySQL | db.t3.micro Multi-AZ | $30 |
| ALB | 1 ALB + 10GB tráfego | $20 |
| ECR | 1GB imagens | $0.10 |
| Secrets Manager | 2 secrets | $0.80 |
| CloudWatch Logs | 5GB logs/mês | $2.50 |
| Route 53 | 1 hosted zone + 1M queries | $0.50 |
| Data Transfer | 10GB out | $0.90 |
| **Total** | | **~$70/mês** |

---

### **Opção 2: Produção Profissional (Scale-Ready)**

```
                         Internet
                            │
                            └─→ CloudFront (CDN + WAF)
                                  ├─ DDoS Protection (Shield)
                                  ├─ Cache estático (24h)
                                  └─ Geo-restriction (opcional)
                                        │
                                        └─→ Route 53
                                              └─→ ALB Multi-AZ
                                                    │
                                                    ├─→ ECS Fargate (Auto Scaling 2-10 tasks)
                                                    │     ├─ Target tracking: CPU 70%
                                                    │     └─ Image: ECR (multi-region replication)
                                                    │
                                                    ├─→ RDS Aurora MySQL (Serverless v2)
                                                    │     ├─ Multi-AZ + Read Replicas
                                                    │     ├─ Auto-scaling storage
                                                    │     └─ Backups: 30 dias + snapshots
                                                    │
                                                    ├─→ ElastiCache Redis Cluster
                                                    │     ├─ 2 nodes (replication)
                                                    │     └─ Automatic failover
                                                    │
                                                    └─→ OpenSearch Service (substitui ELK)
                                                          ├─ 3 data nodes (t3.small)
                                                          └─ Kibana integrado

Monitoring: CloudWatch + X-Ray + Custom Dashboards
CI/CD: CodePipeline → CodeBuild → ECR → ECS (Blue/Green)
Secrets: Secrets Manager (rotation automática)
```

**Estimativa de Custo: $300-500/mês**

---

## 📅 Roadmap de Implementação (4 Semanas)

### **Semana 1: Preparação e Hardening** ⏰ Prazo: 18/12/2025

**Objetivo:** Aplicação production-ready

#### Segunda-feira (2h)
- [ ] Corrigir testes quebrados
- [ ] Criar `application-local.properties`
- [ ] Validar que todos os 79 testes passam
- [ ] Commitar fix

#### Terça-feira (3h)
- [ ] Alterar versão para `1.0.0`
- [ ] Criar `application-prod.properties`
- [ ] Remover `show-sql=true` do default
- [ ] Adicionar JaCoCo ao `pom.xml`
- [ ] Rodar testes e gerar relatório de cobertura

#### Quarta-feira (2h)
- [ ] Criar `.env.example` documentando variáveis
- [ ] Documentar profiles no README
- [ ] Criar `CHANGELOG.md` inicial
- [ ] Atualizar documentação da API

#### Quinta-feira (3h)
- [ ] Adicionar warning do Hibernate dialect ao backlog
- [ ] Configurar `spring.jpa.open-in-view=false`
- [ ] Testar aplicação com profile prod localmente
- [ ] Verificar que Swagger está desabilitado no profile prod

#### Sexta-feira (2h)
- [ ] Code review geral
- [ ] Criar tag git `v1.0.0`
- [ ] Push de todas as mudanças
- [ ] **Milestone 1 concluído! ✅**

---

### **Semana 2: Infraestrutura AWS** ⏰ Prazo: 25/12/2025

**Objetivo:** Ambiente AWS provisionado

#### Segunda-feira (4h)
- [ ] Criar conta AWS (free tier)
- [ ] Configurar MFA na conta root
- [ ] Criar IAM user para deploy (com políticas mínimas)
- [ ] Instalar AWS CLI: `aws configure`

#### Terça-feira (4h)
- [ ] Criar VPC (10.0.0.0/16)
  - Public subnets: 10.0.1.0/24, 10.0.2.0/24 (para ALB)
  - Private subnets: 10.0.11.0/24, 10.0.12.0/24 (para ECS, RDS)
- [ ] Criar Internet Gateway
- [ ] Criar NAT Gateway (para private subnets)
- [ ] Configurar Route Tables

#### Quarta-feira (4h)
- [ ] Criar Security Groups:
  - `sg-alb`: Allow 80, 443 from 0.0.0.0/0
  - `sg-ecs`: Allow 8080 from sg-alb
  - `sg-rds`: Allow 3306 from sg-ecs
- [ ] Provisionar RDS MySQL
  - Engine: MySQL 8.0
  - Instance: db.t3.micro
  - Multi-AZ: Yes
  - Storage: 20GB gp3
  - Automated backups: 7 dias
  - **⚠️ Subnet: PRIVATE (sem acesso público)**

#### Quinta-feira (3h)
- [ ] Criar ECR repository: `mediasapi`
- [ ] Autenticar Docker local com ECR
- [ ] Build e push primeira imagem:
  ```bash
  aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com
  docker build -t mediasapi:1.0.0 .
  docker tag mediasapi:1.0.0 <account-id>.dkr.ecr.us-east-1.amazonaws.com/mediasapi:1.0.0
  docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/mediasapi:1.0.0
  ```

#### Sexta-feira (4h)
- [ ] Criar secrets no AWS Secrets Manager:
  - `mediasapi/database/url`
  - `mediasapi/database/username`
  - `mediasapi/database/password`
  - `mediasapi/jwt/public-key`
  - `mediasapi/jwt/private-key`
- [ ] Testar acesso aos secrets via CLI
- [ ] **Milestone 2 concluído! ✅**

---

### **Semana 3: Deploy e CI/CD** ⏰ Prazo: 01/01/2026

**Objetivo:** Aplicação rodando em produção

#### Segunda-feira (4h)
- [ ] Criar ALB (Application Load Balancer)
  - Scheme: internet-facing
  - Subnets: public subnets
  - Security group: sg-alb
- [ ] Solicitar certificado ACM para domínio
- [ ] Configurar listener HTTPS:443 (ACM cert)
- [ ] Configurar redirect HTTP → HTTPS
- [ ] Criar Target Group (health check: `/actuator/health/liveness`)

#### Terça-feira (5h)
- [ ] Criar ECS Cluster (Fargate)
- [ ] Criar Task Definition:
  - CPU: 512 (0.5 vCPU)
  - Memory: 1024 MB
  - Container port: 8080
  - Environment variables: `SPRING_PROFILES_ACTIVE=prod`
  - Secrets: from Secrets Manager
  - Logging: CloudWatch Logs
- [ ] Criar ECS Service:
  - Desired tasks: 1
  - Load balancer: ALB target group
  - Health check grace period: 60s

#### Quarta-feira (3h)
- [ ] Conectar ao RDS via bastion host ou Cloud9
- [ ] Rodar migrations manualmente (primeira vez):
  ```bash
  # Temporariamente permitir acesso do IP local ao RDS
  # Rodar Flyway migrations
  ```
- [ ] Verificar schema criado corretamente
- [ ] Remover acesso temporário

#### Quinta-feira (4h)
- [ ] Configurar Route 53:
  - Criar hosted zone
  - Adicionar registro A (alias para ALB)
  - Validar DNS propagation
- [ ] Testar aplicação via domínio:
  ```bash
  curl https://mediasapi.com/actuator/health
  curl https://mediasapi.com/api/v1/...
  ```
- [ ] Criar usuário de teste via API

#### Sexta-feira (4h)
- [ ] Criar workflow GitHub Actions: `.github/workflows/deploy-prod.yml`
  ```yaml
  name: Deploy to Production

  on:
    push:
      tags:
        - 'v*.*.*'

  jobs:
    deploy:
      runs-on: ubuntu-latest
      steps:
        - uses: actions/checkout@v4

        - name: Configure AWS credentials
          uses: aws-actions/configure-aws-credentials@v4
          with:
            aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
            aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
            aws-region: us-east-1

        - name: Login to ECR
          run: |
            aws ecr get-login-password | docker login --username AWS --password-stdin ${{ secrets.ECR_REGISTRY }}

        - name: Build and push
          run: |
            docker build -t mediasapi:${{ github.ref_name }} .
            docker tag mediasapi:${{ github.ref_name }} ${{ secrets.ECR_REGISTRY }}/mediasapi:${{ github.ref_name }}
            docker push ${{ secrets.ECR_REGISTRY }}/mediasapi:${{ github.ref_name }}

        - name: Update ECS service
          run: |
            aws ecs update-service --cluster mediasapi-cluster --service mediasapi-service --force-new-deployment
  ```
- [ ] Configurar GitHub Secrets (AWS credentials, ECR registry)
- [ ] Testar deploy automático
- [ ] **Milestone 3 concluído! 🚀**

---

### **Semana 4: Observabilidade e Hardening** ⏰ Prazo: 08/01/2026

**Objetivo:** Monitoramento robusto e aplicação hardened

#### Segunda-feira (3h)
- [ ] Configurar CloudWatch Alarms:
  1. **ECS CPU > 80%** (5 min) → SNS
  2. **ECS Memory > 85%** (5 min) → SNS
  3. **ALB 5xx errors > 10** (1 min) → SNS
  4. **ALB Target Response Time P95 > 500ms** (5 min) → SNS
  5. **RDS CPU > 75%** (10 min) → SNS
  6. **RDS Storage < 20%** (1 min) → SNS
  7. **RDS Connections > 80** (5 min) → SNS
- [ ] Criar SNS topic e subscription (email)

#### Terça-feira (4h)
- [ ] Criar CloudWatch Dashboard:
  - ECS metrics (CPU, memory, task count)
  - ALB metrics (requests, latency, errors)
  - RDS metrics (CPU, connections, storage)
  - Custom metrics (se houver)
- [ ] Configurar CloudWatch Logs Insights queries salvas:
  - Top 10 erros
  - Latência P95 por endpoint
  - Requisições por usuário

#### Quarta-feira (4h)
- [ ] Implementar rate limiting (Bucket4j)
  - 100 req/min por IP (global)
  - 5 req/min para `/login`
  - 10 req/min para `/register`
- [ ] Testar com k6 ou JMeter
- [ ] Validar que 429 é retornado

#### Quinta-feira (3h)
- [ ] Configurar ECS Auto Scaling:
  - Min tasks: 1
  - Max tasks: 3
  - Target tracking: CPU 70%
- [ ] Executar teste de carga para validar scaling
- [ ] Documentar comportamento observado

#### Sexta-feira (3h)
- [ ] Backup manual do RDS (snapshot)
- [ ] Documentar runbook de incidentes:
  - Como fazer rollback
  - Como acessar logs
  - Como escalar manualmente
  - Contatos de emergência
- [ ] Teste de restore do backup
- [ ] **Milestone 4 concluído! 🎉**

---

## 🔒 Checklist de Segurança Final

**Antes de considerar produção:**

### Código e Configuração
- [ ] Secrets não estão no código fonte ✅
- [ ] `.env` no `.gitignore` ✅
- [ ] Versão não é SNAPSHOT
- [ ] `show-sql=false` em produção
- [ ] Swagger UI desabilitado em produção
- [ ] Actuator endpoints limitados e protegidos
- [ ] CORS configurado apenas para domínios conhecidos
- [ ] Rate limiting implementado

### AWS
- [ ] RDS em subnet privada (sem acesso público)
- [ ] Security Groups com regras mínimas necessárias
- [ ] Secrets no AWS Secrets Manager
- [ ] Certificado SSL válido (ACM)
- [ ] MFA habilitado na conta root
- [ ] IAM users com políticas de least privilege
- [ ] CloudTrail habilitado (auditoria)
- [ ] Automated backups configurados

### Aplicação
- [ ] HTTPS obrigatório (redirect HTTP → HTTPS)
- [ ] SQL injection protegido (JPA prepared statements) ✅
- [ ] XSS protegido (Thymeleaf escaping) ✅
- [ ] CSRF protection habilitado ✅
- [ ] Senhas com BCrypt ✅
- [ ] JWT tokens com expiração apropriada
- [ ] Logs não vazam dados sensíveis (passwords, tokens)
- [ ] Headers de segurança (HSTS, X-Content-Type-Options)

### Monitoramento
- [ ] Healthchecks não expõem dados internos
- [ ] Alertas configurados para 7 métricas críticas
- [ ] Dashboard de monitoramento funcional
- [ ] Logs centralizados e pesquisáveis
- [ ] Runbook de incidentes documentado

---

## 📊 Métricas de Sucesso (SLIs)

**Definir após 1 mês em produção:**

### Disponibilidade
- **Target:** 99.5% uptime (43 min downtime/mês)
- **Medição:** CloudWatch Synthetics (canary)
- **Ação:** Se < 99%, investigar e melhorar

### Latência
- **Target P50:** < 200ms
- **Target P95:** < 500ms
- **Target P99:** < 1000ms
- **Medição:** ALB metrics
- **Ação:** Se P95 > 500ms, otimizar (cache, indexes, queries N+1)

### Taxa de Erro
- **Target:** < 1% de requests com erro (4xx + 5xx)
- **Medição:** ALB metrics
- **Ação:** Se > 1%, investigar top errors

### Throughput
- **Baseline:** Medir nas primeiras 2 semanas
- **Target:** Suportar 2x baseline sem degradação
- **Medição:** ALB request count
- **Ação:** Auto-scaling deve ativar se throughput > 1.5x baseline

---

## 💰 Otimização de Custos

### Imediato (Free Tier)
- RDS: db.t3.micro elegível por 12 meses (750h/mês)
- ECS Fargate: $500 de crédito por 12 meses (new accounts)
- CloudWatch: 10 alarmes gratuitos sempre

### Após Free Tier (12 meses)
- [ ] Avaliar Reserved Instances para RDS (30-50% economia)
- [ ] Considerar Savings Plans para Fargate
- [ ] Revisar logs retention (reduzir de 7 para 3 dias)
- [ ] Implementar S3 lifecycle policies para backups antigos

### Otimizações Contínuas
- [ ] Monitorar AWS Cost Explorer mensalmente
- [ ] Configurar Budget Alerts (ex: > $150/mês)
- [ ] Deletar recursos não usados (snapshots antigos, AMIs, volumes)
- [ ] Usar AWS Trusted Advisor para recomendações

---

## 📚 Recursos de Estudo

### AWS
- **AWS Skill Builder:** https://skillbuilder.aws (cursos gratuitos)
- **AWS Well-Architected Framework:** https://aws.amazon.com/architecture/well-architected/
- **AWS Free Tier:** https://aws.amazon.com/free/ (12 meses)

### Spring Boot Production
- **Spring Boot Production Ready:** https://spring.io/guides/gs/production-ready/
- **Spring Security Best Practices:** https://docs.spring.io/spring-security/reference/

### DevOps
- **Terraform AWS:** https://developer.hashicorp.com/terraform/tutorials/aws-get-started (IaC)
- **GitHub Actions:** https://docs.github.com/en/actions

### Monitoramento
- **CloudWatch Best Practices:** https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/Best_Practice_Recommended_Alarms_AWS_Services.html
- **Distributed Tracing (X-Ray):** https://aws.amazon.com/xray/

---

## 🎯 Próximas Evoluções (Pós-Deploy)

### Mês 2
- [ ] Implementar cache com Redis (ElastiCache)
- [ ] Adicionar testes de integração end-to-end
- [ ] Implementar blue-green deployment
- [ ] Configurar AWS X-Ray para tracing

### Mês 3
- [ ] Migrar logs para OpenSearch Service (análise avançada)
- [ ] Implementar feature flags (AWS AppConfig)
- [ ] Adicionar autenticação OAuth2 externa (Google, GitHub)
- [ ] Criar ambiente de staging (mirror de produção)

### Mês 4+
- [ ] Multi-region deployment (disaster recovery)
- [ ] API rate limiting granular por usuário
- [ ] GraphQL endpoint (opcional)
- [ ] Mobile app (React Native / Flutter)
- [ ] Analytics e métricas de negócio (produto)

---

## 📝 Log de Progresso

### 2025-12-11 - Início do Projeto
- ✅ Análise completa da aplicação
- ✅ Roadmap criado
- ⏳ Aguardando início da Semana 1

### [Data] - Semana 1: Preparação
- [ ] Testes corrigidos
- [ ] Versão 1.0.0
- [ ] Profile de produção criado
- [ ] Cobertura de testes medida

### [Data] - Semana 2: AWS Setup
- [ ] Conta AWS criada
- [ ] VPC provisionada
- [ ] RDS criado
- [ ] ECR configurado

### [Data] - Semana 3: Deploy
- [ ] Primeira deploy em produção
- [ ] ALB + certificado SSL
- [ ] CI/CD automático

### [Data] - Semana 4: Observabilidade
- [ ] Alarmes configurados
- [ ] Dashboard criado
- [ ] Rate limiting implementado
- [ ] **PRODUÇÃO ESTÁVEL! 🎉**

---

## 🆘 Troubleshooting

### Problema: Testes falhando com "Failed to load ApplicationContext"
**Solução:** Criar `src/test/resources/application-local.properties` com configurações H2

### Problema: ECS task iniciando mas healthcheck falhando
**Possíveis causas:**
1. Security group não permite tráfego do ALB para ECS na porta 8080
2. Secrets não foram injetados corretamente
3. RDS inacessível (security group)
4. Start period muito curto (aumentar para 60s)

**Debug:**
```bash
aws ecs describe-tasks --cluster mediasapi-cluster --tasks <task-id>
aws logs tail /ecs/mediasapi --follow
```

### Problema: RDS connection timeout
**Possíveis causas:**
1. Security group do RDS não permite conexão do ECS
2. RDS em subnet sem NAT Gateway (ECS não consegue resolver DNS)
3. Connection string incorreta

**Solução:**
```bash
# Verificar security groups
aws ec2 describe-security-groups --group-ids <sg-id>

# Testar conectividade de dentro do ECS
aws ecs execute-command --cluster mediasapi-cluster --task <task-id> --interactive --command "/bin/bash"
# Dentro do container:
nc -zv <rds-endpoint> 3306
```

### Problema: Deploy automático falhou
**Rollback manual:**
```bash
# Voltar para versão anterior da imagem
aws ecs update-service \
  --cluster mediasapi-cluster \
  --service mediasapi-service \
  --task-definition mediasapi:PREVIOUS_VERSION \
  --force-new-deployment
```

---

## ✅ Conclusão

Este roadmap guiará o deploy profissional da MediasAPI na AWS em 4 semanas. A aplicação já está 80% pronta - os 20% restantes são principalmente configuração de infraestrutura e ajustes finos.

**Mantenha este documento atualizado** conforme progride. Use os checkboxes para tracking e documente aprendizados na seção "Log de Progresso".

**Boa sorte! 🚀**
