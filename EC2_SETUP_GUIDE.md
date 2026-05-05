# EC2 Deployment Guide for BankApp

This guide explains how to set up your Amazon Linux EC2 instance for deploying the BankApp using Jenkins.

## Prerequisites

- Amazon Linux EC2 instance (t2.micro or larger)
- Security group allows SSH (port 22)
- Security group allows HTTP (port 80) and HTTPS (port 443)
- Application port 8080 accessible (either internal or through load balancer)

## Step 1: SSH into Your EC2 Instance

```bash
ssh -i your-key.pem ec2-user@your-instance-ip
```

## Step 2: Install Java on Amazon Linux

```bash
# Update system packages
sudo yum update -y

# Install Java 17 (required for the application)
sudo yum install java-17-amazon-corretto -y

# Verify installation
java -version
```

## Step 3: Create Application Directory and User

```bash
# Create deployment directory
sudo mkdir -p /home/ec2-user/bankapp
sudo chown ec2-user:ec2-user /home/ec2-user/bankapp

# Create a dedicated user for the application (optional but recommended)
sudo useradd -r -s /bin/false bankapp-user 2>/dev/null || true
```

## Step 4: Set Up Systemd Service

```bash
# Copy the service file to systemd directory
sudo cp bankapp.service /etc/systemd/system/

# Set correct permissions
sudo chmod 644 /etc/systemd/system/bankapp.service

# Reload systemd daemon
sudo systemctl daemon-reload

# Enable the service to start on boot
sudo systemctl enable bankapp

# Verify the service is recognized
sudo systemctl status bankapp
```

## Step 5: Configure EC2 Security Group

Add inbound rules to your EC2 security group:

```
Port          Protocol    Source
22 (SSH)      TCP         Your IP or Jenkins Server IP
8080 (App)    TCP         0.0.0.0/0 or Your Load Balancer
```

For production, use a load balancer and restrict port 8080 to internal traffic.

## Step 6: Configure Jenkins Credentials

In Jenkins UI, go to **Manage Credentials** and add:

1. **EC2_HOST** (Secret text)
   - Value: Your EC2 instance IP or DNS name
   - Example: `54.123.456.789` or `ec2-instance.amazonaws.com`

2. **EC2_KEY** (Secret file)
   - Upload your EC2 key pair file (e.g., `bankapp-key.pem`)
   - Or create a SSH Secret with privateKey content

## Step 7: Test Manual Deployment (Optional)

```bash
# Build the application locally
mvn clean package

# Copy JAR to EC2
scp -i your-key.pem target/bankapp-0.0.1-SNAPSHOT.jar ec2-user@your-instance-ip:/home/ec2-user/bankapp/app.jar

# SSH to EC2 and start the service
ssh -i your-key.pem ec2-user@your-instance-ip
sudo systemctl start bankapp
sudo systemctl status bankapp

# Check logs
sudo journalctl -u bankapp -f
```

## Step 8: Verify Application is Running

```bash
# Check if application started
curl http://localhost:8080/login

# Check service status
sudo systemctl status bankapp

# View logs
sudo journalctl -u bankapp -n 50

# View live logs
sudo journalctl -u bankapp -f
```

## Troubleshooting

### Application won't start
```bash
# Check service status and logs
sudo systemctl status bankapp
sudo journalctl -u bankapp -n 100
```

### Port 8080 already in use
```bash
# Find process using port 8080
sudo lsof -i :8080

# Stop the process
sudo kill -9 <PID>
```

### Java not found
```bash
# Check Java installation
java -version

# If not found, install again
sudo yum install java-17-amazon-corretto -y
```

### SSH connection issues
```bash
# Ensure EC2 security group allows SSH from Jenkins server
# Ensure key permissions are correct (400 or 600)
chmod 400 your-key.pem

# Test SSH connection
ssh -i your-key.pem -v ec2-user@your-instance-ip
```

## Production Recommendations

1. **Use Application Load Balancer (ALB)** - Route traffic to port 8080
2. **Enable HTTPS/TLS** - Use AWS Certificate Manager
3. **Auto-scaling** - Configure auto-scaling groups for multiple instances
4. **Monitoring** - Set up CloudWatch alarms for CPU, memory, disk
5. **Backups** - Enable EBS snapshots for automatic backups
6. **Logs** - Configure CloudWatch Logs agent
7. **Security** - Use IAM roles for EC2 instances
8. **Database** - For production, use managed database (RDS) instead of H2
9. **Caching** - Consider ElastiCache for Redis/Memcached

## Manual Commands Reference

```bash
# Start application
sudo systemctl start bankapp

# Stop application
sudo systemctl stop bankapp

# Restart application
sudo systemctl restart bankapp

# Check status
sudo systemctl status bankapp

# View logs (last 50 lines)
sudo journalctl -u bankapp -n 50

# View live logs
sudo journalctl -u bankapp -f

# Enable on boot
sudo systemctl enable bankapp

# Disable on boot
sudo systemctl disable bankapp
```

## Application Properties

The application uses H2 in-memory database, so:
- **No external database required**
- Data is lost on application restart
- For persistent storage, modify `application.properties` to use file-based H2 or external database

## Access Application

Once deployed and running:
- **URL**: `http://your-instance-ip:8080`
- **Login**: Use credentials created in the application
- **H2 Console** (if needed): `http://your-instance-ip:8080/h2-console`

---

For more information, check the main README.md in your project root.

