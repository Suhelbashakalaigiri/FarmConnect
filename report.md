# Farm Connect Microservices Report

## 1. Buyer Service

### Entities and Fields
- **Buyer**
    - `buyerId` (Long, PK): Unique identifier for the buyer.
    - `fullName` (String): Full name of the buyer.
    - `email` (String, Unique): Email address.
    - `phoneNumber` (String): Contact number.
    - `password` (String): Hashed password.
    - `companyName` (String): Optional company name.
    - `addressLine` (String): Street address.
    - `villageCity` (String): Village or City name.
    - `district` (String): District name.
    - `state` (String): State name.
    - `pincode` (String): Postal code.
    - `buyerType` (Enum: INDIVIDUAL, WHOLESALER, RETAILER): Type of buyer.
    - `status` (Enum: ACTIVE, INACTIVE, BLOCKED): Account status.
    - `createdAt` (LocalDateTime): Timestamp of creation.
    - `updatedAt` (LocalDateTime): Timestamp of last update.

### Endpoints
- **Buyer Profile Management**
    - `POST /api/buyers`: Register a new buyer.
    - `GET /api/buyers`: Retrieve all buyers.
    - `GET /api/buyers/{buyerId}`: Retrieve a specific buyer by ID.
    - `PUT /api/buyers/{buyerId}`: Update buyer profile.
    - `DELETE /api/buyers/{buyerId}`: Delete a buyer account.
- **Marketplace Browsing (Mocked)**
    - `GET /api/buyers/crops`: Browse available crops.
    - `GET /api/buyers/crops/{cropId}`: Get details of a specific crop.
- **Bidding & Ordering (Mocked)**
    - `POST /api/buyers/bids`: Place a bid on a crop.
    - `POST /api/buyers/orders`: Place an order for a crop.
- **Logistics & Finance (Mocked)**
    - `GET /api/buyers/deliveries/{orderId}`: Track delivery status of an order.
    - `GET /api/buyers/transactions`: View transaction history.

### Services and Functionalities
- **BuyerService**: Handles core buyer registration and profile maintenance. It also serves as an entry point for buyers to interact with the marketplace, though many features (browsing, bidding, ordering) currently return mock data.

### Inter-service Communication Identification
- **browseCrops() / getCropById()**: These methods currently return mock data but should communicate with the **Crop Service** to fetch real-time crop listings.
- **placeOrder()**: Should communicate with the **Crop Service** to verify availability and update quantity, and potentially an **Order Service** or **Farmer Service**.
- **placeBid()**: Should communicate with a **Bidding/Auction Service**.
- **trackDelivery()**: Should integrate with a **Logistics/Delivery Service**.
- **getTransactionHistory()**: Should integrate with a **Payment/Transaction Service**.

---

## 2. Crop Service

### Entities and Fields
- **Category**
    - `id` (Long, PK): Unique identifier.
    - `categoryName` (String, Unique): Name of the category (e.g., Grains, Vegetables).
    - `description` (String): Brief description.
- **Crop**
    - `id` (Long, PK): Unique identifier.
    - `cropName` (String): Name of the crop.
    - `farmerId` (Long): Reference to the farmer who owns the crop.
    - `quantity` (Double): Available quantity.
    - `price` (Double): Price per unit.
    - `harvestDate` (LocalDate): Date of harvest.
    - `imageUrl` (String): URL/Path to the crop image.
    - `status` (Enum: AVAILABLE, SOLD_OUT, PENDING): Listing status.
    - `category` (ManyToOne -> Category): Associated category.
    - `createdAt` (LocalDateTime): Creation timestamp.
    - `updatedAt` (LocalDateTime): Last update timestamp.

### Endpoints
- **Category Management**
    - `POST /api/category/create`: Create a new crop category.
    - `GET /api/category/getall`: List all categories.
    - `GET /api/category/getbyid/{categoryId}`: Get category by ID.
    - `GET /api/category/getbyname/{categoryName}`: Get category by name.
    - `DELETE /api/category/delete/{categoryId}`: Delete a category.
- **Crop Management**
    - `POST /api/crop/addcrop`: Add a new crop listing.
    - `PUT /api/crop/updatecrop/{cropId}`: Update crop details.
    - `DELETE /api/crop/deletecrop/{Id}`: Remove a crop listing.
    - `GET /api/crop/getcropby/{Id}`: Get specific crop details.
    - `GET /api/crop/getallcrops`: List all available crops.
    - `GET /api/crop/getcropsby/{categoryId}`: List crops by category.
    - `PUT /api/crop/uploadimage/{cropId}`: Upload an image for a crop listing.

### Services and Functionalities
- **CategoryService**: Manages the taxonomy of crops.
- **CropService**: Manages crop listings, including price, quantity, and image uploads (stored in local `uploads/products/` directory).

### Inter-service Communication Identification
- **addCrop()**: Currently uses a hardcoded `farmerId = 1L`. It should communicate with the **Farmer Service** to validate the `farmerId` before creating a listing.
- **Image Upload**: While internal, it populates `imageUrl` which is consumed by the **Buyer Service**.

---

## 3. Farmer Service

### Entities and Fields
- **Farmer**
    - `id` (Long, PK): Unique identifier.
    - `fullName` (String): Full name.
    - `email` (String, Unique): Email.
    - `phoneNumber` (String, Unique): Phone number.
    - `password` (String): Hashed password.
    - `gender` (Enum: MALE, FEMALE, OTHER): Gender.
    - `dateOfBirth` (LocalDate): Birth date (used for age validation).
    - `addressLine` (String): Street address.
    - `village`, `mandal`, `district`, `state`, `pincode`: Location details.
    - `landArea` (Double): Total farming land area.
    - `landUnit` (Enum: ACRES, HECTARES, GUNTA): Unit of land measurement.
    - `farmingType` (Enum: ORGANIC, CONVENTIONAL, SUBSISTENCE, COMMERCIAL): Method of farming.
    - `aadhaarNumber` (String, Unique): Government ID.
    - `bankAccountNumber`, `ifscCode`: Financial details for payments.
    - `profileImageUrl` (String): Path to profile picture.
    - `status` (Enum: ACTIVE, INACTIVE, BLOCKED): Account status.
    - `verificationStatus` (Enum: PENDING, VERIFIED, REJECTED): KYC status.
    - `createdAt` / `updatedAt`: Timestamps.

### Endpoints
- **Farmer Profile Management**
    - `POST /api/farmers`: Register a new farmer (Validates age 18+).
    - `GET /api/farmers`: Retrieve all farmers.
    - `GET /api/farmers/{id}`: Get profile by ID.
    - `PUT /api/farmers/{id}`: Update profile (Blocked farmers cannot update).
    - `DELETE /api/farmers/{id}`: Delete account.
- **Farmer Activity Summaries (Mocked)**
    - `GET /api/farmers/{id}/crops`: Get summary of crops listed by the farmer.
    - `GET /api/farmers/{id}/orders`: Get summary of orders received by the farmer.

### Services and Functionalities
- **FarmerService**: Handles registration with strict business validations (duplicate checks, age verification). Manages farmer lifecycle and status.

### Inter-service Communication Identification
- **getFarmerCropSummary()**: Currently returns mock data. It should communicate with the **Crop Service** to aggregate data for all crops belonging to the specific `farmerId`.
- **getFarmerOrdersSummary()**: Currently returns mock data. It should communicate with an **Order Service** or **Buyer Service** to fetch orders involving the farmer's crops.
- **Profile Update**: Validation logic could potentially check against external KYC services in the future.
