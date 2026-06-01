# Eureka Discovery Server

This is the Service Registry for the **Farmer Crop Marketplace Platform**. It uses Netflix Eureka to allow microservices to discover each other without hardcoding hostnames and ports.

## Project Details
- **Application Name**: `DISCOVERY-SERVER`
- **Port**: `8761`
- **Package**: `com.farmmarket.discoveryserver`

## Requirements
- **Java**: 25 (as configured in root POM)
- **Spring Boot**: 4.0.6
- **Spring Cloud**: 2025.1.1

## How to Run
1. Navigate to the `eureka-server` directory:
   ```powershell
   cd eureka-server
   ```
2. Run the application using Maven:
   ```powershell
   ../mvnw spring-boot:run
   ```

## Verification Steps
1. **Startup**: Ensure the logs show `Started EurekaServerApplication in ... seconds`.
2. **Dashboard**: Access the Eureka Dashboard at [http://localhost:8761](http://localhost:8761).
3. **Health Check**: Verify the health status at [http://localhost:8761/actuator/health](http://localhost:8761/actuator/health).

---

## Future Microservice Registration

To register existing or future microservices with this Eureka Server, follow these steps:

### 1. Add Dependency
Add the following to the `pom.xml` of the microservice:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### 2. Configure application.yml
Add the following configuration to the microservice's `application.yml`:

#### Crop Service
```yaml
spring:
  application:
    name: CROP-SERVICE

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### Buyer Service
```yaml
spring:
  application:
    name: BUYER-SERVICE

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### Farmer Service
```yaml
spring:
  application:
    name: FARMER-SERVICE

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### 3. Enable Discovery Client
Add `@EnableDiscoveryClient` (optional in newer Spring Boot versions as it's auto-detected) to the main application class.

---

## Best Practices

1. **Naming Conventions**: Always use uppercase with hyphens for `spring.application.name` (e.g., `ORDER-SERVICE`).
2. **Service Registration**: Ensure `eureka.client.register-with-eureka` is `true` (default) for business services.
3. **High Availability**: In production, run multiple instances of Eureka Server and peer-aware them by listing other Eureka URLs in `defaultZone`.
4. **Health Monitoring**: Use Spring Boot Actuator to monitor the health of registered services. Eureka uses these heartbeats to determine service availability.
5. **Self-Preservation Mode**: Be aware of Eureka's "Self-Preservation" mode, which prevents expiring instances when network issues cause a heartbeat failure across many services.

---

## Common Troubleshooting Guide

| Issue | Possible Cause | Solution |
|-------|----------------|----------|
| Service not showing in Dashboard | Client dependency missing | Ensure `spring-cloud-starter-netflix-eureka-client` is added. |
| Service not showing in Dashboard | Wrong `defaultZone` URL | Check if `eureka.client.service-url.defaultZone` points to `http://localhost:8761/eureka/`. |
| Connection Refused | Eureka Server not running | Ensure `DISCOVERY-SERVER` is started before other services. |
| Hostname resolution issues | Docker/Network config | Set `eureka.instance.prefer-ip-address: true` in the client configuration. |
| Status "DOWN" in Dashboard | Actuator health check failure | Check the service's `/actuator/health` endpoint and logs. |
