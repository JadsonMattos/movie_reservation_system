# Movie Reservation System

Backend system for a movie theater seat reservation service. Users can sign up, log in, browse movies and showtimes, reserve seats, and manage reservations. Admins can manage content (genres, movies, theaters, showtimes) and access reports on capacity and revenue.

**Architecture:** DDD + Clean Architecture (see [ARCHITECTURE.md](ARCHITECTURE.md))  
**Technology choices:** See [TRADEOFF.md](TRADEOFF.md)

---

## Features

- **User authentication:** Sign up, login, JWT-based sessions
- **Role-based access:** Admin and regular user roles
- **Movie catalog:** Genres, movies with descriptions and poster images
- **Theaters & seats:** Theater management with seat layout
- **Showtimes:** Movie sessions with date, time, and pricing
- **Reservations:** Seat selection, availability check, cancel upcoming reservations
- **Admin reports:** Capacity per showtime, revenue by movie/theater
- **Optional Redis:** Distributed seat locking to reduce overbooking risk

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Runtime |
| Spring Boot 3 | Framework |
| Spring Security | Auth (JWT) |
| PostgreSQL | Primary database |
| JPA/Hibernate | ORM |
| Flyway | Database migrations |
| Redis (optional) | Seat lock during reservation |
| springdoc-openapi | API documentation |
| Testcontainers + JUnit 5 | Integration tests |
| Docker Compose | Local dev environment |
| GitHub Actions | CI/CD |

---

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker and Docker Compose (for PostgreSQL and Redis)

---

## Quick Start

```bash
# Start PostgreSQL and Redis
docker compose up -d

# Run the application
./mvnw spring-boot:run

# Run with dev profile (SQL logging)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## API

- **Base URL:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI spec:** http://localhost:8080/api-docs

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/signup` | User registration |
| `POST` | `/api/auth/login` | Login (returns JWT) |
| `GET` | `/api/movies` | List movies (public) |
| `GET` | `/api/movies/genres` | List genres (public) |
| `GET` | `/api/movies/{id}` | Get movie by id (public) |
| `GET` | `/api/movies/by-date?date=YYYY-MM-DD` | Movies with showtimes for date (public) |
| `GET` | `/api/reservations/showtimes/{id}/seats` | Seat availability |
| `POST` | `/api/reservations` | Create reservation (auth required) |
| `GET` | `/api/reservations/me` | My reservations |
| `DELETE` | `/api/reservations/{id}` | Cancel reservation |
| `PATCH` | `/api/admin/users/{id}/role` | Promote user role (admin) |
| CRUD | `/api/admin/genres` | Manage genres |
| CRUD | `/api/admin/movies` | Manage movies |
| CRUD | `/api/admin/theaters` | Manage theaters and seats |
| CRUD | `/api/admin/showtimes` | Manage showtimes |
| `GET` | `/api/admin/reports/*` | Capacity and revenue reports |

---

## Default Admin

On first run, an admin user is created:

- **Email:** admin@example.com
- **Password:** admin123

> **Change this password in production.**

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5432 | PostgreSQL port |
| `DB_NAME` | movie_reservation | Database name |
| `DB_USER` | postgres | Database user |
| `DB_PASSWORD` | postgres | Database password |
| `REDIS_ENABLED` | false | Enable Redis for seat locking |
| `REDIS_HOST` | localhost | Redis host (when enabled) |
| `REDIS_PORT` | 6379 | Redis port |
| `JWT_SECRET` | (dev default) | JWT signing key (min 256 bits) |
| `JWT_EXPIRATION_MS` | 86400000 | Token expiration (ms) |
| `SERVER_PORT` | 8080 | Application port |

---

## Development

```bash
# Run tests
./mvnw test

# Package
./mvnw package

# Run with custom profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Integration tests use Testcontainers and require Docker. If Docker is unavailable, they are skipped automatically.

---

## Project Structure

```
src/main/java/com/moviereservation/
├── domain/          # Domain models and ports
├── application/     # Use cases, DTOs
├── infrastructure/  # Persistence, security adapters
├── controller/      # REST controllers
├── config/          # Spring configuration
├── entity/          # JPA entities
└── repository/      # Spring Data JPA repositories
```

For a detailed description of the layered architecture, see [ARCHITECTURE.md](ARCHITECTURE.md).

https://roadmap.sh/projects/movie-reservation-system
