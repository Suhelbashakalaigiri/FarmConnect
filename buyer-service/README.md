# Buyer Service - Farmer Crop Marketplace Platform

This is the Buyer Microservice for the Farmer Crop Marketplace Platform.

## Features
1. **Buyer Registration**: Register new buyers with details like name, email, phone, address, etc.
2. **Profile Management**: View, update, and delete buyer profiles.
3. **Mock Crop Browsing**: Browse available crops (Mock data).
4. **Mock Bidding**: Place bids on crops (Mock implementation).
5. **Mock Orders**: Place orders for crops (Mock implementation).
6. **Mock Delivery Tracking**: Track order deliveries (Mock implementation).
7. **Mock Transactions**: View transaction history (Mock implementation).

## Tech Stack
- Java 21
- Spring Boot 4.0.6 (Inherited from parent)
- MySQL
- Lombok
- MapStruct
- Hibernate / JPA

## Database Setup
1. Create a MySQL database named `buyer_service_db`.
2. Update `src/main/resources/application.yaml` with your MySQL credentials.

### SQL Table Structure
```sql
CREATE TABLE buyers (
    buyer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    address_line VARCHAR(255) NOT NULL,
    village_city VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(6) NOT NULL,
    buyer_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
```

## Running the Application
1. Navigate to the project root: `cd farm-connect`
2. Build the project: `./mvnw clean install`
3. Run the Buyer Service: `cd buyer-service && ../mvnw spring-boot:run`

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

### 1. Buyer Profile APIs

#### Register Buyer
- **URL**: `POST /api/buyers`
- **Request Body**:
```json
{
  "fullName": "Alice Smith",
  "email": "alice@example.com",
  "phoneNumber": "9876543210",
  "password": "password123",
  "companyName": "Alice Foods",
  "addressLine": "123 Green St",
  "villageCity": "Greenville",
  "district": "Organic District",
  "state": "California",
  "pincode": "123456",
  "buyerType": "WHOLESALER"
}
```

#### Get All Buyers
- **URL**: `GET /api/buyers`

#### Get Buyer by ID
- **URL**: `GET /api/buyers/{buyerId}`

#### Update Buyer
- **URL**: `PUT /api/buyers/{buyerId}`
- **Request Body**: (Same as register, email and buyerId are not updatable)

#### Delete Buyer
- **URL**: `DELETE /api/buyers/{buyerId}`

### 2. Mock APIs (Simulating other services)

#### Browse Crops
- **URL**: `GET /api/buyers/crops`

#### Place Bid
- **URL**: `POST /api/buyers/bids`
- **Request Body**:
```json
{
  "cropId": 1,
  "buyerId": 1,
  "bidAmount": 26.50,
  "quantity": 500
}
```

#### Place Order
- **URL**: `POST /api/buyers/orders`
- **Request Body**:
```json
{
  "cropId": 1,
  "buyerId": 1,
  "quantity": 100
}
```

#### Track Delivery
- **URL**: `GET /api/buyers/deliveries/{orderId}`

#### Transaction History
- **URL**: `GET /api/buyers/transactions`

## Postman Testing
Import the collection into Postman and test the endpoints. Ensure the MySQL service is running before starting the application.
