#!/bin/bash
# =============================================================================
# MediasAPI Deploy Script
# Fetches secrets from AWS SSM Parameter Store and starts containers
# Usage: ./start.sh [IMAGE_TAG]
# =============================================================================

set -e

REGION="us-east-1"
cd /opt/mediasapi

echo "Fetching secrets from SSM Parameter Store..."

# Database connection (Spring Boot relaxed binding format)
export SPRING_DATASOURCE_URL="$(aws ssm get-parameter --name '/mediasapi/DATABASE_URL' --with-decryption --region $REGION --query 'Parameter.Value' --output text)?useSSL=false&allowPublicKeyRetrieval=true"
export SPRING_DATASOURCE_USERNAME="$(aws ssm get-parameter --name '/mediasapi/DATABASE_USERNAME' --with-decryption --region $REGION --query 'Parameter.Value' --output text)"
export SPRING_DATASOURCE_PASSWORD="$(aws ssm get-parameter --name '/mediasapi/DATABASE_PASSWORD' --with-decryption --region $REGION --query 'Parameter.Value' --output text)"

# JWT keys: remove newlines for Docker Compose YAML compatibility
export JWT_PUBLIC_KEY_CONTENT=$(aws ssm get-parameter --name '/mediasapi/JWT_PUBLIC_KEY_CONTENT' --with-decryption --region $REGION --query 'Parameter.Value' --output text | tr -d '\n')
export JWT_PRIVATE_KEY_CONTENT=$(aws ssm get-parameter --name '/mediasapi/JWT_PRIVATE_KEY_CONTENT' --with-decryption --region $REGION --query 'Parameter.Value' --output text | tr -d '\n')

export SPRING_PROFILES_ACTIVE=prod
export IMAGE_TAG=${1:-latest}

echo "Secrets loaded. Starting containers with IMAGE_TAG=$IMAGE_TAG..."
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d

# Cleanup old images
docker image prune -f

echo "Deploy complete. Container status:"
docker compose -f docker-compose.prod.yml ps
