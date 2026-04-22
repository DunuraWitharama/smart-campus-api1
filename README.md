# Smart Campus IoT Management API

**Student Name:** Dunura Saradha Witharama  
**Student ID:** 20231815 / w2119959  
**Module:** Client-Server Architectures (5COSC022W)  
**University:** University of Westminster / IIT  

---

## Project Introduction

This project is a RESTful API developed using Java (JAX-RS with Jersey) and deployed on an **Apache Tomcat server**.

The system manages a Smart Campus IoT environment including:
- Rooms
- Sensors
- Sensor Readings (temperature, motion, etc.)

The API demonstrates core REST principles such as:
- Resource-based design
- HATEOAS (Discovery endpoint)
- Validation & integrity constraints
- Sub-resource architecture
- Filtering with query parameters
- Robust error handling & security

---

# 1. How to Run the Project (Tomcat Deployment)

### Prerequisites
- Java JDK 17
- Maven
- Apache Tomcat 9

---

### Step 1 — Build the Project

```
mvn clean package
```

👉 This creates:
```
target/smart-campus-api.war
```

---

### Step 2 — Deploy to Tomcat

Copy the `.war` file into:

```
C:\apache-tomcat-9.0.117\webapps\
```

---

### Step 3 — Start Tomcat

```
cd C:\apache-tomcat-9.0.117\bin
.\startup.bat
```

---

### Step 4 — Access API

```
http://localhost:8080/smart-campus-api/api/v1/
```

---

# 2. API Endpoints

| Method | URL |
|--------|-----|
| GET | /api/v1/ |
| GET | /api/v1/rooms |
| POST | /api/v1/rooms |
| GET | /api/v1/rooms/{id} |
| DELETE | /api/v1/rooms/{id} |
| GET | /api/v1/sensors |
| GET | /api/v1/sensors?type= |
| POST | /api/v1/sensors |
| GET | /api/v1/sensors/{id}/readings |
| POST | /api/v1/sensors/{id}/readings |

---

# 3. Sample Requests

### Create Room
```
POST /api/v1/rooms
```

```json
{
  "id": "R1",
  "name": "Lab",
  "capacity": 30
}
```

---

### Create Sensor
```json
{
  "id": "S1",
  "type": "temperature",
  "status": "ACTIVE",
  "roomId": "R1"
}
```

---

### Add Reading
```json
{
  "id": "RD1",
  "timestamp": 123456,
  "value": 25.5
}
```

---

# 4. Technologies Used

- Java (JDK 17)
- JAX-RS (Jersey)
- Apache Tomcat (WAR Deployment)
- Maven

---

# 5. Features

- Full Room CRUD operations
- Sensor validation with foreign key constraints
- Sensor readings using sub-resource pattern
- Dynamic filtering using query parameters
- Custom exception handling with structured JSON
- Global exception mapper (security)
- Logging filter for request/response tracking
- Discovery endpoint (HATEOAS)
- Thread-safe data storage using ConcurrentHashMap

---

# 📄 6. CONCEPTUAL REPORT

---

## PART 1: SERVICE ARCHITECTURE & SETUP

### 1.1 Project & Application Configuration

**Question:**  
Explain the lifecycle of JAX-RS resources and how data is managed.

**Answer:**  
JAX-RS follows a **request-scoped lifecycle**, meaning a new instance of a resource class is created for each HTTP request. This ensures thread safety because no instance is shared between requests.

However, this prevents storing persistent data inside resource classes. To solve this, the system uses a centralized static `DataStore` class that holds all data.

The DataStore uses `ConcurrentHashMap`, which ensures thread-safe access when multiple users interact with the API simultaneously. This avoids race conditions and guarantees consistency.

Additionally, the application is configured using `ResourceConfig` and deployed using a WAR file on Apache Tomcat, making it portable and suitable for real-world deployment.

---

### 1.2 Discovery Endpoint (HATEOAS)

**Question:**  
Why is HATEOAS important?

**Answer:**  
HATEOAS allows the API to guide clients dynamically by returning links to available resources.

The endpoint:
```
/api/v1/
```

returns:
- rooms endpoint
- sensors endpoint

This removes hardcoded URLs, improves flexibility, and makes the API self-documenting.

---

## PART 2: ROOM MANAGEMENT

### 2.1 Room Resource Implementation

**Question:**  
Returning IDs vs full objects?

**Answer:**  
Returning only IDs reduces payload size but increases the number of API calls.

Returning full objects:
- reduces client complexity
- improves performance for small datasets
- avoids additional network requests

This implementation uses full objects for better usability.

---

### 2.2 Room Deletion Logic

**Question:**  
Is DELETE idempotent?

**Answer:**  
Yes. Multiple DELETE requests produce the same result.

Logic implemented:
- First DELETE → removes room
- Next DELETE → returns 404

Additionally:
- Prevent deletion if sensors exist
- Returns **409 Conflict**

This ensures strong data integrity.

---

## PART 3: SENSOR OPERATIONS

### 3.1 Sensor Validation

**Question:**  
What happens if room does not exist?

**Answer:**  
The system validates that `roomId` exists before creating a sensor.

If invalid:
- Returns **422 Unprocessable Entity**

This ensures:
- Referential integrity
- No orphan data

---

### 3.2 Filtering

**Question:**  
Why query parameters?

**Answer:**  
Query parameters are ideal for filtering collections.

Example:
```
/api/v1/sensors?type=temperature
```

Advantages:
- Flexible
- Optional
- REST-compliant

---

## PART 4: SUB-RESOURCES

### 4.1 Sub-Resource Locator

**Question:**  
Why use sub-resources?

**Answer:**  
Sub-resources allow hierarchical structuring.

Example:
```
/sensors/{id}/readings
```

Benefits:
- Separation of concerns
- Cleaner design
- Better scalability

---

### 4.2 Historical Data Management

Sensor readings are stored per sensor in a list.

When a reading is added:
- It is appended to history
- Sensor current value is updated

This ensures:
- Historical tracking
- Real-time state consistency

---

## PART 5: ERROR HANDLING

### 5.1 HTTP 422 vs 404

**Question:**  
Why use 422?

**Answer:**  
422 = valid request, invalid data  
404 = resource not found

Example:
- Invalid roomId → 422
- Missing endpoint → 404

---

### 5.2 Global Exception Handling

**Question:**  
Why hide stack traces?

**Answer:**  
Stack traces expose:
- file paths
- internal logic
- vulnerabilities

A global exception mapper returns:
```
{
  "error": "Something went wrong"
}
```

This improves security.

---

### 5.3 Logging Filters

Logging is implemented using filters to:
- track requests/responses
- centralize logging logic
- avoid duplication

---

## Author

Dunura Saradha Witharama