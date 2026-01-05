# 🚀 Deploy Gratuito AWS Free Tier - MediasAPI

> **Tempo estimado:** 30-40 minutos
> **Custo:** $0/mês por 12 meses
> **Resultado:** Aplicação rodando em servidor real AWS

---

## 📋 O que vamos usar (100% Gratuito)

- ✅ **EC2 t2.micro** - 750h/mês (24/7 por 12 meses)
- ✅ **20 GB SSD** - Incluído no free tier
- ✅ **IP público** - Gratuito
- ✅ **Docker + Docker Compose** - Mesma stack local
- ✅ **MySQL** - Containerizado
- ✅ **Nginx** - Reverse proxy com SSL

---

## 🎯 Passo 1: Criar Conta AWS (5 min)

### 1.1 Acessar e Criar Conta

```
1. Acessar: https://aws.amazon.com/free
2. Clicar em "Create a Free Account"
3. Preencher informações:
   - Email
   - Password
   - AWS account name
4. Escolher "Personal Account"
5. Informações de pagamento (cartão necessário, mas não cobra)
6. Verificação de identidade (SMS)
7. Escolher plano: "Basic Support - Free"
```

### 1.2 Verificar Email e Login

```
1. Verificar email de confirmação
2. Acessar: https://console.aws.amazon.com
3. Login com email e senha
```

---

## 🔔 Passo 2: Configurar Billing Alerts (IMPORTANTE!)

**⚠️ FAZER ISSO PRIMEIRO para evitar cobranças inesperadas!**

### 2.1 Habilitar Alertas

```
1. No Console AWS, clicar em seu nome (canto superior direito)
2. Account → Billing preferences
3. Marcar:
   ✅ Receive Free Tier Usage Alerts
   ✅ Receive Billing Alerts
4. Email: seu-email@example.com
5. Save preferences
```

### 2.2 Criar Alarme de Custo

```
1. Services → CloudWatch
2. All alarms → Create alarm
3. Select metric → Billing → Total Estimated Charge
4. Conditions:
   - Threshold type: Static
   - Whenever EstimatedCharges is: Greater
   - than: 1 (USD)
5. Next → Create new topic
   - Email: seu-email@example.com
6. Create alarm
7. Confirmar subscription no email
```

---

## 🖥️ Passo 3: Criar EC2 Instance (10 min)

### 3.1 Acessar EC2 Dashboard

```
Services → EC2 → Instances → Launch Instance
```

### 3.2 Configurar Instância

#### Nome
```
mediasapi-server
```

#### Application and OS Images (AMI)

```
Ubuntu Server 22.04 LTS (HVM), SSD Volume Type
64-bit (x86)
✅ Free tier eligible
```

#### Instance Type

```
t2.micro
- 1 vCPU
- 1 GB Memory
✅ Free tier eligible
```

#### Key Pair (login)

```
1. Create new key pair
2. Key pair name: mediasapi-key
3. Key pair type: RSA
4. Private key file format: .pem
5. Create key pair (download automático)
6. Mover para ~/.ssh/:
   mv ~/Downloads/mediasapi-key.pem ~/.ssh/
   chmod 400 ~/.ssh/mediasapi-key.pem
```

#### Network Settings

```
Clicar em "Edit"

Auto-assign public IP: Enable
Firewall (security groups): Create security group

Security group name: mediasapi-sg
Description: Security group for MediasAPI server

Inbound security group rules:
┌─────────────────────────────────────────────────┐
│ Type          Port    Source                    │
├─────────────────────────────────────────────────┤
│ SSH           22      My IP (auto-detectado)    │
│ HTTP          80      0.0.0.0/0 (Anywhere)      │
│ HTTPS         443     0.0.0.0/0 (Anywhere)      │
│ Custom TCP    8080    0.0.0.0/0 (temporário)    │
└─────────────────────────────────────────────────┘
```

#### Configure Storage

```
Root volume:
- Size: 20 GB
- Volume type: gp3
- Delete on termination: Yes
✅ Free tier eligible
```

