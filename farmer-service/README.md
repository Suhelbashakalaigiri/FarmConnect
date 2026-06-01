# Farmer Service - Farmer Crop Marketplace Platform

This is the Farmer Microservice for the Farmer Crop Marketplace Platform.

## Features
1. **Farmer Registration**: Register new farmers with profile, land, and bank details.
2. **Profile Management**: View, update, and delete farmer profiles.
3. **Mock Crop Summary**: View crops listed by the farmer (Mock data).
4. **Mock Orders Summary**: View orders received by the farmer (Mock data).
5. **Business Validations**: Age check (18+), duplicate checks for Email, Phone, and Aadhaar.

## Tech Stack
- Java 21
- Spring Boot 4.0.6 (Inherited from parent)
- MySQL
- Lombok
- MapStruct
- Hibernate / JPA

## Database Setup
1. Create a MySQL database named `farmer_service_db`.
2. Update `src/main/resources/application.yaml` with your MySQL credentials.

### SQL Table Structure
```sql
CREATE TABLE farmers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    date_of_birth DATE NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    village VARCHAR(100) NOT NULL,
    mandal VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    land_area DOUBLE NOT NULL,
    land_unit VARCHAR(20) NOT NULL,
    farming_type VARCHAR(20) NOT NULL,
    aadhaar_number VARCHAR(12) NOT NULL UNIQUE,
    bank_account_number VARCHAR(20) NOT NULL,
    ifsc_code VARCHAR(11) NOT NULL,
    profile_image_url VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    verification_status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);
```

## Running the Application
1. Navigate to the project root: `cd farm-connect`
2. Build the project: `./mvnw clean install`
3. Run the Farmer Service: `cd farmer-service && ../mvnw spring-boot:run`

## API Endpoints

All APIs return a structured response:
```json
{
  "success": true,
  "message": "...",
  "statusCode": 200,
  "data": { ... },
  "timestamp": "2026-05-26T15:30:00.000000"
}
```

### 1. Farmer Profile APIs

#### Register Farmer
- **URL**: `POST /api/farmers`
- **Request Body**:
```json
{
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "9876543210",
  "password": "password123",
  "gender": "MALE",
  "dateOfBirth": "1985-06-15",
  "addressLine": "House No 45, Main Road",
  "village": "Green Village",
  "mandal": "Harvest Mandal",
  "district": "Organic District",
  "state": "Maharashtra",
  "pincode": "400001",
  "landArea": 5.5,
  "landUnit": "ACRES",
  "farmingType": "ORGANIC",
  "aadhaarNumber": "123456789012",
  "bankAccountNumber": "91234567890",
  "ifscCode": "SBIN0001234"
}
```

#### Get All Farmers
- **URL**: `GET /api/farmers`

#### Get Farmer by ID
- **URL**: `GET /api/farmers/{id}`

#### Update Farmer
- **URL**: `PUT /api/farmers/{id}`
- **Request Body**: (Similar to register, fields like email and aadhaar are protected)

#### Delete Farmer
- **URL**: `DELETE /api/farmers/{id}`

### 2. Mock APIs (Simulating other services)

#### View Crop Summary
- **URL**: `GET /api/farmers/{id}/crops`

#### View Order Summary
- **URL**: `GET /api/farmers/{id}/orders`

## Postman Testing
Import the collection into Postman. Ensure MySQL is running on port 3306 with credentials `root/root` or update `application.yaml`.
