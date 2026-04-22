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

This creates:
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

# 3. Sample Requests (cURL Commands)

### 1. API Discovery
```
curl http://localhost:8080/smart-campus-api/api/v1/
```

### 2. Create Room
```
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"R1\",\"name\":\"Lab\",\"capacity\":30}"
```

### 3. Get All Rooms
```
curl http://localhost:8080/smart-campus-api/api/v1/rooms
```

### 4. Create Sensor
```
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"S1\",\"type\":\"temperature\",\"status\":\"ACTIVE\",\"roomId\":\"R1\"}"
```

### 5. Add Sensor Reading
```
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/S1/readings -H "Content-Type: application/json" -d "{\"id\":\"RD1\",\"timestamp\":123456,\"value\":25.5}"
```

### 6. Get Sensor Readings
```
curl http://localhost:8080/smart-campus-api/api/v1/sensors/S1/readings
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
- Sensor validation with referential integrity  
- Sensor readings using sub-resource pattern  
- Dynamic filtering using query parameters  
- Custom exception handling with structured JSON  
- Global exception mapper for security  
- Logging filter for request/response tracking  
- Discovery endpoint (HATEOAS)  
- Thread-safe data storage using ConcurrentHashMap  

---

# 6. CONCEPTUAL REPORT

---

## Part 1: Service Architecture & Setup

### 1.1 Project & Application Configuration

**Question:**  
In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures.

**Answer:**  
JAX-RS resource classes follow a request-scoped lifecycle, meaning a new instance is created for each incoming HTTP request rather than being treated as a singleton. This ensures thread safety because no instance is shared across requests.

However, this also means resource classes cannot store persistent data. To manage shared data, I implemented a centralized static DataStore class using ConcurrentHashMap.

This ensures:
- Safe concurrent access  
- Prevention of race conditions  
- Consistent application state  

The project is built using Maven and deployed as a WAR file on Apache Tomcat, aligning with enterprise deployment practices.

---

### 1.2 The Discovery Endpoint

**Question:**  
Why is the provision of Hypermedia (HATEOAS) considered a hallmark of advanced RESTful design? How does this approach benefit client developers compared to static documentation?

**Answer:**  
HATEOAS allows clients to dynamically discover available API resources through links in responses instead of relying on hardcoded URLs.

In this project, the discovery endpoint provides links to rooms and sensors, making the API self-descriptive.

Benefits include:
- Reduced dependency on static documentation  
- Easier client integration  
- Improved flexibility  

---

## Part 2: Room Management

### 2.1 RoomResource Implementation

**Question:**  
When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects?

**Answer:**  
Returning only IDs reduces payload size but requires additional API calls. Returning full objects provides complete information in one response, simplifies client logic, and improves usability for this system.

---

### 2.2 Room Deletion & Safety Logic

**Question:**  
Is the DELETE operation idempotent in your implementation?

**Answer:**  
Yes, DELETE is idempotent. The first request removes the room, and subsequent requests return 404 without changing the system state.

Deletion is prevented if sensors exist, returning 409 Conflict to maintain data integrity.

---

## Part 3: Sensor Operations & Linking

### 3.1 Sensor Resource & Integrity

**Question:**  
What happens if a client sends data in a different format?

**Answer:**  
JAX-RS returns HTTP 415 Unsupported Media Type if the request is not JSON, ensuring strict validation.

---

### 3.2 Filtered Retrieval & Search

**Question:**  
Why use query parameters instead of path parameters?

**Answer:**  
Query parameters are flexible and suitable for filtering collections without changing resource identity, making them more appropriate for search operations.

---

## Part 4: Deep Nesting with Sub-Resources

### 4.1 Sub-Resource Locator Pattern

**Question:**  
What are the benefits of the Sub-Resource Locator pattern?

**Answer:**  
It improves modularity, separates concerns, and enhances maintainability by delegating nested logic to separate classes.

---

### 4.2 Historical Data Management

Sensor readings are stored per sensor. Adding a reading updates both history and current sensor value, ensuring consistency.

---

## Part 5: Advanced Error Handling, Exception Mapping & Logging

### 5.2 Dependency Validation (422)

**Question:**  
Why is HTTP 422 more accurate than 404?

**Answer:**  
422 indicates valid request but invalid data (e.g., non-existent roomId), while 404 indicates missing resource.

---

### 5.4 The Global Safety Net (500)

**Question:**  
Why should stack traces not be exposed?

**Answer:**  
They reveal internal system details that can be exploited. A global exception mapper ensures secure responses.

---

### 5.5 Logging Filters

**Question:**  
Why use filters for logging?

**Answer:**  
Filters centralize logging, reduce duplication, and keep business logic clean.

---

## Author

Dunura Saradha Witharama