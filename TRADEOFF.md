# Technology Tradeoffs

This document explains the main technology choices for the Movie Reservation System, the rationale behind them, and their tradeoffs.

---

## Java 21 + Spring Boot 3

**Choice:** Java 21 LTS with Spring Boot 3.2

**Why:**
- Java 21 provides virtual threads, improved GC, and long-term support.
- Spring Boot 3 runs on Jakarta EE 10 and requires Java 17+.
- Spring Boot simplifies configuration, dependency injection, and integration with databases, security, and web.

**Tradeoffs:**
- **Pro:** Mature ecosystem, good performance, strong typing.
- **Con:** Heavier than lightweight frameworks; migration from older Java/Spring versions can be costly.

---

## Spring Security + JWT

**Choice:** Spring Security for authentication and authorization; stateless JWT for session handling.

**Why:**
- Spring Security integrates well with Spring Boot and supports roles, method-level security (`@PreAuthorize`), and custom filters.
- JWT keeps the API stateless and works well for mobile/web clients.

**Tradeoffs:**
- **Pro:** Stateless, scalable, no server-side session store.
- **Con:** JWT revocation is harder; logout typically handled client-side (token discard). For strict revocation, a blacklist or short-lived tokens + refresh tokens are needed.

**Alternatives considered:** OAuth2/OIDC (more complex), session cookies (requires sticky sessions or shared session store).

---

## PostgreSQL

**Choice:** PostgreSQL as the primary database.

**Why:**
- ACID compliance for reservations and concurrent access.
- Rich feature set (JSON, full-text search, partitioning).
- Strong ecosystem and tooling.

**Tradeoffs:**
- **Pro:** Reliable, well-known, good for relational data.
- **Con:** May require read replicas or sharding for very high scale.

**Alternatives considered:** MySQL (similar; different ecosystem), MongoDB (weaker fit for relational reservation model).

---

## JPA/Hibernate

**Choice:** Spring Data JPA with Hibernate.

**Why:**
- Reduces boilerplate and keeps persistence logic declarative.
- Integrates cleanly with Spring Boot, Flyway, and Testcontainers.

**Tradeoffs:**
- **Pro:** Fast development, abstraction over SQL, automatic schema handling with Flyway.
- **Con:** Complex queries or large datasets may need native SQL or projections; N+1 and lazy loading require care.

**Alternatives considered:** jOOQ (more SQL control, better performance), MyBatis (explicit SQL mapping).

---

## Flyway

**Choice:** Flyway for database migrations.

**Why:**
- Versioned, ordered migrations (`V1__`, `V2__`, etc.).
- Integrates with Spring Boot and supports PostgreSQL.
- Migrations are plain SQL, easy to review.

**Tradeoffs:**
- **Pro:** Clear history, repeatable deployments, no ORM-generated schema drift.
- **Con:** Requires discipline; rollbacks need explicit down migrations.

**Alternatives considered:** Liquibase (XML/YAML-based, more flexible but heavier).

---

## Redis (Optional)

**Choice:** Redis for distributed seat locking during reservation, with a no-op implementation when disabled.

**Why:**
- Reduces race conditions when multiple users try to reserve the same seat.
- `SET NX` (or equivalent) provides atomic locks.
- Optional so the app can run without Redis in dev or small deployments.

**Tradeoffs:**
- **Pro:** Less overbooking risk, better concurrency handling.
- **Con:** Extra infrastructure; uniqueness still enforced by DB constraints as a fallback.

**Alternatives considered:** DB advisory locks (simpler but more DB load), in-memory locks (not distributed).

---

## springdoc-openapi

**Choice:** springdoc-openapi for OpenAPI 3 and Swagger UI.

**Why:**
- Generates spec from annotations and Javadoc.
- Swagger UI provides interactive documentation.
- No need for hand-written OpenAPI files.

**Tradeoffs:**
- **Pro:** Documentation stays in sync with code, easy for API consumers.
- **Con:** Annotations can clutter controllers if overused.

**Alternatives considered:** Springfox (deprecated), manual OpenAPI YAML.

---

## Testcontainers + JUnit 5

**Choice:** Testcontainers for integration tests with real PostgreSQL.

**Why:**
- Tests run against a real database instead of H2, reducing surprises in production.
- JUnit 5 supports conditional test execution (e.g., skip when Docker is unavailable).

**Tradeoffs:**
- **Pro:** High confidence, realistic environment.
- **Con:** Slower runs, requires Docker; CI usually provides it.

**Alternatives considered:** H2 (faster but different dialect), embedded PostgreSQL (less common).

---

## Docker Compose

**Choice:** Docker Compose for local development (PostgreSQL, Redis).

**Why:**
- Single `docker compose up -d` to start dependencies.
- Matches production-like setup locally.

**Tradeoffs:**
- **Pro:** Reproducible, easy onboarding.
- **Con:** Requires Docker installed locally.

---

## GitHub Actions

**Choice:** GitHub Actions for CI/CD.

**Why:**
- Integrated with GitHub, easy to trigger on push/PR.
- Supports Java, Maven, and Docker.

**Tradeoffs:**
- **Pro:** No separate CI server, good for open source and small teams.
- **Con:** Minutes limits on free tier; complex pipelines may need paid plans.

---

## DDD + Clean Architecture

**Choice:** Domain-Driven Design and Clean Architecture for structure.

**Why:**
- Separates business rules from infrastructure.
- Domain and application logic can be tested without frameworks.
- Easier to swap databases, security, or external services.

**Tradeoffs:**
- **Pro:** Testability, maintainability, clearer boundaries.
- **Con:** More classes and mappers; can feel heavy for very small projects.

**Alternatives considered:** Anemic domain model with services only (simpler but weaker domain encapsulation).
