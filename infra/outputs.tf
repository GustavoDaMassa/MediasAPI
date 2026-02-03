# =============================================================================
# MediasAPI Terraform Outputs
# =============================================================================

output "ec2_public_ip" {
  description = "Public IP address of the EC2 instance (Elastic IP)"
  value       = aws_eip.mediasapi.public_ip
}

output "ec2_instance_id" {
  description = "ID of the EC2 instance"
  value       = aws_instance.mediasapi.id
}

output "rds_endpoint" {
  description = "Endpoint of the RDS MySQL instance"
  value       = aws_db_instance.mysql.endpoint
}

output "rds_address" {
  description = "Address of the RDS MySQL instance (without port)"
  value       = aws_db_instance.mysql.address
}

output "ssh_command" {
  description = "SSH command to connect to the EC2 instance"
  value       = "ssh -i ${var.ec2_key_pair_name}.pem ec2-user@${aws_eip.mediasapi.public_ip}"
}

output "database_url" {
  description = "JDBC connection URL for the application"
  value       = "jdbc:mysql://${aws_db_instance.mysql.endpoint}/${var.db_name}"
  sensitive   = true
}

output "cloudwatch_log_group" {
  description = "CloudWatch Log Group name"
  value       = aws_cloudwatch_log_group.mediasapi.name
}

output "application_url" {
  description = "URL to access the application"
  value       = "https://${aws_eip.mediasapi.public_ip}"
}
