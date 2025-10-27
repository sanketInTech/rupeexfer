# RupeeXfer — Secure Fund Transfer API

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-brightgreen?logo=spring)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.1.0-brightgreen?logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-✓-blue?logo=docker)](https://www.docker.com/)
[![Swagger](https://img.shields.io/badge/Swagger-✓-green?logo=swagger)](https://swagger.io/)

##  Overview

RupeeXfer is a high-performance, secure fund transfer API that implements banking-grade transaction management with ACID compliance. Built with Spring Boot 3 and Java 17, it demonstrates modern backend development practices including distributed transactions, optimistic locking, and JWT-based security.

## Features

-  **Atomic Money Transfers** - ACID-compliant fund transfers with rollback support
-  **Optimistic Locking** - Prevents double-spending in high-concurrency scenarios
-  **Immutable Transaction Ledger** - Tamper-proof record of all transactions
-  **JWT Authentication** - Stateless, secure user authentication
-  **Comprehensive API Docs** - Interactive documentation with Swagger UI
-  **Docker Support** - Containerized deployment with Docker Compose
-  **Test Coverage** - Unit, integration, and concurrency tests
-  **Performance Optimized** - Connection pooling, batch processing, and caching

##  Project Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       Client Applications                   │
└───────────────────────────────┬─────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────┐
│                API Gateway (Spring Cloud Gateway)           │
└───────────────────────────────┬─────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────┐
│                      RupeeXfer API (This Service)           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                   REST Controllers                    │  │
│  └───────────────┬───────────────────────┬───────────────┘  │
│                  │                       │                  │
│  ┌───────────────▼───────┐ ┌────────────▼───────────────┐   │
│  │    Account Service    │ │    Transaction Service     │   │
│  └───────────────┬───────┘ └────────────┬───────────────┘   │
│                  │                       │                  │
│  ┌───────────────▼───────────────────────▼───────────────┐  │
│  │                    Repository Layer                   │  │
│  └───────────────────────────┬───────────────────────────┘  │
│                              │                              │
└──────────────────────────────┼──────────────────────────────┘
                               │
                 ┌─────────────┴───────────────┐
                 │                             │
    ┌────────────▼───────┐        ┌────────────▼───────┐
    │   PostgreSQL DB    │        │    H2 (for dev)    │
    └────────────────────┘        └────────────────────┘
```

##  Tech Stack

- **Core**: Java 17, Spring Boot 3.1.0
- **Security**: Spring Security 6.1.0, JWT, BCrypt
- **Database**: PostgreSQL 15, H2 (development)
- **Data Access**: Spring Data JPA, Hibernate 6.2.0
- **API Docs**: SpringDoc OpenAPI 3.0
- **Build**: Maven 3.8.6+
- **Containerization**: Docker 20.10+, Docker Compose
- **Testing**: JUnit 5, TestContainers, MockMVC

##  Quickstart

### Local Development (H2 Database)

```bash
# Clone the repository
git clone https://github.com/yourusername/rupeexfer.git
cd rupeexfer

# Run with Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Access the application at `http://localhost:8080`

### Docker with PostgreSQL

```bash
docker-compose up --build
```

### Environment Variables

| Variable           | Default (Dev)                     | Description                     |
|--------------------|----------------------------------|---------------------------------|
| `SPRING_PROFILES_ACTIVE` | `dev`                          | Active Spring profile          |
| `DB_URL`           | `jdbc:h2:mem:rupeexfer`         | Database connection URL         |
| `DB_USERNAME`      | `sa`                            | Database username              |
| `DB_PASSWORD`      | (empty)                         | Database password              |
| `JWT_SECRET`       | `your-256-bit-secret`           | JWT signing key                |
| `JWT_EXPIRATION_MS`| `86400000` (24h)                | JWT expiration time in ms      |

##  API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/auth/signin` | User authentication | No |
| `POST` | `/api/accounts` | Create new account | Yes |
| `GET` | `/api/accounts/{accountNumber}/balance` | Get balance | Yes |
| `POST` | `/api/transactions/transfer` | Transfer funds | Yes |
| `GET` | `/api/accounts/{accountNumber}/transactions` | Transaction history | Yes |

##  Example Requests

### 1. Create Account
```bash
curl -X POST 'http://localhost:8080/api/accounts' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -d '{
    "accountType": "SAVINGS",
    "initialBalance": 1000.00
  }'
```

### 2. Transfer Funds
```bash
curl -X POST 'http://localhost:8080/api/transactions/transfer' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN' \
  -d '{
    "sourceAccountNumber": "ACC123456",
    "destinationAccountNumber": "ACC789012",
    "amount": 100.00,
    "description": "Dinner bill"
  }'
```

### 3. Get Transaction History
```bash
curl 'http://localhost:8080/api/accounts/ACC123456/transactions?page=0&size=10' \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

##  Testing

Run the complete test suite:
```bash
mvn test
```

Test categories:
- Unit tests (`*Test.java`)
- Integration tests (`*IT.java`)

##  Design Choices

### Optimistic Locking
- Prevents lost updates in concurrent transfer scenarios
- Uses `@Version` for automatic conflict detection
- Throws `OptimisticLockingFailureException` on conflicts

### ACID Compliance
- All money transfers are wrapped in `@Transactional`
- Automatic rollback on any failure
- Consistent state guaranteed at all times

### Immutable Ledger
- All transactions are append-only
- No updates/deletes on transaction records
- Complete audit trail for compliance

### Idempotency
- Support for idempotency keys in transfer requests
- Prevents duplicate processing of the same request
- Essential for reliable retries

## Deployment

### Build JAR
```bash
mvn clean package
```

### Run JAR
```bash
java -jar target/rupeexfer-0.0.1-SNAPSHOT.jar
```

### Production Deployment
For production deployment, it's recommended to:
1. Use HTTPS
2. Set strong database credentials
3. Configure proper JWT secret
4. Enable proper logging and monitoring
5. Set up proper backup strategy


## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Copyright © 2025 Sanket Desai. All rights reserved.
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
