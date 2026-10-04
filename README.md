# RideLink
IT3130 Application Development Group Assignment - RideLink Backend Microservices
# 🚗 RideLink – Backend Microservices

**IT3130 Application Development Group Assignment**

RideLink is a backend microservices-based application developed as a group assignment for the IT3130 Application Development module. The project is designed to organize backend functionalities into separate services, making the application modular, maintainable, and scalable.

## 📌 Project Overview

The RideLink backend is divided into independent microservices, each responsible for a specific functionality. This structure improves code organization and makes individual services easier to develop, test, and maintain.

## 🛠️ Technologies Used

- **Programming Language:** Java
- **Architecture:** Microservices
- **API Documentation:** Swagger / OpenAPI
- **Testing:** Unit Testing
- **Version Control:** Git & GitHub

## 📂 Project Structure

```text
RideLink/
├── account-service/
├── driver-vehicle-service/
├── fare-payment-service/
├── .gitignore
└── README.md
```

### 🔹 Microservices

**1. Account Service**
- Manages account-related functionality.
- Includes account models with roles and status.
- Supports unit testing and test configuration.

**2. Driver Vehicle Service**
- Handles driver and vehicle-related functionality.
- Includes unit tests for driver and vehicle services.

**3. Fare and Payment Service**
- Handles fare and payment-related functionality.
- Provides API documentation using Swagger/OpenAPI.

## 📖 API Documentation

Swagger/OpenAPI is used to document the APIs and make it easier for developers to understand and test the available endpoints.

To access the Swagger UI, run the relevant service and open its configured Swagger URL in a browser.

> Note: The exact URL and port depend on each service's configuration.

## 🧪 Testing

Unit tests are included to verify the functionality of individual services and improve application reliability.

## 🚀 Getting Started

### Prerequisites

Make sure you have installed:

- Java Development Kit (JDK)
- Git
- Maven or the build tool configured for the project

### Clone the Repository

```bash
git clone https://github.com/IT24104304/RideLink.git
```

### Open the Project

1. Open the cloned project in IntelliJ IDEA or another Java IDE.
2. Navigate to the required microservice folder.
3. Configure the necessary application settings.
4. Install dependencies using the build tool configured for that service.
5. Run the service using the IDE or the appropriate build command.

> Note: Database settings, service ports, and environment variables must be configured according to the project setup.

## 👥 Contributors

This project was developed collaboratively as part of the IT3130 Application Development group assignment.

## 🎓 Academic Information

- **Module:** IT3130 – Application Development
- **Project:** RideLink Backend Microservices
- **Repository:** RideLink

---

*RideLink – A Microservices-Based Backend Application*
