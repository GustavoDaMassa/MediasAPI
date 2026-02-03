# =============================================================================
# MediasAPI Terraform Variables
# =============================================================================

variable "aws_region" {
  description = "AWS region for all resources"
  type        = string
  default     = "us-east-1"
}

variable "admin_ip_cidr" {
  description = "CIDR block for SSH access (your IP address, e.g., 203.0.113.0/32)"
  type        = string
}

variable "ec2_key_pair_name" {
  description = "Name of the EC2 key pair for SSH access"
  type        = string
}

# =============================================================================
# Database Configuration
# =============================================================================

variable "db_name" {
  description = "Name of the MySQL database"
  type        = string
  default     = "mediasdb"
}

variable "db_username" {
  description = "Username for the MySQL database"
  type        = string
  default     = "mediasapi"
}

variable "db_password" {
  description = "Password for the MySQL database"
  type        = string
  sensitive   = true
}

# =============================================================================
# JWT Keys
# =============================================================================

variable "jwt_public_key_content" {
  description = "Content of the JWT public key (RSA)"
  type        = string
  sensitive   = true
}

variable "jwt_private_key_content" {
  description = "Content of the JWT private key (RSA)"
  type        = string
  sensitive   = true
}
