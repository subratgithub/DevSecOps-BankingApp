# Goldencat Bank Application - Setup & Access Guide

## Project Completion Summary

I have successfully implemented the complete backend for your banking application with the following components:

### ✅ Implemented Features:

1. **Entity Classes** (Database Models)
   - `Account`: User accounts with username, password, and balance
   - `Transaction`: Transaction history (deposits, withdrawals, transfers)

2. **Repository Interfaces** (Data Access)
   - `AccountRepository`: JPA repository for account operations
   - `TransactionRepository`: JPA repository for transaction history

3. **Service Classes** (Business Logic)
   - `AccountService`: Handle account registration, authentication, and management
   - `TransactionService`: Handle deposits, withdrawals, and transfers

4. **Controller Classes** (Request Handlers)
   - `AuthController`: Handle login, registration, and logout endpoints
   - `DashboardController`: Handle dashboard and transaction operations
   - `TransactionController`: Display transaction history

5. **Security Configuration**
   - Spring Security configured for authentication and authorization
   - Password hashing using BCrypt
   - Login/logout flow

---

## Prerequisites Required

### 1. Java 17+
```bash
java -version
```

### 2. MySQL Server (v8.0+)
- Must be running on `localhost:3306`
- Credentials configured: `root` / `Test@123`

---

## Setup Steps

### Step 1: Create MySQL Database

Run this SQL command in your MySQL client:

```sql
CREATE DATABASE IF NOT EXISTS bankappdb;
```

Or using MySQL command line:

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS bankappdb;"
```

### Step 2: Start MySQL Server

**Windows (MySQL installed as service):**
```bash
net start MySQL80  # or your MySQL service name
```

**Or if using Docker:**
```bash
docker run -d --name mysql-container \
  -e MYSQL_ROOT_PASSWORD=Test@123 \
  -e MYSQL_DATABASE=bankappdb \
  -p 3306:3306 \
  mysql:8.0
```

### Step 3: Build the Application

Navigate to the project directory:

```bash
cd "C:\Users\Badi\Downloads\Multi-Tier-With-Database-start\Multi-Tier-With-Database-start"
```

Build using Maven:

```bash
.\mvnw.cmd clean install
```

### Step 4: Run the Application

```bash
.\mvnw.cmd spring-boot:run
```

Or after building, run:

```bash
java -jar target/bankapp-0.0.1-SNAPSHOT.jar
```

---

## Accessing the Application

Once the application is running, open your browser and navigate to:

### **Main URL: http://localhost:8080**

### Available Pages:

| Page | URL | Description |
|------|-----|-------------|
| **Login** | http://localhost:8080/login | Sign in with your account |
| **Register** | http://localhost:8080/register | Create a new account |
| **Dashboard** | http://localhost:8080/dashboard | Main dashboard (after login) |
| **Transactions** | http://localhost:8080/transactions | View transaction history |
| **Logout** | http://localhost:8080/logout | Sign out |

---

## Features Available

### 1. **User Registration**
- Create new accounts with username and password
- Passwords are securely hashed using BCrypt

### 2. **User Login**
- Authenticate with credentials
- Session management with Spring Security

### 3. **Dashboard Operations**
- **View Balance**: See current account balance
- **Deposit**: Add money to your account
- **Withdraw**: Remove money from your account (if sufficient balance)
- **Transfer**: Send money to another user by their username

### 4. **Transaction History**
- View all transactions (deposits, withdrawals, transfers)
- Sorted by date (newest first)
- Shows transaction type and amount

---

## Database Schema

### Accounts Table
```
accounts
├──id (PK, AUTO_INCREMENT)
├── username (UNIQUE, NOT NULL)
├── password (NOT NULL, encrypted)
└── balance (NOT NULL, DEFAULT: 0.0)
```

### Transactions Table
```
transactions
├── id (PK, AUTO_INCREMENT)
├── fromAccountId (FK to accounts.id)
├── toAccountId (FK to accounts.id, nullable)
├── amount (NOT NULL)
├── type (NOT NULL: DEPOSIT, WITHDRAW, TRANSFER)
├── timestamp (NOT NULL)
└── balanceAfter (NOT NULL)
```

---

## Troubleshooting

### Error: "Connection refused"
**Solution**: Ensure MySQL is running on port 3306

```bash
# Check if MySQL is running
netstat -an | findstr 3306
```

### Error: "Public Key Retrieval is not allowed"
**Status**: ✅ Already fixed in `application.properties`

### Error: "Unknown database 'bankappdb'"
**Solution**: Create the database first (see Setup Step 1)

### Error: "Access denied for user 'root'"
**Solution**: Verify credentials in `application.properties`:
- Username: `root`
- Password: `Test@123`

---

## Configuration Files

### application.properties
```ini
spring.datasource.url=jdbc:mysql://localhost:3306/bankappdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Test@123
spring.jpa.hibernate.ddl-auto=update
```

---

## Testing the Application

### Test User Registration:
1. Go to http://localhost:8080/register
2. Enter username: `testuser`
3. Enter password: `Test@123`
4. Click Register

### Test Login:
1. Go to http://localhost:8080/login
2. Enter the same credentials
3. You'll be redirected to the dashboard

### Test Balance Operations:
1. On the dashboard, use:
   - **Deposit**: Add $100
   - **Withdraw**: Remove $20
   - **Transfer**: Send $10 to another user

---

## Technology Stack

- **Backend**: Spring Boot 3.3.3
- **Security**: Spring Security 6.1.12
- **Database**: MySQL 8.0 with JPA/Hibernate
- **Build Tool**: Maven
- **Java Version**: 17
- **Frontend**: Thymeleaf (HTML templates)

---

## Project Structure

```
Multi-Tier-With-Database-start/
├── src/main/java/com/example/bankapp/
│   ├── BankappApplication.java (Entry point)
│   ├── entity/
│   │   ├── Account.java
│   │   └── Transaction.java
│   ├── repository/
│   │   ├── AccountRepository.java
│   │   └── TransactionRepository.java
│   ├── service/
│   │   ├── AccountService.java
│   │   └── TransactionService.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── DashboardController.java
│   │   └── TransactionController.java
│   └── config/
│       └── SecurityConfig.java
├── src/main/resources/
│   ├── application.properties
│   └── templates/
│       ├── login.html
│       ├── register.html
│       ├── dashboard.html
│       └── transactions.html
└── pom.xml
```

---

## Build & Run Summary

```bash
# Build
.\mvnw.cmd clean install

# Run
.\mvnw.cmd spring-boot:run

# Access
http://localhost:8080
```

---

## Notes

- The application uses `spring.jpa.hibernate.ddl-auto=update`, which automatically creates tables on startup
- Passwords are hashed using BCrypt for security
- All transactions are logged with timestamp and balance information
- The application redirects unauthenticated users to the login page

---

**Status**: ✅ Application is ready to use!
Just start MySQL and run the application.

