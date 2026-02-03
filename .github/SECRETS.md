# GitHub Secrets Configuration

This document describes the GitHub Secrets required for the CI/CD pipeline.

## Required Secrets

Configure these secrets in your GitHub repository settings:
**Settings → Secrets and variables → Actions → New repository secret**

| Secret Name | Description | How to Get |
|-------------|-------------|------------|
| `DOCKER_USERNAME` | Docker Hub username | Your Docker Hub account username |
| `DOCKER_PASSWORD` | Docker Hub access token | Docker Hub → Account Settings → Security → New Access Token |
| `EC2_HOST` | Public IP of EC2 instance | Run `terraform output ec2_public_ip` |
| `EC2_SSH_KEY` | Private SSH key content | Content of the `.pem` file from EC2 key pair |

## Setting Up Each Secret

### 1. DOCKER_USERNAME

Your Docker Hub username (e.g., `gustavodamassa`).

### 2. DOCKER_PASSWORD

**Important**: Use an Access Token, not your password!

1. Go to [Docker Hub Security Settings](https://hub.docker.com/settings/security)
2. Click "New Access Token"
3. Give it a description (e.g., "GitHub Actions MediasAPI")
4. Select "Read, Write, Delete" permissions
5. Copy the token and save it as the secret

### 3. EC2_HOST

After running Terraform, get the EC2 public IP:

```bash
cd infra
terraform output ec2_public_ip
```

Example: `54.123.45.67`

### 4. EC2_SSH_KEY

The content of your EC2 key pair private key file (`.pem`).

```bash
# View your key content
cat mediasapi-key.pem
```

Copy the entire content including:
```
-----BEGIN RSA PRIVATE KEY-----
...
-----END RSA PRIVATE KEY-----
```

**Important**: Copy exactly as is, including the header and footer lines.

## Verification Checklist

Before pushing to trigger the pipeline:

- [ ] `DOCKER_USERNAME` is set
- [ ] `DOCKER_PASSWORD` is an access token (not password)
- [ ] `EC2_HOST` is the Elastic IP from Terraform
- [ ] `EC2_SSH_KEY` is the complete private key content
- [ ] EC2 instance is running and accessible
- [ ] Terraform apply completed successfully

## Troubleshooting

### Docker push fails

- Verify `DOCKER_USERNAME` is correct
- Ensure `DOCKER_PASSWORD` is an access token with write permissions
- Check if the repository `gustavodamassa/medias-api` exists on Docker Hub

### SSH connection fails

- Verify `EC2_HOST` is the correct IP
- Ensure `EC2_SSH_KEY` includes the complete key with headers
- Check EC2 security group allows SSH from GitHub Actions IPs
- Verify the EC2 instance is running

### Deploy script fails

- SSH into EC2 manually and check:
  - Docker is running: `sudo systemctl status docker`
  - `/opt/mediasapi` directory exists
  - `fetch-secrets.sh` script is executable

## Security Best Practices

1. **Never commit secrets** to the repository
2. **Rotate secrets** periodically
3. **Use minimal permissions** for tokens
4. **Restrict SSH access** to known IPs when possible
5. **Monitor GitHub Actions** logs for unauthorized access
