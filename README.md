# 🚗 RideLink - Backend Microservices Architecture

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-MongoDB-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/SLIIT-IT3130--Application--Development-blue.svg)](https://www.sliit.lk/)

> **IT3130 Application Development Group Assignment**  
> RideLink is a distributed, microservices-based ride-hailing & transport management platform built using Java 17, Spring Boot, MongoDB, and RESTful Web APIs.

---

## 🏗️ Architecture Overview

The system consists of 4 core microservices communicating independently:

```
                              ┌──────────────────────────────┐
                              │       RideLink Client        │
                              └──────────────┬───────────────┘
                                             │
         ┌───────────────────────┬───────────┴───────────┬───────────────────────┐
         │                       │                       │                       │
┌────────▼─────────┐    ┌────────▼─────────┐    ┌────────▼─────────┐    ┌────────▼─────────┐
│ Account Service  │    │ Driver & Vehicle │    │  Fare & Payment  │    │ Ride Management  │
│  (Port: 8081)    │    │  (Port: 8082)    │    │   (Port: 8083)   │    │   (Port: 8084)   │
└──────────────────┘    └──────────────────┘    └──────────────────┘    └──────────────────┘
```

---

## 👥 Group Members (Contributors)

| Student ID | Name | Role / Microservice |
| :--- | :--- | :--- |
| **IT24104304** | Nelundi Kulasuriya | **Driver & Vehicle Service** *(Group Leader)* |
| **IT24104276** | Bandara J.K.T.I. | **Account Service** |
| **IT24104353** | Karunarathna H.W.D.S.S. | **Fare & Payment Service** |
| **IT24104143** | Perera H.G.K.D. | **Ride Management Service** |

---

## 📡 Complete REST API Documentation

### 🔑 1. Account Service (`/api/accounts`) - Port `8081`
> Manages user registration, authentication, JWT security tokens, and user profile operations.

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/accounts/register` | Register a new user account (RIDER, DRIVER, ADMIN) |
| `POST` | `/api/accounts/login` | Authenticate user and return JWT bearer token |
| `GET` | `/api/accounts` | Retrieve a list of all registered user accounts |
| `GET` | `/api/accounts/{id}` | Get specific user account details by Account ID |
| `PUT` | `/api/accounts/{id}/profile` | Update user profile details (name, phone, role) |
| `DELETE` | `/api/accounts/{id}` | Delete a user account |

---

### 🚗 2. Driver & Vehicle Service (`/api/drivers` & `/api/vehicles`) - Port `8082`
> Manages driver profiles, driver availability status, vehicle registrations, and vehicle assignments.

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/drivers` | Register a new driver profile |
| `GET` | `/api/drivers/{driverId}` | Get driver details by Driver ID |
| `PUT` | `/api/drivers/{driverId}` | Update driver details and availability status (`AVAILABLE`, `BUSY`, `OFFLINE`) |
| `GET` | `/api/drivers/available` | Retrieve a list of all currently available drivers |
| `POST` | `/api/vehicles` | Register a new vehicle profile |
| `GET` | `/api/vehicles/{vehicleId}` | Get vehicle details by Vehicle ID |
| `PUT` | `/api/vehicles/{vehicleId}` | Update vehicle details and status |
| `GET` | `/api/drivers/{driverId}/vehicles` | Get all vehicles registered under a specific driver |

---

### 💳 3. Fare & Payment Service (`/api/fares` & `/api/payments`) - Port `8083`
> Handles fare estimations based on distance/time, fare finalizations, and payment simulation processing.

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/fares/estimate` | Estimate ride fare based on pickup/dropoff coordinates & vehicle type |
| `POST` | `/api/fares/{fareId}/finalize` | Finalize final fare amount upon ride completion |
| `GET` | `/api/fares/{fareId}` | Get fare calculation breakdown by Fare ID |
| `GET` | `/api/fares/ride/{rideId}` | Get fare details linked to a specific Ride ID |
| `POST` | `/api/payments` | Process payment for a completed ride (CASH, CARD, WALLET) |
| `GET` | `/api/payments/{paymentId}` | Get payment transaction details by Payment ID |
| `GET` | `/api/payments/ride/{rideId}` | Retrieve payment status for a specific Ride ID |

---

### 🚘 4. Ride Management Service (`/api/rides`) - Port `8084`
> Handles ride booking requests, status workflows (`REQUESTED`, `ACCEPTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/rides` | Create a new ride booking request |
| `GET` | `/api/rides` | Retrieve all ride bookings |
| `GET` | `/api/rides/{id}` | Get ride details by Ride ID |
| `PUT` | `/api/rides/{id}` | Update ride details and status |
| `DELETE` | `/api/rides/{id}` | Cancel / delete a ride booking |

---

## 💻 Local Setup & Execution Guide

### Prerequisites
- **JDK 17 or higher**
- **Apache Maven 3.8+**
- **MongoDB** (Running on `localhost:27017` or configured via MongoDB Atlas)

### How to Run Microservices

1. **Clone the repository**:
   ```bash
   git clone https://github.com/IT24104304/RideLink.git
   cd RideLink
   ```

2. **Run each microservice via Maven**:
   ```bash
   # 1. Account Service (Port 8081)
   cd account-service
   ./mvnw spring-boot:run

   # 2. Driver & Vehicle Service (Port 8082)
   cd ../driver-vehicle-service
   ./mvnw spring-boot:run

   # 3. Fare & Payment Service (Port 8083)
   cd ../fare-payment-service
   ./mvnw spring-boot:run

   # 4. Ride Management Service (Port 8084)
   cd ../ride-management-service
   ./mvnw spring-boot:run
   ```

---

## 📖 Swagger OpenAPI Documentation

Interactive API documentation and testing UI can be accessed at:
- **Account Service**: `http://localhost:8081/swagger-ui.html`
- **Driver & Vehicle Service**: `http://localhost:8082/swagger-ui.html`
- **Fare & Payment Service**: `http://localhost:8083/swagger-ui.html`
- **Ride Management Service**: `http://localhost:8084/swagger-ui.html`

---

## 📄 Academic Context
Developed for **IT3130 - Application Development** course module at **Sri Lanka Institute of Information Technology (SLIIT)**.
