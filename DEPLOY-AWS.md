# Deploy MediasAPI na AWS

Este guia descreve como fazer o deploy da MediasAPI na AWS usando Terraform (infraestrutura como código) e GitHub Actions (CI/CD automático).

## Arquitetura

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              Internet                                    │
└─────────────────────────────────┬───────────────────────────────────────┘
                                  │
                            :80 / :443
                                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                           AWS Cloud                                      │
│                                                                          │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                        EC2 t3.micro                              │   │
│  │                                                                  │   │
│  │   ┌──────────────────┐      ┌──────────────────┐                │   │
│  │   │      Nginx       │      │    MediasAPI     │                │   │
│  │   │    (Container)   │─────▶│   (Container)    │                │   │
│  │   │   :80 / :443     │      │      :8080       │                │   │
│  │   └──────────────────┘      └────────┬─────────┘                │   │
│  │                                       │                          │   │
│  └───────────────────────────────────────┼──────────────────────────┘   │
│                                          │                               │
│                         :3306            ▼                               │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │                    RDS MySQL 8.0 (db.t3.micro)                    │  │
│  │                    20GB, Encrypted, Private                       │  │
│  └───────────────────────────────────────────────────────────────────┘  │
│                                                                          │
│  ┌─────────────────────┐    ┌─────────────────────────────────────────┐ │
│  │  SSM Parameter      │    │          CloudWatch Logs                │ │
│  │  Store (Secrets)    │    │         /ecs/mediasapi                  │ │
│  └─────────────────────┘    └─────────────────────────────────────────┘ │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

## Custos Estimados

| Serviço | Free Tier (12 meses) | Após Free Tier |
|---------|---------------------|----------------|
| EC2 t3.micro | 750 hrs/mês | ~$8.50/mês |
| RDS db.t3.micro | 750 hrs/mês | ~$13.00/mês |
| EBS (20GB) | 30GB/mês | ~$2.00/mês |
| Elastic IP | Gratuito (quando associado) | ~$3.60/mês |
| CloudWatch Logs | 5GB/mês | Pay per use |
| **Total** | **~$0/mês** | **~$27/mês** |

## Pré-requisitos

1. **Conta AWS** com credenciais configuradas
2. **AWS CLI** instalado e configurado
3. **Terraform** >= 1.0 instalado
4. **Chaves JWT RSA** geradas

### Verificar instalações

```bash
# AWS CLI
aws --version
aws sts get-caller-identity

# Terraform
terraform --version
```

### Gerar chaves JWT (se ainda não tiver)

```bash
# Gerar chave privada
openssl genrsa -out private_key.pem 2048

# Gerar chave pública
openssl rsa -in private_key.pem -pubout -out public_key.pem

# Converter para uma linha (para usar no Terraform)
cat public_key.pem | tr '\n' '\\n'
cat private_key.pem | tr '\n' '\\n'
```

## Deploy - Passo a Passo

### 1. Criar EC2 Key Pair

```bash
# Criar key pair na AWS
aws ec2 create-key-pair \
  --key-name mediasapi-key \
  --query 'KeyMaterial' \
  --output text > mediasapi-key.pem

# Proteger arquivo
chmod 400 mediasapi-key.pem
```

### 2. Obter seu IP público

```bash
curl ifconfig.me
# Resultado: 203.0.113.45
```

### 3. Configurar variáveis do Terraform

```bash
cd infra

# Copiar template
cp terraform.tfvars.example terraform.tfvars

# Editar com seus valores
nano terraform.tfvars
```

Exemplo de `terraform.tfvars`:

```hcl
aws_region        = "us-east-1"
admin_ip_cidr     = "203.0.113.45/32"  # Seu IP
ec2_key_pair_name = "mediasapi-key"
db_password       = "SuaSenhaForte123!"

jwt_public_key_content = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhki...\n-----END PUBLIC KEY-----"

jwt_private_key_content = "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgk...\n-----END PRIVATE KEY-----"
```

### 4. Aplicar Terraform

