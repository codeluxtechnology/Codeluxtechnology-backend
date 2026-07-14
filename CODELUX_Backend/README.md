# CODELUX Backend

This repository contains the Spring Boot backend API for the **CODELUX Technology** website/web application.

## Technologies Used

- **Java 17+**
- **Spring Boot** (Spring MVC, Spring Security, Spring Data JPA)
- **Maven** (Dependency & build management)
- **MySQL** (Database)
- **Hibernate / JPA** (ORM framework)
- **Cloudinary** (Cloud image and asset storage)

## Detailed Documentation

For base URLs, database setup, Cloudinary configuration, default credentials, and full API endpoints, please check:
👉 **[CODELUX API Guide](CODELUX_API_GUIDE.md)**

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Maven 3.8+
- MySQL database running

### Configuration

Ensure you define the environment variables required by `src/main/resources/application.properties` (e.g., database connection details, Cloudinary keys, admin credentials). Alternatively, copy them to a local configuration or set them in your running environment.

### Run Locally

Using Maven:
```bash
./mvnw spring-boot:run
```
