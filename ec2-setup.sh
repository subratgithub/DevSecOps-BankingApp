#!/bin/bash

# BankApp EC2 Setup Script for Amazon Linux
# This script automates the initial setup of the EC2 instance for BankApp deployment

set -e

echo "================================"
echo "BankApp EC2 Setup Script"
echo "Amazon Linux"
echo "================================"

# Update system
echo "[1/5] Updating system packages..."
sudo yum update -y > /dev/null 2>&1

# Install Java 17
echo "[2/5] Installing Java 17..."
sudo yum install java-17-amazon-corretto -y > /dev/null 2>&1
java -version

# Create application directory
echo "[3/5] Creating application directory..."
sudo mkdir -p /home/ec2-user/bankapp
sudo chown ec2-user:ec2-user /home/ec2-user/bankapp
echo "Directory created: /home/ec2-user/bankapp"

# Copy and enable systemd service
echo "[4/5] Setting up systemd service..."
if [ -f bankapp.service ]; then
    sudo cp bankapp.service /etc/systemd/system/
    sudo chmod 644 /etc/systemd/system/bankapp.service
    sudo systemctl daemon-reload
    sudo systemctl enable bankapp
    echo "Systemd service configured"
else
    echo "WARNING: bankapp.service file not found in current directory"
    echo "Please ensure bankapp.service is copied to /etc/systemd/system/bankapp.service manually"
fi

# Verify setup
echo "[5/5] Verifying setup..."
echo "✓ Java version:"
java -version
echo ""
echo "✓ Directory permissions:"
ls -ld /home/ec2-user/bankapp
echo ""
echo "✓ Systemd service status:"
sudo systemctl status bankapp --no-pager || echo "Service not yet deployed"
echo ""

echo "================================"
echo "Setup Complete!"
echo "================================"
echo ""
echo "Next steps:"
echo "1. Ensure your Git repository is configured in Jenkins"
echo "2. Configure EC2_HOST and EC2_KEY credentials in Jenkins"
echo "3. Run your Jenkins pipeline to deploy the application"
echo "4. Monitor logs: sudo journalctl -u bankapp -f"
echo ""
echo "To manually test deployment:"
echo "  scp -i your-key.pem target/bankapp-0.0.1-SNAPSHOT.jar ec2-user@YOUR_IP:/home/ec2-user/bankapp/app.jar"
echo "  ssh -i your-key.pem ec2-user@YOUR_IP 'sudo systemctl start bankapp'"
echo "  curl http://YOUR_IP:8080/login"
echo ""

