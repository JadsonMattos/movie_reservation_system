# Architecture — DDD + Clean Architecture

The project follows **Domain-Driven Design (DDD)** and **Clean Architecture** principles, with layers organized so that dependencies point inward toward the domain.

## Layer Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE (Adapters)                     │
│  persistence/ │ controllers │ security/ │ config/                 │
└───────────────────────────┬─────────────────────────────────────┘
                            │ implements
┌───────────────────────────▼─────────────────────────────────────┐
│                    APPLICATION (Use Cases)                       │
│  port/in/ │ port/out/ │ service/ │ dto/ │ exception/             │
└───────────────────────────┬─────────────────────────────────────┘
                            │ depends on
┌───────────────────────────▼─────────────────────────────────────┐
│                    DOMAIN (Core)                                 │
│  model/ │ port/ │ exception/                                     │
└─────────────────────────────────────────────────────────────────┘
```

## Layers

### 1. Domain

**Responsibility:** Business logic, no framework dependencies.

- **`domain/model/`** — Domain entities, aggregates, value objects
  - `user/` — User, UserRole
  - `movie/` — Movie, Genre
  - `showtime/` — Showtime, Theater, Seat
  - `reservation/` — Reservation (aggregate root), ReservedSeat, ReservationStatus

- **`domain/port/`** — Output ports (interfaces for persistence and external services)
  - `UserRepositoryPort`, `MovieRepositoryPort`, `ShowtimeRepositoryPort`, etc.
  - `SeatLockPort` — Distributed seat locking

- **`domain/exception/`** — Domain exceptions

### 2. Application

**Responsibility:** Use cases and orchestration.

- **`application/port/in/`** — Input ports (use case contracts)
  - `AuthUseCase`, `ReservationUseCase`, etc.

- **`application/port/out/`** — Output ports for infrastructure
  - `TokenGeneratorPort`, `PasswordEncoderPort`

- **`application/service/`** — Use case implementations
  - `AuthService` (implements `AuthUseCase`), `GenreService`, etc.

- **`application/dto/`** — Request/response DTOs
  - `auth/`, `movie/`, `reservation/`, etc.

- **`application/exception/`** — Application exceptions
  - `ResourceNotFoundException`, `ConflictException`, etc.

### 3. Infrastructure

**Responsibility:** Adapters for frameworks and external systems.

- **`infrastructure/persistence/`**
  - `mapper/` — Domain ↔ JPA entity mapping
  - `repository/` — Port implementations (e.g., `UserRepositoryAdapter`)

- **`infrastructure/security/`**
  - `JwtTokenGeneratorAdapter` — `TokenGeneratorPort` implementation
  - `BcryptPasswordEncoderAdapter` — `PasswordEncoderPort` implementation

- **`infrastructure/adapter/`**
  - `NoOpSeatLockAdapter` — No-op lock when Redis is disabled
  - `RedisSeatLockAdapter` — Redis-based seat lock when enabled

- **`entity/`** — JPA entities (persistence model)

- **`controller/`** — REST controllers

- **`config/`** — Spring configuration

## Dependency Rule

- **Domain** does not depend on anything outside the domain.
- **Application** depends only on the **Domain**.
- **Infrastructure** depends on **Domain** and **Application**, implementing their ports.

## Request Flow Example (Login)

1. `AuthController` receives the request and calls `AuthUseCase.login()`.
2. `AuthService` (implements `AuthUseCase`) uses `UserRepositoryPort` and `TokenGeneratorPort`.
3. `UserRepositoryAdapter` implements `UserRepositoryPort` and uses the JPA repository.
4. `JwtTokenGeneratorAdapter` implements `TokenGeneratorPort` and uses `JwtService`.
5. The result is converted to `AuthResponse` and returned to the client.

## Migration Status

| Module | Status |
|--------|--------|
| Auth (signup, login) | ✅ Migrated |
| Admin Users (promote role) | ✅ Migrated |
| Genres | ✅ Migrated |
| Movies | ✅ Migrated |
| Theaters & Seats | ✅ Migrated |
| Showtimes | ✅ Migrated |
| Reservations | ✅ Migrated |
| Reports | ✅ Migrated |
| Seat Lock (Redis) | ✅ Migrated (NoOpSeatLockAdapter, RedisSeatLockAdapter) |

All modules use DDD + Clean Architecture.
