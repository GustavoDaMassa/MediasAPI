#!/bin/bash
# =============================================================================
# MediasAPI EC2 User Data Script
# Amazon Linux 2023
# =============================================================================

set -e

# Update system packages
dnf update -y

# Install Docker
dnf install -y docker
systemctl enable docker
systemctl start docker

# Add ec2-user to docker group
usermod -aG docker ec2-user

# Install Docker Compose v2 (as Docker plugin)
mkdir -p /usr/local/lib/docker/cli-plugins
curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# Create application directory
mkdir -p /opt/mediasapi/nginx/certs
chown -R ec2-user:ec2-user /opt/mediasapi

# Install AWS CLI (for SSM parameter retrieval)
dnf install -y awscli

# Install jq for JSON parsing
dnf install -y jq

# Create a script to fetch SSM parameters and create .env file
cat > /opt/mediasapi/fetch-secrets.sh << 'SCRIPT'
#!/bin/bash
# Fetch secrets from SSM Parameter Store and create .env file

REGION=$(curl -s http://169.254.169.254/latest/meta-data/placement/region)

# Fetch parameters
DATABASE_URL=$(aws ssm get-parameter --name "/mediasapi/DATABASE_URL" --with-decryption --region $REGION --query "Parameter.Value" --output text)
DATABASE_USERNAME=$(aws ssm get-parameter --name "/mediasapi/DATABASE_USERNAME" --with-decryption --region $REGION --query "Parameter.Value" --output text)
DATABASE_PASSWORD=$(aws ssm get-parameter --name "/mediasapi/DATABASE_PASSWORD" --with-decryption --region $REGION --query "Parameter.Value" --output text)
JWT_PUBLIC_KEY_CONTENT=$(aws ssm get-parameter --name "/mediasapi/JWT_PUBLIC_KEY_CONTENT" --with-decryption --region $REGION --query "Parameter.Value" --output text)
JWT_PRIVATE_KEY_CONTENT=$(aws ssm get-parameter --name "/mediasapi/JWT_PRIVATE_KEY_CONTENT" --with-decryption --region $REGION --query "Parameter.Value" --output text)

# Create .env file
cat > /opt/mediasapi/.env << EOF
DATABASE_URL=${DATABASE_URL}
DATABASE_USERNAME=${DATABASE_USERNAME}
DATABASE_PASSWORD=${DATABASE_PASSWORD}
JWT_PUBLIC_KEY_CONTENT=${JWT_PUBLIC_KEY_CONTENT}
JWT_PRIVATE_KEY_CONTENT=${JWT_PRIVATE_KEY_CONTENT}
AWS_REGION=${REGION}
IMAGE_TAG=latest
EOF

echo "Secrets fetched and .env created successfully"
SCRIPT

chmod +x /opt/mediasapi/fetch-secrets.sh
chown ec2-user:ec2-user /opt/mediasapi/fetch-secrets.sh

# Generate self-signed SSL certificate for Nginx (temporary, replace with Let's Encrypt in production)
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /opt/mediasapi/nginx/certs/nginx.key \
  -out /opt/mediasapi/nginx/certs/nginx.crt \
  -subj "/C=BR/ST=SP/L=SaoPaulo/O=MediasAPI/CN=localhost"

chown -R ec2-user:ec2-user /opt/mediasapi/nginx/certs

# Log completion
echo "EC2 user-data script completed at $(date)" >> /var/log/user-data.log
