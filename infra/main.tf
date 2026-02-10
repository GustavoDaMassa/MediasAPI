# =============================================================================
# MediasAPI AWS Infrastructure
# =============================================================================
# This Terraform configuration creates:
# - EC2 t3.micro with Amazon Linux 2023 (application + Nginx)
# - RDS db.t3.micro MySQL 8.0 (managed database)
# - Security Groups (EC2 and RDS)
# - IAM Role for EC2 to read SSM and write CloudWatch logs
# - SSM Parameter Store for secrets
# - CloudWatch Log Group
# =============================================================================

terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# =============================================================================
# Data Sources
# =============================================================================

data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_ami" "amazon_linux_2023" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# =============================================================================
# VPC and Networking (using default VPC)
# =============================================================================

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# =============================================================================
# Security Groups
# =============================================================================

# EC2 Security Group
resource "aws_security_group" "ec2_sg" {
  name        = "mediasapi-ec2-sg"
  description = "Security group for MediasAPI EC2 instance"
  vpc_id      = data.aws_vpc.default.id

  # SSH (open for CI/CD GitHub Actions and admin access)
  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTP
  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTPS
  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Outbound: all traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "mediasapi-ec2-sg"
  }
}

# RDS Security Group
resource "aws_security_group" "rds_sg" {
  name        = "mediasapi-rds-sg"
  description = "Security group for MediasAPI RDS instance"
  vpc_id      = data.aws_vpc.default.id

  # MySQL from EC2 only
  ingress {
    description     = "MySQL from EC2"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.ec2_sg.id]
  }

  tags = {
    Name = "mediasapi-rds-sg"
  }
}

# =============================================================================
# IAM Role for EC2
# =============================================================================

resource "aws_iam_role" "ec2_role" {
  name = "mediasapi-ec2-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "mediasapi-ec2-role"
  }
}

# Policy for SSM Parameter Store read access
resource "aws_iam_role_policy" "ssm_read_policy" {
  name = "mediasapi-ssm-read-policy"
  role = aws_iam_role.ec2_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters",
          "ssm:GetParametersByPath"
        ]
        Resource = "arn:aws:ssm:${var.aws_region}:*:parameter/mediasapi/*"
      }
    ]
  })
}

# Policy for CloudWatch Logs
resource "aws_iam_role_policy" "cloudwatch_logs_policy" {
  name = "mediasapi-cloudwatch-logs-policy"
  role = aws_iam_role.ec2_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams"
        ]
        Resource = "arn:aws:logs:${var.aws_region}:*:log-group:/ecs/mediasapi*"
      }
    ]
  })
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "mediasapi-ec2-profile"
  role = aws_iam_role.ec2_role.name
}

# =============================================================================
# SSM Parameter Store
# =============================================================================

resource "aws_ssm_parameter" "database_url" {
  name        = "/mediasapi/DATABASE_URL"
  description = "Database connection URL for MediasAPI"
  type        = "SecureString"
  value       = "jdbc:mysql://${aws_db_instance.mysql.endpoint}/${var.db_name}"

  tags = {
    Application = "MediasAPI"
  }
}

resource "aws_ssm_parameter" "database_username" {
  name        = "/mediasapi/DATABASE_USERNAME"
  description = "Database username for MediasAPI"
  type        = "SecureString"
  value       = var.db_username

  tags = {
    Application = "MediasAPI"
  }
}

resource "aws_ssm_parameter" "database_password" {
  name        = "/mediasapi/DATABASE_PASSWORD"
  description = "Database password for MediasAPI"
  type        = "SecureString"
  value       = var.db_password

  tags = {
    Application = "MediasAPI"
  }
}

resource "aws_ssm_parameter" "jwt_public_key" {
  name        = "/mediasapi/JWT_PUBLIC_KEY_CONTENT"
  description = "JWT public key for MediasAPI"
  type        = "SecureString"
  value       = var.jwt_public_key_content

  tags = {
    Application = "MediasAPI"
  }
}

resource "aws_ssm_parameter" "jwt_private_key" {
  name        = "/mediasapi/JWT_PRIVATE_KEY_CONTENT"
  description = "JWT private key for MediasAPI"
  type        = "SecureString"
  value       = var.jwt_private_key_content

  tags = {
    Application = "MediasAPI"
  }
}

# =============================================================================
# CloudWatch Log Group
# =============================================================================

resource "aws_cloudwatch_log_group" "mediasapi" {
  name              = "/ecs/mediasapi"
  retention_in_days = 14

  tags = {
    Application = "MediasAPI"
  }
}

# =============================================================================
# RDS MySQL Instance
# =============================================================================

resource "aws_db_subnet_group" "mysql" {
  name       = "mediasapi-db-subnet-group"
  subnet_ids = data.aws_subnets.default.ids

  tags = {
    Name = "mediasapi-db-subnet-group"
  }
}

resource "aws_db_instance" "mysql" {
  identifier = "mediasapi-mysql"

  engine         = "mysql"
  engine_version = "8.0"
  instance_class = "db.t3.micro"

  allocated_storage     = 20
  max_allocated_storage = 100
  storage_type          = "gp2"
  storage_encrypted     = true

  db_name  = var.db_name
  username = var.db_username
  password = var.db_password

  db_subnet_group_name   = aws_db_subnet_group.mysql.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  publicly_accessible    = false

  backup_retention_period = 1
  backup_window           = "03:00-04:00"
  maintenance_window      = "Mon:04:00-Mon:05:00"

  skip_final_snapshot       = true
  delete_automated_backups  = true
  deletion_protection       = false
  apply_immediately         = true

  tags = {
    Name        = "mediasapi-mysql"
    Application = "MediasAPI"
  }
}

# =============================================================================
# EC2 Instance
# =============================================================================

resource "aws_instance" "mediasapi" {
  ami                    = data.aws_ami.amazon_linux_2023.id
  instance_type          = "t3.micro"
  key_name               = var.ec2_key_pair_name
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]
  iam_instance_profile   = aws_iam_instance_profile.ec2_profile.name

  user_data = file("${path.module}/user-data.sh")

  root_block_device {
    volume_size = 20
    volume_type = "gp3"
    encrypted   = true
  }

  tags = {
    Name        = "mediasapi-server"
    Application = "MediasAPI"
  }

  depends_on = [aws_db_instance.mysql]
}

# =============================================================================
# Elastic IP (optional, for stable public IP)
# =============================================================================

resource "aws_eip" "mediasapi" {
  instance = aws_instance.mediasapi.id
  domain   = "vpc"

  tags = {
    Name = "mediasapi-eip"
  }
}