```bash
# Inicializar Terraform
terraform init

# Verificar plano
terraform plan

# Aplicar (cria infraestrutura)
terraform apply
```

Anote os outputs:
```
ec2_public_ip = "54.xxx.xxx.xxx"
rds_endpoint = "mediasapi-mysql.xxx.us-east-1.rds.amazonaws.com:3306"
ssh_command = "ssh -i mediasapi-key.pem ec2-user@54.xxx.xxx.xxx"
```

### 5. Configurar GitHub Secrets

No repositório GitHub, vá em **Settings → Secrets and variables → Actions** e adicione:

| Secret | Valor |
|--------|-------|
| `DOCKER_USERNAME` | gustavodamassa |
| `DOCKER_PASSWORD` | Token do Docker Hub |
| `EC2_HOST` | IP do output `ec2_public_ip` |
| `EC2_SSH_KEY` | Conteúdo do `mediasapi-key.pem` |

Ver [.github/SECRETS.md](.github/SECRETS.md) para detalhes.

### 6. Primeiro Deploy Manual

Após o Terraform criar a infraestrutura, faça o primeiro deploy:

```bash
# SSH para EC2
ssh -i mediasapi-key.pem ec2-user@54.xxx.xxx.xxx

# Na EC2: Buscar secrets do SSM
/opt/mediasapi/fetch-secrets.sh

# Copiar arquivos do repositório
cd /opt/mediasapi
# (usar scp ou git clone)

# Iniciar aplicação
docker compose -f docker-compose.prod.yml up -d

# Verificar logs
docker compose -f docker-compose.prod.yml logs -f
```

### 7. Deploy Automático (CI/CD)

Após a configuração inicial, todo push para `main` dispara automaticamente:

1. ✅ Testes com Maven
2. ✅ Build da imagem Docker
3. ✅ Push para Docker Hub
4. ✅ Deploy na EC2
5. ✅ Health check

## Verificação

### Testar aplicação

```bash
# Health check
curl -k https://54.xxx.xxx.xxx/actuator/health

# API (depois de criar usuário)
curl -k https://54.xxx.xxx.xxx/api/v1/users
```

### Ver logs no CloudWatch

1. Acesse AWS Console → CloudWatch → Log Groups
2. Procure por `/ecs/mediasapi`
3. Veja os log streams: `mediasapi/*` e `nginx/*`

### SSH para debug

```bash
# Conectar
ssh -i mediasapi-key.pem ec2-user@54.xxx.xxx.xxx

# Ver containers
docker compose -f /opt/mediasapi/docker-compose.prod.yml ps

# Ver logs
docker compose -f /opt/mediasapi/docker-compose.prod.yml logs -f mediasapi

# Reiniciar
docker compose -f /opt/mediasapi/docker-compose.prod.yml restart
```

## Troubleshooting

### Aplicação não inicia

```bash
# Verificar logs do container
docker logs mediasapi-app

# Verificar conectividade com RDS
mysql -h <rds-endpoint> -u mediasapi -p

# Verificar secrets
cat /opt/mediasapi/.env
```

### Erro de conexão SSH

```bash
# Verificar se EC2 está rodando
aws ec2 describe-instances --filters "Name=tag:Name,Values=mediasapi-server"

# Verificar security group permite seu IP
aws ec2 describe-security-groups --group-names mediasapi-ec2-sg
```

### Logs não aparecem no CloudWatch

```bash
# Verificar driver de log do Docker
docker inspect mediasapi-app | jq '.[0].HostConfig.LogConfig'

# Verificar IAM role da EC2
aws sts get-caller-identity
```

## Limpeza (Destruir Infraestrutura)

```bash
cd infra
terraform destroy
```

**Atenção**: Isso remove tudo, incluindo o banco de dados!

## Referências

- [infra/README.md](infra/README.md) - Documentação do Terraform
- [.github/SECRETS.md](.github/SECRETS.md) - Configuração de secrets
- [.github/workflows/ci.yml](.github/workflows/ci.yml) - Pipeline CI/CD
- [docker-compose.prod.yml](docker-compose.prod.yml) - Compose de produção