### 3.3 Launch Instance

```
1. Clicar em "Launch instance"
2. Aguardar status "Running" (~2 minutos)
3. Copiar "Public IPv4 address"
```

**Exemplo de IP:** `18.191.123.45`

---

## 🔌 Passo 4: Conectar via SSH (2 min)

### 4.1 Testar Conectividade

```bash
# Testar se instância está acessível
ping <SEU_IP_PUBLICO>
```

### 4.2 Conectar

```bash
ssh -i ~/.ssh/mediasapi-key.pem ubuntu@<SEU_IP_PUBLICO>

# Exemplo:
# ssh -i ~/.ssh/mediasapi-key.pem ubuntu@18.191.123.45
```

**Primeira conexão:**
```
The authenticity of host '18.191.123.45 (18.191.123.45)' can't be established.
ECDSA key fingerprint is SHA256:xxx...
Are you sure you want to continue connecting (yes/no)? yes
```

**Se conectou com sucesso, verá:**
```
Welcome to Ubuntu 22.04.1 LTS (GNU/Linux 5.15.0-1023-aws x86_64)
ubuntu@ip-172-31-xx-xx:~$
```

---

## 🐳 Passo 5: Instalar Docker na EC2 (5 min)

### 5.1 Atualizar Sistema

```bash
sudo apt update && sudo apt upgrade -y
```

### 5.2 Instalar Docker

```bash
# Download script oficial
curl -fsSL https://get.docker.com -o get-docker.sh

# Instalar
sudo sh get-docker.sh

# Adicionar usuário ao grupo docker
sudo usermod -aG docker ubuntu

# Verificar instalação
docker --version
```

### 5.3 Instalar Docker Compose

```bash
# Instalar
sudo apt install docker-compose -y

# Verificar instalação
docker-compose --version
```

### 5.4 Instalar Git e Ferramentas

```bash
sudo apt install git curl nano -y
```

### 5.5 Reiniciar Sessão SSH

**IMPORTANTE:** Logout e login novamente para grupo docker funcionar

```bash
exit
ssh -i ~/.ssh/mediasapi-key.pem ubuntu@<SEU_IP_PUBLICO>

# Testar docker sem sudo
docker ps  # Deve funcionar sem erro
```

---

## 📦 Passo 6: Preparar Aplicação (10 min)

### 6.1 Clonar Repositório

**Se repositório público:**
```bash
git clone https://github.com/seu-usuario/MediasAPI.git
cd MediasAPI
```

**Se repositório privado:**

**Opção A - HTTPS com Personal Access Token:**
```bash
# 1. Criar token no GitHub:
#    Settings → Developer settings → Personal access tokens → Tokens (classic)
#    → Generate new token → Marcar 'repo' → Generate
# 2. Copiar token
# 3. Clonar:
git clone https://SEU_TOKEN@github.com/seu-usuario/MediasAPI.git
```

**Opção B - SSH (recomendado):**
```bash
# 1. Gerar chave SSH na EC2
ssh-keygen -t ed25519 -C "seu-email@example.com"
# Pressionar Enter 3x (sem passphrase)

# 2. Copiar chave pública
cat ~/.ssh/id_ed25519.pub

# 3. Adicionar no GitHub:
#    Settings → SSH and GPG keys → New SSH key
#    Colar a chave pública

# 4. Clonar
git clone git@github.com:seu-usuario/MediasAPI.git
```

### 6.2 Gerar Chaves JWT

**No seu computador local (não na EC2):**

```bash
# Gerar par de chaves RSA
ssh-keygen -t rsa -b 2048 -m PEM -f app.key -N ""

# Extrair chave pública
openssl rsa -in app.key -pubout -outform PEM -out app.pub

# Converter para base64 (formato que a app espera)
cat app.pub | base64 -w 0 > app.pub.base64
cat app.key | base64 -w 0 > app.key.base64

# Ver conteúdo (copiar para usar no .env)
cat app.pub.base64
cat app.key.base64
```

