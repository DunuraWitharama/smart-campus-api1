# Smart Campus IoT Management API

Student Name: Dunura Saradha Witharama  
Student ID: 20231815 / w2119959  
Module: Client-Server Architectures (5COSC022W)  
University: University of Westminster /IIT  

---

## Project Introduction

This is a RESTful API built using Java (JAX-RS) to manage a university smart campus system.  
It allows management of Rooms, Sensors, and Sensor Readings such as temperature and motion data.

The system simulates an IoT environment where sensors are installed in rooms and continuously provide readings.

---

## 1. How to Run the Project

1. Open the project in VS Code or IntelliJ IDEA  
2. Make sure Java (JDK 17) and Maven are installed  
3. Run the following commands:

mvn clean install  
mvn exec:java  

4. Access the API at:  
http://localhost:8080/api/v1/

---

## 2. Technical Analysis (Report)

### 2.1 JAX-RS Lifecycle and Data Synchronization

In this project, JAX-RS uses a request-scoped lifecycle. This means a new instance of the resource class is created for every request. Because these instances do not persist, data cannot be stored inside them.

To solve this, a static DataStore using HashMap is used. This ensures all data is shared across requests and remains available throughout the application lifecycle.

---

### 2.2 Benefits of the Discovery Endpoint

A root endpoint at /api/v1/ is implemented using HATEOAS principles. This makes the API self-discoverable instead of relying on external documentation.

Clients can dynamically identify available resources such as rooms and sensors. This improves flexibility and reduces dependency on hardcoded URLs.

---

### 2.3 Sensor Validation Logic

The system ensures that a sensor cannot be created unless the specified room exists.

Before adding a sensor, the system checks if the roomId exists in the DataStore. If not, the API returns a 422 Unprocessable Entity response.

This prevents invalid data and ensures proper relationships between rooms and sensors.

---

### 2.4 Error Mapping (422 vs 404)

Custom exception mapping is used to return meaningful responses.

404 Not Found is used when a resource does not exist.  
422 Unprocessable Entity is used when the request is valid but contains incorrect data.

This improves clarity and helps clients understand errors better.

---

### 2.5 Security and the Global Safety Net

A global exception mapper is implemented to handle unexpected errors.

Instead of exposing stack traces, the system returns a clean 500 Internal Server Error response.  
This prevents sensitive internal information from being exposed and improves API security.

---

## 3. API Endpoints

Method | URL | Description  
GET | /api/v1/ | API Discovery  
GET | /api/v1/rooms | List all rooms  
POST | /api/v1/rooms | Create a room  
GET | /api/v1/rooms/{id} | Get room by ID  
DELETE | /api/v1/rooms/{id} | Delete room (only if no sensors)  
GET | /api/v1/sensors | List all sensors  
GET | /api/v1/sensors?type= | Filter sensors by type  
POST | /api/v1/sensors | Create sensor  
GET | /api/v1/sensors/{id}/readings | Get sensor readings  
POST | /api/v1/sensors/{id}/readings | Add sensor reading  

---

## 4. Sample Requests (cURL)

Create Room:
curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"R1\",\"name\":\"Lab\",\"capacity\":30}"

Get Rooms:
curl http://localhost:8080/api/v1/rooms

Create Sensor:
curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"S1\",\"type\":\"temperature\",\"status\":\"ACTIVE\",\"roomId\":\"R1\"}"

Add Reading:
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings -H "Content-Type: application/json" -d "{\"id\":\"RD1\",\"timestamp\":123456,\"value\":25.5}"

---

## Technologies Used

Java  
JAX-RS (Jersey)  
Grizzly Server  
Maven  

---

## Features

Room Management  
Sensor Management with validation  
Sensor Readings (sub-resource)  
Filtering using query parameters  
Exception handling with custom mappers  
Logging filter for request and response tracking  
Discovery endpoint  

---

## Author

Dunura Saradha Witharama