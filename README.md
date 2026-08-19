Portfolio Tracker

A wealth-management REST API built with Java and Spring Boot, inspired by real-world TCS BaNCS domain experience. Advisors manage clients, portfolios, and holdings; record BUY/SELL transactions that keep each holding's weighted-average cost accurate; and rely on a nightly batch job that revalues every holding and refreshes cached prices. Access is protected by JWT authentication with role-based permissions for advisors and clients.

Features
Client management — full CRUD for clients (name, unique email, phone), with a clear 404 response when a client isn't found.
Portfolio management — full CRUD for portfolios, each linked to a client, plus lookup of all portfolios belonging to a given client.
Holding management — track instrument holdings per portfolio (quantity, average buy price, current price) and retrieve a portfolio's live total value.
BUY/SELL transaction engine — a BUY into an existing holding recalculates the weighted-average buy price (oldQty·oldAvg + newQty·newPrice) / (oldQty + newQty); a SELL reduces the quantity and removes the holding entirely once it reaches zero.
Insufficient-holdings validation — a SELL for more units than are held is rejected with a 400 Bad Request instead of producing a negative position.
Nightly NAV revaluation — a Spring Batch job runs every night at midnight, reading holdings in chunks of 10, applying a new current price, and writing the result back to the database.
Redis price caching — instrument prices are cached in Redis so portfolio-value lookups and the batch job can read prices without hitting the database each time.
JWT authentication — protected endpoints require a valid JSON Web Token, verified on every request by a custom security filter. Passwords are hashed with BCrypt before storage.
Role-based access — ADVISOR accounts manage clients, portfolios, and transactions; holdings are readable by both ADVISOR and CLIENT roles.
Global exception handling — not-found and validation errors return clean, consistent responses with appropriate HTTP status codes instead of raw stack traces.
Tech Stack
Language: Java 17
Framework: Spring Boot 3.5
Security: Spring Security, JWT (jjwt 0.11.5), BCrypt
Data: Spring Data JPA / Hibernate, MySQL
Batch: Spring Batch (chunk-based nightly job) with Spring Scheduling
Caching: Redis (Spring Data Redis)
Build: Maven
Utilities: Lombok
Architecture

The application follows a layered architecture:

Controller layer — receives HTTP requests and returns responses (ClientController, PortfolioController, HoldingController, TransactionController, AuthController).
Service layer — contains the business logic: transaction execution and weighted-average recalculation, portfolio-value computation, and authentication.
Repository layer — talks to MySQL through Spring Data JPA, with derived queries such as findByClientId, findByPortfolioId, and findByPortfolioIdAndInstrumentName.
Security layer — a custom OncePerRequestFilter reads the JWT from the Authorization header and sets the authenticated user in Spring's SecurityContext; SecurityConfig then decides which endpoints are public and which require a given role.
Batch layer — NavRecalculationJob defines the reader/processor/writer step, and NavRecalculationScheduler triggers it on a nightly cron.

Instrument prices are cached in Redis. The nightly batch job writes fresh prices into the cache as it revalues each holding, so portfolio-value reads stay fast and consistent between runs.

API Endpoints
Method	Endpoint	Role	Description
POST	/api/auth/register	Public	Register a new user
POST	/api/auth/login	Public	Log in and receive a JWT
POST	/api/clients	ADVISOR	Create a client
GET	/api/clients	ADVISOR	List all clients
GET	/api/clients/{id}	ADVISOR	Get a client by ID
PUT	/api/clients/{id}	ADVISOR	Update a client
DELETE	/api/clients/{id}	ADVISOR	Delete a client
POST	/api/portfolios	ADVISOR	Create a portfolio
GET	/api/portfolios	ADVISOR	List all portfolios
GET	/api/portfolios/{id}	ADVISOR	Get a portfolio by ID
GET	/api/portfolios/client/{clientId}	ADVISOR	List a client's portfolios
PUT	/api/portfolios/{id}	ADVISOR	Update a portfolio
DELETE	/api/portfolios/{id}	ADVISOR	Delete a portfolio
POST	/api/transactions	ADVISOR	Execute a BUY or SELL transaction
GET	/api/transactions/portfolio/{portfolioId}	ADVISOR	List a portfolio's transactions
GET	/api/transactions/{id}	ADVISOR	Get a transaction by ID
GET	/api/holdings/portfolio/{portfolioId}	ADVISOR / CLIENT	List a portfolio's holdings
GET	/api/holdings/{id}	ADVISOR / CLIENT	Get a holding by ID
GET	/api/holdings/portfolio/{portfolioId}/value	ADVISOR / CLIENT	Get a portfolio's current total value

For protected endpoints, include the token in the request header:

Authorization: Bearer <your-token>
Getting Started
Prerequisites
Java 17
Maven
MySQL running locally
Redis running locally (e.g. via Docker: docker run -d -p 6379:6379 redis)
Setup
Clone the repository:
git clone https://github.com/UshaM22/portfolio-tracker.git
cd portfolio-tracker
Create a MySQL database:
sql
CREATE DATABASE portfolio_db;
Create src/main/resources/application.properties with your own credentials. This file is intentionally gitignored, so it is not included in the repository:
properties
spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

spring.data.redis.host=localhost
spring.data.redis.port=6379

# Keep the nightly batch job from running on every startup
spring.batch.job.enabled=false
Run the application:
./mvnw spring-boot:run

The API will be available at http://localhost:8080.

Running Tests
./mvnw test
Roadmap
Swagger / OpenAPI — interactive API documentation (config class scaffolded, not yet wired up).
Externalized JWT secret — move the signing key out of source and into configuration.
Expanded test coverage — unit tests for the transaction engine (weighted-average and insufficient-holdings paths) and the batch job.
Docker & cloud deployment — containerize the app and its MySQL/Redis dependencies.
Author

Backend Developer — Berlin, Germany GitHub: github.com/UshaM22