### 6.3 Criar arquivo .env

**Na EC2:**

```bash
cd MediasAPI

# Criar arquivo .env
nano .env
```

**Conteúdo do .env:**
```bash
# Database
MYSQL_ROOT_PASSWORD=SenhaForte123!@#
MYSQL_DATABASE=mediasdb
DATABASE_URL=jdbc:mysql://db:3306/mediasdb
DATABASE_USERNAME=root
DATABASE_PASSWORD=SenhaForte123!@#

# JWT Keys (colar os valores gerados acima)
JWT_PUBLIC_KEY_CONTENT=<COLAR_CONTEUDO_app.pub.base64>
JWT_PRIVATE_KEY_CONTENT=<COLAR_CONTEUDO_app.key.base64>
```

**Salvar e sair:**
- `CTRL + O` (salvar)
- `ENTER` (confirmar)
- `CTRL + X` (sair)

### 6.4 Verificar arquivo .env

```bash
# Verificar que arquivo foi criado
ls -la .env

# Ver conteúdo (sem mostrar senhas)
cat .env | grep -v PASSWORD
```

---

## 🔧 Passo 7: Simplificar Docker Compose (5 min)

**Por que?** A instância t2.micro tem apenas 1GB de RAM. ELK Stack consome muito.

### 7.1 Backup do original

```bash
cp docker-compose.yaml docker-compose.yaml.backup
```

### 7.2 Criar versão simplificada

```bash
nano docker-compose.yaml
```

**Substituir todo conteúdo por:**

```yaml
services:
  mediasapi:
    build: .
    container_name: mediasapi-app
    environment:
      SPRING_PROFILES_ACTIVE: docker
      DATABASE_URL: jdbc:mysql://db:3306/${MYSQL_DATABASE}
      DATABASE_USERNAME: ${DATABASE_USERNAME}
      DATABASE_PASSWORD: ${DATABASE_PASSWORD}
      JWT_PUBLIC_KEY_CONTENT: ${JWT_PUBLIC_KEY_CONTENT}
      JWT_PRIVATE_KEY_CONTENT: ${JWT_PRIVATE_KEY_CONTENT}
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
    depends_on:
      db:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health/liveness"]
      interval: 15s
      timeout: 5s
      retries: 3
      start_period: 30s
    ports:
      - "8080:8080"
    networks:
      - medias-rede
    restart: unless-stopped

  nginx:
    image: nginx:latest
    container_name: medias-nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/certs:/etc/nginx/certs:ro
    depends_on:
      - mediasapi
    networks:
      - medias-rede
    restart: unless-stopped

  db:
    image: mysql:8.0
    container_name: medias-mysql
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin" ,"ping", "-h", "localhost"]
      interval: 10s
      timeout: 20s
      retries: 10
      start_period: 30s
    networks:
      - medias-rede
    restart: unless-stopped

networks:
  medias-rede:

volumes:
  mysql_data:
```

**Salvar:** `CTRL + O` → `ENTER` → `CTRL + X`

---

## 🚀 Passo 8: Build e Deploy! (10-15 min)

### 8.1 Iniciar Build

```bash
# Build e subir containers em background
docker-compose up --build -d
```

**Isso vai:**
1. Build da imagem Maven (5-8 min)
2. Criar imagem final (1 min)
3. Subir MySQL (30s)
4. Subir MediasAPI (aguarda MySQL healthy)
5. Rodar migrations Flyway automaticamente
6. Subir Nginx

### 8.2 Acompanhar Logs

```bash
# Ver logs em tempo real
docker-compose logs -f

# Ver apenas logs da aplicação
docker-compose logs -f mediasapi

# Sair dos logs: CTRL + C
```

**Aguardar até ver:**
```
mediasapi-app  | Started MediasApiApplication in 6.xxx seconds
```

### 8.3 Verificar Status

```bash
# Ver containers rodando
docker-compose ps

# Ver uso de recursos
docker stats --no-stream
```

