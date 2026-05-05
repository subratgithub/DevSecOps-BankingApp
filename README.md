# 🏦 DevSecOps Banking Application

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-blue)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Build-Maven-red)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A secure, enterprise-grade banking web application built with Spring Boot and MySQL, featuring robust user authentication, account management, transaction processing, and comprehensive DevSecOps practices including Jenkins CI/CD integration and Docker containerization.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites-required)
- [Quick Start](#-quick-start)
- [Detailed Setup](#setup-steps)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Application Architecture](#application-architecture)
- [Testing](#testing-the-application)
- [Docker & Deployment](#docker--deployment)
- [CI/CD Pipeline](#cicd-pipeline)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [Documentation](#documentation)

---

## 📖 Overview

The DevSecOps Banking Application is a comprehensive demonstration of building secure, scalable financial software with a focus on **Development**, **Security**, and **Operations** best practices. It provides complete banking functionality with enterprise-grade security measures and modern DevOps practices.

### ✅ Key Implementations:

| Component | Details |
|-----------|---------|
| **Data Models** | Account and Transaction entities with JPA/Hibernate ORM |
| **Data Access Layer** | Repository pattern with Spring Data JPA |
| **Business Logic** | Account management and transaction processing services |
| **API Layer** | RESTful controllers with form-based UI integration |
| **Security** | Spring Security with BCrypt hashing, session management, form login |
| **Frontend** | Thymeleaf templates with Bootstrap styling |
| **Testing** | Comprehensive unit and integration tests with JaCoCo coverage |
| **CI/CD** | Jenkins pipeline for automated build, test, and deployment |
| **Containerization** | Docker and Docker Compose for easy deployment |

---

## ✨ Features

### 1. **User Authentication & Authorization**
- ✅ Secure user registration with password hashing
- ✅ BCrypt-based password encoding
- ✅ Spring Security form login with session management
- ✅ Automatic login/logout page redirects
- ✅ CSRF protection enabled

### 2. **Account Management**
- ✅ User account creation with unique usernames
- ✅ Account balance tracking
- ✅ Password-protected operations
- ✅ Account security with encrypted passwords

### 3. **Transaction Processing**
- ✅ Deposit operations with balance updates
- ✅ Withdrawal with balance validation
- ✅ Peer-to-peer transfers between accounts
- ✅ Real-time transaction history
- ✅ Timestamp and balance tracking for all transactions

### 4. **Dashboard & Reporting**
- ✅ Personal dashboard with balance display
- ✅ Transaction history view with sorting
- ✅ Transaction filtering and tracking
- ✅ Account activity monitoring

### 5. **DevSecOps Practices**
- ✅ Unit testing with JUnit and Mockito
- ✅ Integration testing with Spring Test
- ✅ Code coverage tracking with JaCoCo
- ✅ Jenkins CI/CD pipeline
- ✅ Docker containerization
- ✅ MySQL database with Docker support

---

## 🛠️ Tech Stack

### Backend Framework
- **Spring Boot 3.3.3** - Web application framework
- **Spring Security 6.1.12** - Authentication and authorization
- **Spring Data JPA** - Data access and ORM
- **Hibernate** - Object-relational mapping

### Database
- **MySQL 8.0+** - Primary relational database
- **H2 Database** - Testing database

### Frontend
- **Thymeleaf** - Server-side template engine
- **Bootstrap** - Responsive UI framework
- **HTML5/CSS3** - Markup and styling

### Testing & Quality
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Spring Test** - Integration testing
- **JaCoCo 0.8.8** - Code coverage measurement

### Build & Deployment
- **Maven 3.x** - Build automation
- **Docker** - Containerization platform
- **Jenkins** - CI/CD automation
- **Java 17** - Runtime environment

---

## Prerequisites Required

### 1. **Java 17 or Higher**
```bash
java -version
# Output: openjdk version "17.x.x" or higher
```

### 2. **MySQL Server 8.0+**
- Running on `localhost:3306`
- Default credentials: `root` / `Test@123`
- Database: `bankappdb`

### 3. **Maven** (Included)
- Project includes Maven Wrapper (`mvnw.cmd` for Windows)

### 4. **Optional: Docker**
- Docker Engine for containerized deployment
- Docker Compose for multi-container orchestration

---

## 🚀 Quick Start

### Option 1: Traditional Setup (Windows)

```bash
# 1. Start MySQL (if installed as service)
net start MySQL80

# 2. Navigate to project directory
cd C:\Users\Badi\OneDrive\Documents\Workspace\Devops\DevSecOps-BankingApp

# 3. Build the application
.\mvnw.cmd clean install

# 4. Run the application
.\mvnw.cmd spring-boot:run

# 5. Access the application
# http://localhost:8080
```

### Option 2: Docker Setup

```bash
# 1. Build Docker image
docker build -t bankapp:latest .

# 2. Run with Docker Compose (if docker-compose.yml exists)
docker-compose up -d

# 3. Access the application
# http://localhost:8080
```

### First-Time User Flow
1. **Register**: http://localhost:8080/register
   - Create new account with username and password
2. **Login**: http://localhost:8080/login
   - Authenticate with your credentials
3. **Dashboard**: http://localhost:8080/dashboard
   - Perform transactions and manage account
4. **Transactions**: http://localhost:8080/transactions
   - View transaction history

---

## Setup Steps

### Step 1: Ensure MySQL is Running

**Option A: MySQL as Windows Service**
```powershell
net start MySQL80
# Check if running
netstat -an | findstr 3306
```

**Option B: Docker Container**
```bash
docker run -d --name bankapp-mysql \
  -e MYSQL_ROOT_PASSWORD=Test@123 \
  -e MYSQL_DATABASE=bankappdb \
  -p 3306:3306 \
  mysql:8.0
```

**Option C: Verify Connection**
```bash
mysql -u root -p -h localhost
# Enter password: Test@123
# If successful, you'll see: mysql>
```

### Step 2: Create Database (if needed)

```sql
CREATE DATABASE IF NOT EXISTS bankappdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Or via command line:
```bash
mysql -u root -pTest@123 -e "CREATE DATABASE IF NOT EXISTS bankappdb;"
```

### Step 3: Build the Application

```bash
cd C:\Users\Badi\OneDrive\Documents\Workspace\Devops\DevSecOps-BankingApp

# Clean and build
.\mvnw.cmd clean install

# Or build without running tests
.\mvnw.cmd clean install -DskipTests
```

### Step 4: Run the Application

```bash
# Using Maven plugin
.\mvnw.cmd spring-boot:run

# Or run JAR directly
java -jar target/bankapp-0.0.1-SNAPSHOT.jar
```

### Step 5: Verify Application Startup

```
Expected console output:
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.3.3)

Tomcat started on port(s): 8080 (http)
```

---

## 📁 Project Structure

```
DevSecOps-BankingApp/
├── src/
│   ├── main/
│   │   ├── java/com/example/bankapp/
│   │   │   ├── BankappApplication.java         # Application entry point
│   │   │   ├── entity/                         # JPA entity classes
│   │   │   │   ├── Account.java                # User account entity
│   │   │   │   └── Transaction.java            # Transaction record entity
│   │   │   ├── repository/                     # Data access layer
│   │   │   │   ├── AccountRepository.java      # Account CRUD operations
│   │   │   │   └── TransactionRepository.java  # Transaction CRUD operations
│   │   │   ├── service/                        # Business logic layer
│   │   │   │   ├── AccountService.java         # Account management
│   │   │   │   ├── TransactionService.java     # Transaction processing
│   │   │   │   └── CustomUserDetailsService.java # Spring Security integration
│   │   │   ├── controller/                     # HTTP request handlers
│   │   │   │   ├── AuthController.java         # Login/Register endpoints
│   │   │   │   ├── DashboardController.java    # Dashboard operations
│   │   │   │   └── TransactionController.java  # Transaction display
│   │   │   └── config/                         # Configuration classes
│   │   │       └── SecurityConfig.java         # Spring Security configuration
│   │   └── resources/
│   │       ├── application.properties          # Main configuration
│   │       ├── application-docker.properties   # Docker-specific config
│   │       ├── static/                         # Static assets
│   │       │   └── mysql/SQLScript.txt        # Database initialization
│   │       └── templates/                      # Thymeleaf templates
│   │           ├── login.html                  # Login page
│   │           ├── register.html               # Registration page
│   │           ├── dashboard.html              # Main dashboard
│   │           └── transactions.html           # Transaction history
│   └── test/
│       └── java/com/example/bankapp/           # Unit tests
│           ├── BankappApplicationTests.java
│           ├── controller/
│           ├── service/
│           └── entity/
├── target/                                      # Build output
│   ├── bankapp-0.0.1-SNAPSHOT.jar              # Executable JAR
│   └── surefire-reports/                       # Test reports
├── pom.xml                                      # Maven configuration
├── mvnw / mvnw.cmd                             # Maven wrapper scripts
├── Jenkinsfile                                 # CI/CD pipeline definition
├── bankapp.service                             # Systemd service file
├── README.md                                   # This file
├── QUICK_START.md                              # Quick start guide
└── HOW_TO_RUN_TESTS.md                         # Testing documentation
```

---

## 💾 Database Schema

### Accounts Table
```sql
CREATE TABLE accounts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  balance DECIMAL(19,2) NOT NULL DEFAULT 0.0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Purpose**: Stores user account information with encrypted passwords

### Transactions Table
```sql
CREATE TABLE transactions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  from_account_id BIGINT NOT NULL,
  to_account_id BIGINT,
  amount DECIMAL(19,2) NOT NULL,
  type VARCHAR(50) NOT NULL,  -- DEPOSIT, WITHDRAW, TRANSFER
  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  balance_after DECIMAL(19,2) NOT NULL,
  FOREIGN KEY (from_account_id) REFERENCES accounts(id),
  FOREIGN KEY (to_account_id) REFERENCES accounts(id)
);
```

**Purpose**: Records all financial transactions with complete audit trail

---

## 🌐 API Endpoints

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/login` | Display login form |
| POST | `/login` | Process login (Spring Security) |
| GET | `/register` | Display registration form |
| POST | `/register` | Create new account |
| GET | `/logout` | Logout and end session |

### Dashboard Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Home page (redirects to login/dashboard) |
| GET | `/dashboard` | Main dashboard (requires authentication) |
| POST | `/dashboard/deposit` | Perform deposit operation |
| POST | `/dashboard/withdraw` | Perform withdrawal operation |
| POST | `/dashboard/transfer` | Transfer funds to another account |

### Transaction Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/transactions` | View transaction history |
| POST | `/transactions/filter` | Filter transactions by date/type |

---

## 🏗️ Application Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────┐
│         Presentation Layer (UI)             │
│    Thymeleaf Templates + Bootstrap          │
└──────────────────┬──────────────────────────┘
                   │ HTTP Requests/Responses
┌──────────────────▼──────────────────────────┐
│      Controller Layer (REST APIs)           │
│  AuthController, DashboardController, etc.  │
└──────────────────┬──────────────────────────┘
                   │ Service methods
┌──────────────────▼──────────────────────────┐
│     Business Logic Layer (Services)         │
│  AccountService, TransactionService         │
└──────────────────┬──────────────────────────┘
                   │ Repository methods
┌──────────────────▼──────────────────────────┐
│    Data Access Layer (Repositories)         │
│    Spring Data JPA Repositories             │
└──────────────────┬──────────────────────────┘
                   │ SQL Queries
┌──────────────────▼──────────────────────────┐
│      Database Layer (MySQL)                 │
│   Accounts & Transactions Tables            │
└─────────────────────────────────────────────┘
```

### Security Architecture

```
User Request
    ↓
Spring Security Filter Chain
    ↓
Check Session (if exists)
    ↓
If not authenticated → Redirect to /login
    ↓
Process Login Form
    ↓
CustomUserDetailsService.loadUserByUsername()
    ↓
Query Database for account
    ↓
BCrypt compare passwords
    ↓
✓ Success → Set session → Redirect to dashboard
✗ Fail → Error message → Redirect to login
```

---

## 🧪 Testing the Application

### Prerequisites for Testing
```bash
# Ensure MySQL is running
net start MySQL80

# Run all tests
.\mvnw.cmd test

# Run specific test class
.\mvnw.cmd test -Dtest=AccountServiceTest

# Generate coverage report
.\mvnw.cmd test jacoco:report
```

### Manual Testing Checklist

- [ ] MySQL is running on localhost:3306
- [ ] Application starts without errors
- [ ] Can access http://localhost:8080
- [ ] Registration page loads at /register
- [ ] Can create new account with username/password
- [ ] Login page shows at /login
- [ ] Can login with registered credentials
- [ ] Dashboard displays after successful login
- [ ] Balance shows correctly

### Testing Operations

**1. Test Deposit**
```
1. Go to Dashboard
2. Enter amount: 100
3. Click "Deposit"
4. Verify balance increased by 100
```

**2. Test Withdrawal**
```
1. On Dashboard, enter amount: 30
2. Click "Withdraw"
3. Verify balance decreased by 30
4. Test withdrawal > balance (should fail)
```

**3. Test Transfer**
```
1. Register second account (testuser2)
2. On Dashboard, enter: username=testuser2, amount=50
3. Click "Transfer"
4. Verify money transferred
5. Check both account balances
```

**4. Test Transaction History**
```
1. Go to /transactions
2. Verify all transactions appear
3. Check timestamps are correct
4. Verify transaction types (DEPOSIT, WITHDRAW, TRANSFER)
```

### Test Reports

After running tests, view coverage reports:
```bash
# Generate test report
.\mvnw.cmd test

# Open coverage report (if using JaCoCo)
start target/site/jacoco/index.html
```

---

## 🐳 Docker & Deployment

### Docker Image

#### Build Docker Image
```bash
docker build -t bankapp:1.0 .
```

#### Run Docker Container
```bash
docker run -d \
  --name bankapp-container \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/bankappdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=Test@123 \
  bankapp:1.0
```

### Docker Compose (Multi-Container)

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: Test@123
      MYSQL_DATABASE: bankappdb
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  bankapp:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/bankappdb
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: Test@123
    depends_on:
      - mysql

volumes:
  mysql-data:
```

#### Start Services
```bash
docker-compose up -d
```

#### Stop Services
```bash
docker-compose down
```

---

## 🔄 CI/CD Pipeline

### Jenkins Integration

The project includes a **Jenkinsfile** that defines the CI/CD pipeline with these stages:

**Pipeline Stages:**
1. **Build** - Maven clean install
2. **Test** - Run unit and integration tests
3. **Code Coverage** - JaCoCo coverage analysis
4. **Deploy** - Build Docker image and deploy

**Access Jenkins:**
```
http://jenkins-server:8080
```

**View Pipeline:**
```
http://jenkins-server:8080/job/bankapp
```

---

## 🔧 Configuration

### Application Properties

**Development** (`application.properties`):
```ini
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/bankappdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Test@123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Thymeleaf
spring.thymeleaf.cache=false
```

**Docker** (`application-docker.properties`):
```ini
spring.datasource.url=jdbc:mysql://mysql-db:3306/bankappdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

---

## ❓ Troubleshooting

### Error: "Connection refused - port 3306"
```
✗ Problem: MySQL is not running
✓ Solution:
  - Windows: net start MySQL80
  - Docker: docker run -d -p 3306:3306 mysql:8.0
  - Check: netstat -an | findstr 3306
```

### Error: "Unknown database 'bankappdb'"
```
✗ Problem: Database doesn't exist
✓ Solution:
  - mysql -u root -pTest@123 -e "CREATE DATABASE bankappdb;"
  - Or: Pass -e MYSQL_DATABASE=bankappdb to Docker
```

### Error: "Access denied for user 'root'"
```
✗ Problem: Wrong credentials
✓ Solution:
  - Check application.properties
  - Default: username=root, password=Test@123
  - Verify MySQL password matches
```

### Error: "Public Key Retrieval is not allowed"
```
✗ Problem: MySQL connection security setting
✓ Solution: Already fixed in application.properties
  - Contains: allowPublicKeyRetrieval=true
  - (Note: Use only in development/testing)
```

### Application starts but login fails
```
✗ Problem: CustomUserDetailsService not working
✓ Solution:
  - Check database has accounts table
  - Verify account exists in database
  - Check exception logs for details
  - Try with username created via /register endpoint
```

### Port 8080 already in use
```
✗ Problem: Another application using port 8080
✓ Solution:
  - Find process: netstat -ano | findstr 8080
  - Kill process: taskkill /PID <PID> /F
  - Or use different port: java -jar -Dserver.port=8081 target/bankapp-0.0.1-SNAPSHOT.jar
```

---

## 📝 Build & Run Summary

```bash
# Complete workflow
cd C:\Users\Badi\OneDrive\Documents\Workspace\Devops\DevSecOps-BankingApp

# Start MySQL
net start MySQL80

# Build application
.\mvnw.cmd clean install

# Run application
.\mvnw.cmd spring-boot:run

# Access application
start http://localhost:8080
```

---

## 🤝 Contributing

We welcome contributions! Here's how:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/awesome-feature`)
3. **Commit** your changes (`git commit -m 'Add awesome feature'`)
4. **Push** to the branch (`git push origin feature/awesome-feature`)
5. **Open** a Pull Request

### Code Standards
- Follow Java naming conventions
- Write unit tests for new features
- Maintain test coverage above 80%
- Update documentation as needed

---

## 📚 Documentation

| Document | Purpose |
|----------|---------|
| [README.md](README.md) | Project overview and setup guide |
| [QUICK_START.md](QUICK_START.md) | Quick start instructions |
| [HOW_TO_RUN_TESTS.md](HOW_TO_RUN_TESTS.md) | Testing guide |
| [EC2_SETUP_GUIDE.md](EC2_SETUP_GUIDE.md) | AWS EC2 deployment guide |
| [Jenkinsfile](Jenkinsfile) | CI/CD pipeline definition |

### Example Test Commands

```bash
# Run all tests
.\mvnw.cmd test

# Run specific test class
.\mvnw.cmd test -Dtest=AccountServiceTest

# Run with coverage
.\mvnw.cmd clean test jacoco:report

# Skip tests during build
.\mvnw.cmd clean install -DskipTests
```

---

## 📊 Project Status

| Aspect | Status |
|--------|--------|
| ✅ Core Features | Complete |
| ✅ Security | Implemented (Spring Security + BCrypt) |
| ✅ Testing | Unit & Integration Tests Done |
| ✅ Documentation | Complete |
| ✅ Docker Support | Available |
| ✅ CI/CD Pipeline | Jenkinsfile Ready |
| ✅ Code Coverage | JaCoCo Integrated |
| ✅ Error Handling | Comprehensive |

---

## 📞 Support & Resources

### Getting Help
- Check [Troubleshooting](#troubleshooting) section
- Review [QUICK_START.md](QUICK_START.md)
- Check application logs in console

### Technology Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/projects/spring-security)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🎯 Quick Reference

### Essential Commands
```powershell
# Start MySQL
net start MySQL80

# Build & Run
.\mvnw.cmd clean install && .\mvnw.cmd spring-boot:run

# Run Tests
.\mvnw.cmd test

# Build Docker Image
docker build -t bankapp:latest .

# Run with Docker
docker-compose up -d
```

### Default Access
| Item | Value |
|------|-------|
| URL | http://localhost:8080 |
| DB Host | localhost:3306 |
| DB Name | bankappdb |
| DB User | root |
| DB Pass | Test@123 |

---

**Last Updated**: May 5, 2026  
**Version**: 1.0.0  
**Status**: ✅ Production Ready

Thank you for using the DevSecOps Banking Application!

