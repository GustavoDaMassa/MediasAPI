# MediasAPI Infrastructure (Terraform)

This directory contains Terraform configuration to deploy MediasAPI on AWS.

## Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                           AWS Cloud                                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                      Default VPC                             │   │
│  │                                                               │   │
│  │   ┌─────────────────┐         ┌─────────────────────────┐   │   │
│  │   │    EC2 t3.micro │         │   RDS db.t3.micro       │   │   │
│  │   │  ┌───────────┐  │         │   MySQL 8.0             │   │   │
│  │   │  │   Nginx   │──┼────────▶│   20GB, encrypted       │   │   │
│  │   │  │  :80/:443 │  │  :3306  │   Private (no public)   │   │   │
│  │   │  └───────────┘  │         └─────────────────────────┘   │   │
│  │   │       │         │                                        │   │
│  │   │  ┌───────────┐  │         ┌─────────────────────────┐   │   │
│  │   │  │ MediasAPI │  │         │   SSM Parameter Store   │   │   │
│  │   │  │   :8080   │  │◀────────│   (Secrets)             │   │   │
│  │   │  └───────────┘  │         └─────────────────────────┘   │   │
│  │   │  ┌───────────┐  │                                        │   │
│  │   │  │  Docker   │  │         ┌─────────────────────────┐   │   │
│  │   │  │  Compose  │──┼────────▶│   CloudWatch Logs       │   │   │
│  │   │  └───────────┘  │  awslogs└─────────────────────────┘   │   │
│  │   └─────────────────┘                                        │   │
│  └───────────────────────────────────────────────────────────────┘   │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

## Resources Created

| Resource | Type | Description |
|----------|------|-------------|
| EC2 Instance | t3.micro | Application server with Docker |
| Elastic IP | - | Static public IP for EC2 |
| RDS MySQL | db.t3.micro | Managed database (20GB) |
| Security Groups | - | EC2 (SSH, HTTP, HTTPS), RDS (MySQL from EC2) |
| IAM Role | - | EC2 role for SSM and CloudWatch |
| SSM Parameters | SecureString | Database credentials, JWT keys |
| CloudWatch Log Group | - | Application logs (14 days retention) |

## Prerequisites

1. **AWS CLI** configured with credentials
2. **Terraform** >= 1.0 installed
3. **EC2 Key Pair** created in AWS Console
4. **Your public IP** for SSH access

## Quick Start

### 1. Create EC2 Key Pair

```bash
# In AWS Console or via CLI:
aws ec2 create-key-pair --key-name mediasapi-key --query 'KeyMaterial' --output text > mediasapi-key.pem
chmod 400 mediasapi-key.pem
```

### 2. Get Your Public IP

```bash
curl ifconfig.me
```

### 3. Configure Variables

```bash
cp terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars with your values
```

### 4. Deploy Infrastructure

```bash
terraform init
terraform plan
terraform apply
```

### 5. Deploy Application

```bash
# SSH into EC2
ssh -i mediasapi-key.pem ec2-user@$(terraform output -raw ec2_public_ip)

# On EC2: Fetch secrets from SSM
/opt/mediasapi/fetch-secrets.sh

# Clone docker-compose.prod.yml and nginx config
# (Or use CI/CD to deploy)
cd /opt/mediasapi
# ... copy docker-compose.prod.yml and nginx/nginx.conf

# Start application
docker compose -f docker-compose.prod.yml up -d
```

## Outputs

After `terraform apply`, you'll see:

| Output | Description |
|--------|-------------|
| `ec2_public_ip` | Public IP of the EC2 instance |
| `rds_endpoint` | MySQL endpoint (host:port) |
| `ssh_command` | Ready-to-use SSH command |
| `application_url` | HTTPS URL to access the app |

## Costs (AWS Free Tier Eligible)

| Service | Free Tier | After Free Tier |
|---------|-----------|-----------------|
| EC2 t3.micro | 750 hrs/month (12 months) | ~$8.50/month |
| RDS db.t3.micro | 750 hrs/month (12 months) | ~$13.00/month |
| EBS (20GB) | 30GB/month | ~$2.00/month |
| CloudWatch | 5GB logs/month | Pay per use |
| **Total** | **$0 (first year)** | **~$25/month** |

## Security Considerations

1. **SSH Access**: Limited to your IP only (`admin_ip_cidr`)
2. **RDS**: Private, not publicly accessible
3. **Secrets**: Stored in SSM Parameter Store (SecureString)
4. **Encryption**: EBS and RDS storage encrypted
5. **IAM**: Minimal permissions (SSM read, CloudWatch write)

## Cleanup

```bash
terraform destroy
```

**Warning**: This will delete all resources including the database!

## Files

| File | Description |
|------|-------------|
| `main.tf` | Main infrastructure configuration |
| `variables.tf` | Input variables |
| `outputs.tf` | Output values |
| `user-data.sh` | EC2 initialization script |
| `terraform.tfvars.example` | Example variables file |

## Troubleshooting

### EC2 not starting containers

```bash
# Check Docker status
sudo systemctl status docker

# Check user-data script log
sudo cat /var/log/user-data.log

# Check cloud-init logs
sudo cat /var/log/cloud-init-output.log
```

### Cannot connect to RDS

```bash
# Test from EC2 (must be inside EC2)
mysql -h $(terraform output -raw rds_address) -u mediasapi -p

# Check security groups allow traffic
```

### Logs not appearing in CloudWatch

```bash
# Check Docker is using awslogs driver
docker inspect mediasapi-app | grep -A 10 LogConfig

# Check IAM role permissions
aws sts get-caller-identity
```