**Esperado:**
```
NAME              STATUS                  PORTS
mediasapi-app     Up xx seconds (healthy) 8080/tcp
medias-nginx      Up xx seconds           0.0.0.0:80->80/tcp, 0.0.0.0:443->443/tcp
medias-mysql      Up xx seconds (healthy) 3306/tcp
```

---

## ✅ Passo 9: Testar Aplicação (5 min)

### 9.1 Teste Local (na EC2)

```bash
# Health check
curl http://localhost:8080/actuator/health

# Esperado:
# {"status":"UP",...}
```

### 9.2 Teste Externo (do seu computador)

```bash
# Substituir <SEU_IP_PUBLICO> pelo IP da EC2

# Health check
curl http://<SEU_IP_PUBLICO>/actuator/health

# Swagger UI (abrir no navegador)
http://<SEU_IP_PUBLICO>/swagger-ui/index.html
```

### 9.3 Criar Usuário de Teste

```bash
curl -X POST http://<SEU_IP_PUBLICO>/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin",
    "email": "admin@mediasapi.com",
    "password": "Senha123!",
    "role": "ADMIN"
  }'
```

**Esperado:**
```json
{
  "id": 1,
  "name": "Admin",
  "email": "admin@mediasapi.com",
  "role": "ADMIN"
}
```

### 9.4 Fazer Login

```bash
curl -X POST http://<SEU_IP_PUBLICO>/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@mediasapi.com",
    "password": "Senha123!"
  }'
```

**Esperado:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiY..."
}
```

### 9.5 Testar Endpoint Autenticado

```bash
# Copiar token do passo anterior
TOKEN="eyJhbGciOiJSUzI1NiJ9..."

curl http://<SEU_IP_PUBLICO>/api/v1/users \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🎉 SUCESSO! Aplicação no AR!

**Sua aplicação está rodando em:**
- **HTTP:** `http://<SEU_IP_PUBLICO>`
- **API:** `http://<SEU_IP_PUBLICO>/api/v1/`
- **Swagger:** `http://<SEU_IP_PUBLICO>/swagger-ui/index.html`
- **Health:** `http://<SEU_IP_PUBLICO>/actuator/health`

---

## 🔒 Passo 10: Hardening Básico (Opcional - 10 min)

### 10.1 Remover Porta 8080 do Security Group

```bash
# No Console AWS:
# EC2 → Security Groups → mediasapi-sg → Edit inbound rules
# Remover regra: Custom TCP 8080
# Nginx vai fazer proxy na porta 80
```

### 10.2 Configurar Firewall (UFW)

```bash
# Habilitar firewall
sudo ufw enable

# Permitir apenas o necessário
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS

# Verificar status
sudo ufw status
```

### 10.3 Configurar Auto-restart

Os containers já têm `restart: unless-stopped` no docker-compose.yaml ✅

### 10.4 Configurar Swap (memória virtual)

```bash
# Criar arquivo swap de 2GB
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# Tornar permanente
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Verificar
free -h
```

---

## 📊 Passo 11: Monitoramento Básico

### 11.1 Ver Logs

```bash
# Logs em tempo real
docker-compose logs -f

# Últimas 100 linhas
docker-compose logs --tail=100

# Logs de um serviço específico
docker-compose logs -f mediasapi
```

### 11.2 Ver Uso de Recursos

```bash
# Uso de CPU/Memória dos containers
docker stats

# Uso do sistema
htop  # ou: top
df -h  # Espaço em disco
```

### 11.3 CloudWatch Básico (Gratuito)

```
Console AWS → CloudWatch → Metrics → EC2
Visualizar:
- CPUUtilization
- NetworkIn/Out
- DiskReadBytes/WriteBytes
```

---

## 🔄 Operações Comuns

### Restart da Aplicação

```bash
docker-compose restart mediasapi
```

### Ver Logs de Erro

```bash
docker-compose logs mediasapi | grep ERROR
```

### Backup do Banco de Dados

