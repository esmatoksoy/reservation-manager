# 🏨 Reservation Manager

A full-stack hotel reservation management system built with **Spring Boot**. It allows customers to create and manage reservations, and provides an admin panel for hotel staff to oversee all bookings.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Environment Variables](#-environment-variables)

---

## ✨ Features

- **Reservation CRUD** – Create, read, update reservations with unique UUID reservation numbers
- **Guest Management** – Add multiple guests per reservation with detailed preferences
- **Room Upgrade** – Upgrade rooms with additional cost and confirmation popup
- **Email Notifications** – Automated emails for pending reservations and confirmation updates
- **Admin Panel** – Admin login with security; search reservations by customer phone number
- **Input Validation** – Phone number format (10 digits, numbers only), date restrictions (no past dates)
- **RabbitMQ Integration** – Message queue support for asynchronous processing
- **Data Seeding** – Sample data auto-loaded on startup (excluding admin accounts)
- **Database Indexing** – Optimized queries on reservation number and status fields

---

## 🛠 Tech Stack

| Layer              | Technology                      |
|--------------------|---------------------------------|
| Backend            | Java 21, Spring Boot 3.5        |
| Database           | PostgreSQL                      |
| ORM                | Spring Data JPA / Hibernate     |
| Security           | Spring Security                 |
| Messaging          | RabbitMQ (Spring AMQP)          |
| Email              | Spring Boot Mail                |
| Mapping            | MapStruct                       |
| Build              | Maven                           |
| Containerization   | Docker & Docker Compose         |
| Frontend           | React                           |

---

## 🏗 Architecture

src/main/java/com/esma/reservation/manager/
├── config/          # RabbitMQ and app configuration
├── controller/      # REST controllers (Admin, Reservation, RabbitTest)
├── dto/             # Data Transfer Objects (request/response)
├── exception/       # Custom exception handling
├── mapper/          # MapStruct mappers (Entity ↔ DTO)
├── migration/       # Data seeding / migration scripts
├── model/
│   ├── entity/      # JPA Entities (Reservation, Customer, Room, etc.)
│   └── type/        # Enums (ReservationStatus, FormStatus, Role)
├── repository/      # Spring Data JPA repositories
├── security/        # Security configuration
└── service/         # Business logic layer

---

## 📌 Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running

> **That's it!** Docker Compose will handle PostgreSQL, RabbitMQ, and the application itself.

---

## 🚀 Getting Started

### 1. Clone the Repository

git clone https://github.com/esmatoksoy/reservation-manager.git
cd reservation-manager

### 2. Configure Environment Variables

Create a `.env` file in the project root:

DB_PASSWORD=your_postgres_password
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

### 3. Build & Run

docker-compose up --build

### 4. Access the Application

| Service             | URL                       |
|---------------------|---------------------------|
| Application         | http://localhost:8090      |
| RabbitMQ Management | http://localhost:15672     |
| PostgreSQL          | localhost:5433             |

### Management Commands

# Run in detached mode
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f app

---

## 📡 API Endpoints

### Reservation Controller

| Method | Endpoint                       | Description                    |
|--------|--------------------------------|--------------------------------|
| GET    | `/api/reservations/{uuid}`     | Get reservation by UUID        |
| POST   | `/api/reservations`            | Create a new reservation       |
| PUT    | `/api/reservations/{uuid}`     | Update an existing reservation |

### Admin Controller

| Method | Endpoint                       | Description                    |
|--------|--------------------------------|--------------------------------|
| POST   | `/api/admin/login`             | Admin login                    |
| GET    | `/api/admin/reservations`      | Search reservations by phone   |

> *Exact paths may vary – check the controller classes for details.*

---

## 🗄 Database Schema

### Entities

| Entity                   | Description                                  |
|--------------------------|----------------------------------------------|
| **Reservation**          | Core booking record with UUID, status, dates |
| **Customer**             | Contact info (name, email, phone)            |
| **Room**                 | Room details and pricing                     |
| **ReservationGuest**     | Links guests to a reservation                |
| **ReservationGuestDetail** | Guest preferences and special requests     |
| **AdminAccount**         | Admin credentials and roles                  |

### Enums

- `ReservationStatus` – PENDING, CONFIRMED, etc.
- `FormStatus` – Tracks form completion state
- `Role` – User roles (ADMIN, etc.)

---

## 🔐 Environment Variables

| Variable           | Description              |
|--------------------|--------------------------|
| `DB_PASSWORD`      | PostgreSQL password      |
| `RABBITMQ_USERNAME`| RabbitMQ admin username  |
| `RABBITMQ_PASSWORD`| RabbitMQ admin password  |

---

## 📧 Email System

The application automatically:
1. Detects **PENDING** reservations on startup
2. Generates a unique URL with the reservation UUID
3. Sends a confirmation email to the customer
4. Updates reservation status to **CONFIRMED** after form submission

---

## 👩‍💻 Author

**Esma Toksoy**

---

## 📝 License

This project is for learning and internship purposes.
