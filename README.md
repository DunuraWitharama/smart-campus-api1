# Smart Campus IoT Management API

**Student Name:** Dunura Saradha Witharama  
**Student ID:** 20231815 / w2119959  
**Module:** Client-Server Architectures (5COSC022W)  
**University:** University of Westminster / IIT  

---

## Project Introduction

This project is a RESTful API developed using Java (JAX-RS with Jersey) and an embedded Grizzly server to manage a Smart Campus environment.  
It provides functionality to manage Rooms, Sensors, and Sensor Readings such as temperature and motion data.

The system simulates an IoT-based campus where sensors are deployed in rooms and continuously generate data. The API demonstrates key RESTful principles including resource modeling, validation, sub-resources, filtering, and robust error handling.

---

## 1. How to Run the Project

1. Open the project in VS Code or IntelliJ IDEA  
2. Ensure Java (JDK 17) and Maven are installed  

Run:

```
mvn clean install
mvn exec:java
```

Access the API at:  
http://localhost:8080/api/v1/

---

## 2. API Endpoints

| Method | URL                           | Description                      |
|--------|--------------------------------|----------------------------------|
| GET    | /api/v1/                      | API Discovery                    |
| GET    | /api/v1/rooms                 | List all rooms                   |
| POST   | /api/v1/rooms                 | Create a room                    |
| GET    | /api/v1/rooms/{id}            | Get room by ID                   |
| DELETE | /api/v1/rooms/{id}            | Delete room (only if no sensors) |
| GET    | /api/v1/sensors               | List all sensors                 |
| GET    | /api/v1/sensors?type=         | Filter sensors by type           |
| POST   | /api/v1/sensors               | Create sensor                    |
| GET    | /api/v1/sensors/{id}/readings | Get sensor readings              |
| POST   | /api/v1/sensors/{id}/readings | Add sensor reading               |

---

## 3. Sample Requests

### Create Room
```
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\":\"R1\",\"name\":\"Lab\",\"capacity\":30}"
```

### Create Sensor
```
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"S1\",\"type\":\"temperature\",\"status\":\"ACTIVE\",\"roomId\":\"R1\"}"
```

---

## 4. Technologies Used

- Java  
- JAX-RS (Jersey)  
- Grizzly HTTP Server  
- Maven  

---

## 5. Features

- Room management with validation and conflict handling  
- Sensor management with referential integrity validation  
- Sensor readings implemented using sub-resource pattern  
- Query parameter filtering for efficient searching  
- Structured JSON error handling with custom exception mappers  
- Global exception handling for security  
- Logging filter for request and response monitoring  
- HATEOAS-based discovery endpoint  
- Thread-safe in-memory data storage using ConcurrentHashMap  

---

# 📄 6. CONCEPTUAL REPORT

---

## PART 1: SERVICE ARCHITECTURE & SETUP

### 1.1 Project & Application Configuration

**Question:**  
Explain the default lifecycle of a JAX-RS Resource class and how it impacts data management.

**Answer:**  
In JAX-RS, resource classes follow a request-scoped lifecycle, meaning a new instance of each resource class is created for every incoming HTTP request. This ensures thread safety at the resource level since no instance is shared between requests.

However, this also means resource classes cannot store persistent data because their state is destroyed after each request. To handle this, my implementation uses a centralized static `DataStore` class that stores all application data including rooms, sensors, and sensor readings.

The DataStore uses `ConcurrentHashMap`, which ensures thread-safe access when multiple clients interact with the API simultaneously. This prevents race conditions, data corruption, and inconsistent application states.

---

### 1.2 Discovery Endpoint (HATEOAS)

**Question:**  
Why is Hypermedia (HATEOAS) important in REST APIs?

**Answer:**  
HATEOAS allows API responses to include links to related resources, enabling clients to dynamically discover available endpoints.

In my implementation, the discovery endpoint (`/api/v1`) returns links to `/rooms` and `/sensors`. This removes the need for hardcoded URLs and allows the client to navigate the API dynamically.

This improves flexibility, reduces dependency on external documentation, and makes the API self-descriptive and easier to maintain.

---

## PART 2: ROOM MANAGEMENT

### 2.1 Room Resource Implementation

**Question:**  
What are the implications of returning only IDs vs full objects?

**Answer:**  
Returning only IDs reduces network bandwidth but requires additional API calls to fetch full details. This increases latency and complexity.

In my implementation, full room objects are returned. This reduces the number of API calls required, simplifies client-side processing, and improves performance for small datasets.

---

### 2.2 Room Deletion & Safety Logic

**Question:**  
Is DELETE idempotent?

**Answer:**  
Yes, DELETE is idempotent. The first DELETE removes the resource, while subsequent DELETE requests return 404 without changing the system state.

Additionally, I implemented validation to prevent deletion if a room contains sensors. This ensures data integrity by avoiding orphaned sensor records and returns a 409 Conflict response.

---

## PART 3: SENSOR OPERATIONS

### 3.1 Media Type Handling

**Question:**  
What happens if a client sends data in the wrong format?

**Answer:**  
The API enforces JSON input using `@Consumes(MediaType.APPLICATION_JSON)`. If a client sends data in another format, JAX-RS automatically returns a 415 Unsupported Media Type response.

This ensures strict validation, prevents incorrect data processing, and maintains consistency.

---

### 3.2 Filtering & Query Parameters

**Question:**  
Why use query parameters instead of path parameters?

**Answer:**  
Query parameters are designed for filtering collections and are optional. They allow flexible and scalable filtering.

For example:
```
/api/v1/sensors?type=temperature
```

This approach follows REST principles, whereas path parameters are better suited for identifying specific resources.

---

## PART 4: SUB-RESOURCES

### 4.1 Sub-Resource Locator Pattern

**Question:**  
What are the benefits?

**Answer:**  
The sub-resource locator pattern allows nested resources to be handled by separate classes.

In my implementation, sensor readings are accessed via:
```
/sensors/{id}/readings
```

This design separates concerns, improves maintainability, reduces complexity, and allows better scalability of the API.

---

### 4.2 Historical Data Management

Sensor readings are stored in a list associated with each sensor. When a new reading is added, it is appended to the history and also updates the sensor’s current value.

This ensures both historical tracking and real-time accuracy.

---

## PART 5: ERROR HANDLING & LOGGING

### 5.2 HTTP 422 vs 404

**Question:**  
Why use 422 instead of 404?

**Answer:**  
HTTP 422 is used when the request is valid but contains incorrect data. In my implementation, if a sensor references a non-existent room, the API returns 422 because the error is within the payload, not the endpoint itself.

---

### 5.4 Security & Global Exception Handling

**Question:**  
Why should stack traces not be exposed?

**Answer:**  
Stack traces expose internal system details such as class names and file paths. Attackers can use this information to identify vulnerabilities.

To prevent this, I implemented a global exception mapper that returns a generic error response instead of exposing sensitive details.

---

### 5.5 Logging Filters

**Question:**  
Why use filters for logging?

**Answer:**  
Logging is implemented using JAX-RS filters to provide centralized logging of requests and responses.

This avoids code duplication, improves maintainability, and ensures consistent logging across all endpoints.

---

## Author

Dunura Saradha Witharama