```bash
# Criar backup
docker exec medias-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} mediasdb > backup-$(date +%Y%m%d).sql

# Download do backup para seu computador
scp -i ~/.ssh/mediasapi-key.pem ubuntu@<SEU_IP_PUBLICO>:~/MediasAPI/backup-*.sql .
```

### Restore do Banco

```bash
# Upload do backup
scp -i ~/.ssh/mediasapi-key.pem backup-20251211.sql ubuntu@<SEU_IP_PUBLICO>:~/MediasAPI/

# Na EC2
docker exec -i medias-mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} mediasdb < backup-20251211.sql
```

### Atualizar Aplicação

```bash
# Pull das mudanças
git pull origin main

# Rebuild e restart
docker-compose up --build -d

# Ver logs para confirmar
docker-compose logs -f mediasapi
```

### Parar Tudo

```bash
docker-compose down
```

### Parar e Remover Volumes (CUIDADO - Perde dados!)

```bash
docker-compose down -v
```

---

## 🌐 Passo 12: Domínio e HTTPS (Opcional - 20 min)

### Opção A: Domínio Gratuito (DuckDNS)

#### 1. Criar Subdomínio

```
1. Acessar: https://www.duckdns.org
2. Login com GitHub/Google
3. Adicionar subdomínio: mediasapi
4. Apontar para IP da EC2
5. Copiar token
```

#### 2. Instalar Cliente DuckDNS

```bash
# Criar script de atualização
mkdir ~/duckdns
cd ~/duckdns
nano duck.sh
```

**Conteúdo:**
```bash
#!/bin/bash
echo url="https://www.duckdns.org/update?domains=mediasapi&token=SEU_TOKEN&ip=" | curl -k -o ~/duckdns/duck.log -K -
```

**Configurar:**
```bash
chmod 700 duck.sh

# Testar
./duck.sh
cat duck.log  # Deve mostrar "OK"

# Adicionar ao crontab (atualizar a cada 5 min)
crontab -e
# Adicionar linha:
*/5 * * * * ~/duckdns/duck.sh >/dev/null 2>&1
```

### Opção B: HTTPS com Let's Encrypt

**Requer domínio configurado (DuckDNS ou próprio)**

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Parar Nginx container temporariamente
docker-compose stop nginx

# Obter certificado
sudo certbot certonly --standalone -d mediasapi.duckdns.org

