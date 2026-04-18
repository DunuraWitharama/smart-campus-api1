# Smart Campus IoT Management API

**Student Name:** Dunura Saradha Witharama  
**Student ID:** 20231815 / w2119959  
**Module:** Client-Server Architectures (5COSC022W)  
**University:** University of Westminster / IIT  

---

## Project Introduction

This project is a RESTful API developed using Java (JAX-RS with Jersey) to manage a Smart Campus environment.  
It provides functionality to manage Rooms, Sensors, and Sensor Readings such as temperature and motion data.

The system simulates an IoT-based campus where sensors are deployed in rooms and continuously generate data.

---

## 1. How to Run the Project

1. Open the project in VS Code or IntelliJ IDEA  
2. Ensure Java (JDK 17) and Maven are installed  
3. Run the following commands:

```
mvn clean install
mvn exec:java
```

4. Access the API at:  
http://localhost:8080/api/v1/

---

## 2. Technical Analysis

### 2.1 JAX-RS Lifecycle and Data Synchronization

JAX-RS uses a request-scoped lifecycle, meaning a new resource instance is created for each request.  
Because of this, resource classes cannot store persistent data.

To solve this, a static `DataStore` is used with `ConcurrentHashMap`, ensuring:

- Data persistence across requests  
- Thread-safe operations for concurrent users  
- Prevention of race conditions  

---

### 2.2 Discovery Endpoint (HATEOAS)

A root endpoint at `/api/v1/` is implemented using HATEOAS principles.

Instead of relying on external documentation, the API provides dynamic links to available resources such as:

- Rooms  
- Sensors  

This makes the API self-discoverable and flexible to future changes.

---

### 2.3 Sensor Validation Logic

The system ensures that a sensor cannot be created unless the specified room exists.

Before creating a sensor:

- The API validates the `roomId`  
- If invalid, it returns **422 Unprocessable Entity**  

This prevents invalid relationships and maintains data integrity.

---

### 2.4 Sub-Resource Design

Sensor readings are implemented as a sub-resource:

```
/sensors/{id}/readings
```

This design:

- Maintains a clear parent-child relationship  
- Improves API structure  
- Reflects real-world IoT hierarchy  

Additionally, each reading updates the sensor’s current value.

---

### 2.5 Error Handling Strategy

Custom exception handling is implemented using `ExceptionMapper`.

The API returns structured JSON error responses:

```
{
  "error": "message"
}
```

Status codes used:

- **404 Not Found** → Resource does not exist  
- **409 Conflict** → Business rule violation (e.g., deleting a room with sensors)  
- **422 Unprocessable Entity** → Invalid input data  
- **403 Forbidden** → Sensor unavailable (e.g., maintenance state)  
- **500 Internal Server Error** → Unexpected errors  

---

### 2.6 Security Considerations

A global exception handler is implemented to prevent exposure of internal system details.

Instead of returning stack traces, the API returns a clean error message:

```
{
  "error": "Something went wrong"
}
```

This protects sensitive internal information and improves API security.

---

### 2.7 Logging

A logging filter is implemented to track:

- Incoming requests  
- Response status codes  

This helps in debugging and monitoring API behavior.

---

## 3. API Endpoints

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

## 4. Sample Requests (cURL)

### Create Room
```
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\":\"R1\",\"name\":\"Lab\",\"capacity\":30}"
```

### Get Rooms
```
curl http://localhost:8080/api/v1/rooms
```

### Create Sensor
```
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"S1\",\"type\":\"temperature\",\"status\":\"ACTIVE\",\"roomId\":\"R1\"}"
```

### Add Reading
```
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d "{\"id\":\"RD1\",\"timestamp\":123456,\"value\":25.5}"
```

---

## Technologies Used

- Java  
- JAX-RS (Jersey)  
- Grizzly HTTP Server  
- Maven  

---

## Features

- Room management with validation  
- Sensor management with relationship enforcement  
- Sensor readings using sub-resource design  
- Query parameter filtering  
- Structured JSON error handling  
- Global exception mapping  
- Logging filter for request/response tracking  
- HATEOAS-based discovery endpoint  
- Thread-safe in-memory data storage  

---

## Author

Dunura Saradha Witharama