# Farm Connect Project Context

This is a Spring Boot multi-module microservices project named **Farm Connect**, designed as a marketplace for farmers and buyers.

## Project Structure

- `buyer-service`: Manages buyer profiles, registrations, and mock operations for bidding, orders, and tracking.
- `crop-service`: Manages crop categories and listings, including support for file uploads.
- `farmer-service`: Manages farmer profiles, land information, and verification status.
- `uploads`: Local storage for product images.

## Technology Stack

- **Java**: 21 (Services) / 25 (Root POM)
- **Framework**: Spring Boot 4.0.6, Spring Cloud 2025.1.1
- **Database**: MySQL (using `mysql-connector-j`)
- **Persistence**: Spring Data JPA / Hibernate
- **Mapping**: MapStruct 1.6.3
- **Utilities**: Lombok
- **API Documentation**: SpringDoc OpenAPI (Swagger) 3.0.2

## Architecture & Design Patterns

- **Microservices**: Independent services for Buyer, Crop, and Farmer management.
- **DTOs & Mappers**: Uses MapStruct for converting between Entities and DTOs.
- **Unified API Response**: All controllers return a standardized `ApiResponse` record.
- **Global Error Handling**: Centralized exception handling using `@RestControllerAdvice` in each service.
- **Validation**: Uses JSR-303/JSR-380 bean validation (`spring-boot-starter-validation`).

## Building and Running

### Prerequisites
- Java 21+
- MySQL Server

### Building the Project
From the root directory:
```powershell
./mvnw clean install
```

### Running Services
Each service can be started independently:

- **Buyer Service** (Port 8082):
  ```powershell
  cd buyer-service
  ../mvnw spring-boot:run
  ```
- **Crop Service** (Port 8083):
  ```powershell
  cd crop-service
  ../mvnw spring-boot:run
  ```
- **Farmer Service** (Port 8081):
  ```powershell
  cd farmer-service
  ../mvnw spring-boot:run
  ```

### Database Configuration
Ensure MySQL is running. Default credentials in `application.yaml` are `root/root`.
- Buyer DB: `farmconnect_buyer_db`
- Crop DB: `farmConnect_crop`
- Farmer DB: `farmer_service_db`

## Development Conventions

- **ApiResponse**: Always wrap controller return values in the `ApiResponse` DTO.
- **Lombok**: Use Lombok annotations (`@Data`, `@Getter`, `@Setter`, `@Builder`, etc.) to reduce boilerplate.
- **MapStruct**: Define mappers in the `mapper` package for all entity-DTO conversions.
- **Exception Handling**: Add custom exceptions to the `exception` package and handle them in `GlobalExceptionHandler`.
- **Entity Naming**: Follow standard Java camelCase for fields and rely on Hibernate to map them to snake_case in MySQL.
- **File Uploads**: `crop-service` is configured to handle multipart file uploads up to 20MB.

## API Documentation
Once running, you can access Swagger UI at:
- Buyer Service: `http://localhost:8082/swagger-ui/index.html`
- Crop Service: `http://localhost:8083/swagger-ui/index.html`
- Farmer Service: `http://localhost:8081/swagger-ui/index.html`