# Certificados salvos em:
# /etc/letsencrypt/live/mediasapi.duckdns.org/fullchain.pem
# /etc/letsencrypt/live/mediasapi.duckdns.org/privkey.pem
```

**Atualizar nginx.conf:**
```bash
nano nginx/nginx.conf
```

```nginx
server {
    listen 443 ssl;
    server_name mediasapi.duckdns.org;

    ssl_certificate /etc/letsencrypt/live/mediasapi.duckdns.org/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/mediasapi.duckdns.org/privkey.pem;

    location / {
        proxy_pass http://mediasapi:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

**Atualizar docker-compose.yaml:**
```yaml
nginx:
  volumes:
    - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
    - /etc/letsencrypt:/etc/letsencrypt:ro  # Adicionar esta linha
```

**Restart:**
```bash
docker-compose up -d
```

**Auto-renovação:**
```bash
# Testar renovação
sudo certbot renew --dry-run

# Adicionar ao crontab
sudo crontab -e
# Adicionar:
0 3 * * * certbot renew --quiet && docker-compose -f /home/ubuntu/MediasAPI/docker-compose.yaml restart nginx
```

---

## 💰 Custos e Free Tier

### Primeiros 12 Meses (Free Tier)

| Recurso | Limite Gratuito | Uso Estimado | Custo |
|---------|-----------------|--------------|-------|
| EC2 t2.micro | 750h/mês | 720h (24/7) | $0 |
| EBS Storage | 30 GB | 20 GB | $0 |
| Data Transfer Out | 15 GB/mês | ~5 GB | $0 |
| Data Transfer In | Ilimitado | ~2 GB | $0 |
| **TOTAL** | | | **$0/mês** ✅ |

### Após 12 Meses

| Recurso | Custo Mensal |
|---------|--------------|
| EC2 t2.micro | ~$8.50 |
| EBS 20 GB gp3 | ~$1.60 |
| Data Transfer | ~$0.50 |
| **TOTAL** | **~$10.60/mês** |

---

## ⚠️ Proteções Contra Cobranças

### 1. Billing Alerts Configurados ✅

Você receberá email se passar de $1.

### 2. Budget AWS (Recomendado)

```
Console AWS → Billing → Budgets → Create budget
- Template: Zero spend budget
- Email alerts automáticos
```

### 3. Monitorar Free Tier Usage

```
Billing → Free Tier
Visualizar uso mensal de cada serviço
```

### 4. Desligar quando não usar (opcional)

```bash
# Parar instância (não perde dados)
aws ec2 stop-instances --instance-ids i-xxxxx

# Iniciar novamente
aws ec2 start-instances --instance-ids i-xxxxx
```

**⚠️ Atenção:** Ao parar/iniciar, IP público muda! Considerar Elastic IP (gratuito se anexado).

---

## 🐛 Troubleshooting

### Problema: Cannot connect to SSH

**Possíveis causas:**
1. Security Group não permite SSH do seu IP
2. Key pair incorreto
3. Instância não está rodando

**Solução:**
```bash
# 1. Verificar security group
Console AWS → EC2 → Security Groups → mediasapi-sg
Inbound rules deve ter: SSH (22) from "My IP"

# 2. Verificar permissões da chave
chmod 400 ~/.ssh/mediasapi-key.pem

# 3. Verificar status da instância
Console AWS → EC2 → Instances
Status deve ser "Running"
```

### Problema: Docker build falhou - Out of memory

**Causa:** t2.micro tem apenas 1GB RAM, build Maven consome muito.

**Solução 1 - Adicionar Swap:**
```bash
# Já fizemos isso no Passo 10.4
free -h  # Verificar se swap está ativo
```

**Solução 2 - Build local e push para Docker Hub:**
```bash
# No seu computador local
docker build -t seu-usuario/mediasapi:latest .
docker push seu-usuario/mediasapi:latest

# Na EC2, editar docker-compose.yaml:
# Trocar:
#   build: .
# Por:
#   image: seu-usuario/mediasapi:latest
```

### Problema: Containers não sobem

```bash
# Ver logs detalhados
docker-compose logs

# Ver status
docker-compose ps

# Ver recursos
docker stats --no-stream

# Reiniciar do zero
docker-compose down
docker-compose up -d
```

### Problema: "Permission denied" ao rodar docker

**Causa:** Usuário não está no grupo docker

**Solução:**
```bash
sudo usermod -aG docker ubuntu
exit
# Conectar novamente via SSH
```

### Problema: Aplicação lenta

**Causas:**
1. t2.micro tem CPU limitada
2. Memória insuficiente (swap sendo usado)
3. Queries lentas no banco

**Diagnóstico:**
```bash
# Ver uso de recursos
docker stats

# Ver uso do sistema
htop

# Ver queries lentas
docker exec -it medias-mysql mysql -u root -p${MYSQL_ROOT_PASSWORD}
# No MySQL:
SHOW PROCESSLIST;
```

### Problema: Não consigo acessar pelo navegador

**Checklist:**
```bash
# 1. Containers estão rodando?
docker-compose ps

# 2. Porta 80 está aberta no security group?
Console AWS → EC2 → Security Groups

# 3. Nginx está respondendo?
curl localhost:80  # Na EC2

# 4. IP público está correto?
Console AWS → EC2 → Instances → Public IPv4 address
```

---

## 📚 Comandos Úteis de Referência

### Docker Compose

```bash
# Subir serviços
docker-compose up -d

# Subir com rebuild
docker-compose up --build -d

# Ver logs
docker-compose logs -f [service]

# Restart serviço
docker-compose restart [service]

# Parar tudo
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Ver status
docker-compose ps

# Ver uso de recursos
docker stats
```

### Docker

```bash
# Listar containers
docker ps -a

# Ver logs de container
docker logs -f <container-name>

# Executar comando em container
docker exec -it <container-name> bash

# Remover containers parados
docker container prune

# Remover imagens não usadas
docker image prune -a

# Ver uso de espaço
docker system df
```

### Sistema

```bash
# Uso de disco
df -h

# Uso de memória
free -h

# Processos
htop

# Logs do sistema
journalctl -xe

# Reiniciar servidor (CUIDADO!)
sudo reboot
```

### Git

```bash
# Pull mudanças
git pull origin main

# Ver status
git status

# Ver branches
git branch -a
```

---

## 🎯 Próximos Passos

### Hoje ✅
- [x] Aplicação no ar
- [x] Testada e funcionando
- [x] Billing alerts configurados

### Amanhã
- [ ] Configurar domínio (DuckDNS)
- [ ] Configurar HTTPS (Let's Encrypt)
- [ ] Backup manual do banco

### Semana que vem
- [ ] Elastic IP (IP fixo)
- [ ] Monitoramento CloudWatch
- [ ] CI/CD com GitHub Actions

### Mês que vem
- [ ] Migrar para RDS (banco gerenciado)
- [ ] Application Load Balancer
- [ ] Auto Scaling
- [ ] S3 para backups

---

## 🎓 Aprendizados

### O que você fez hoje:
- ✅ Provisionou servidor Linux na AWS
- ✅ Configurou SSH e segurança
- ✅ Instalou Docker e Docker Compose
- ✅ Fez deploy de aplicação Spring Boot
- ✅ Configurou MySQL em container
- ✅ Configurou Nginx como reverse proxy
- ✅ Gerenciou secrets com variáveis de ambiente

### Habilidades desenvolvidas:
- AWS EC2
- Linux server administration
- Docker em produção
- Networking básico
- Security groups
- SSH key management

---

## 📖 Recursos Adicionais

### Documentação AWS
- **EC2 User Guide:** https://docs.aws.amazon.com/ec2/
- **Free Tier FAQ:** https://aws.amazon.com/free/free-tier-faqs/
- **Security Best Practices:** https://aws.amazon.com/security/best-practices/

### Tutoriais
- **AWS Well-Architected Framework:** https://aws.amazon.com/architecture/well-architected/
- **Docker Best Practices:** https://docs.docker.com/develop/dev-best-practices/

### Comunidade
- **AWS Forums:** https://forums.aws.amazon.com/
- **Stack Overflow:** Tag `amazon-ec2`

---

## ✅ Checklist Final

- [ ] Conta AWS criada
- [ ] Billing alerts configurados
- [ ] EC2 instance provisionada (t2.micro)
- [ ] Security group configurado (SSH, HTTP, HTTPS)
- [ ] Conectado via SSH com sucesso
- [ ] Docker e Docker Compose instalados
- [ ] Repositório clonado
- [ ] Arquivo .env criado com secrets
- [ ] Docker Compose simplificado (sem ELK)
- [ ] Build concluído com sucesso
- [ ] Containers rodando (mediasapi, nginx, mysql)
- [ ] Health check retorna 200 OK
- [ ] Swagger UI acessível
- [ ] Usuário criado via API
- [ ] Login retorna JWT token
- [ ] Endpoint autenticado funciona
- [ ] Firewall configurado (UFW)
- [ ] Swap configurado (2GB)
- [ ] Backup do banco testado

---

## 🎉 Parabéns!

Você tem uma aplicação **Spring Boot profissional rodando na AWS**!

**Sua aplicação está em:**
```
http://<SEU_IP_PUBLICO>
```

**Custo:** $0/mês por 12 meses ✅

**Próximo nível:** Quando dominar isso, partir para arquitetura com:
- RDS (banco gerenciado)
- ECS/EKS (orquestração de containers)
- CloudFront (CDN)
- Route 53 (DNS gerenciado)

---

**Documento criado em:** 2025-12-11
**Última atualização:** 2025-12-11
**Versão:** 1.0.0
