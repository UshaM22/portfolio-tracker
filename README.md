# Portfolio Tracker System

A production-grade wealth management REST API built with Java and Spring Boot, 
inspired by real-world TCS BaNCS domain experience.

## Tech Stack
- Java 17, Spring Boot 3.5
- Spring Data JPA / Hibernate, MySQL
- Spring Batch — nightly portfolio revaluation
- Redis — instrument price caching
- Spring Security + JWT — role-based access (in progress)
- Docker

## Features Built
- Client management — CRUD
- Portfolio management — CRUD, find by client
- Holding management — real-time NAV calculation using Java Streams
- BUY/SELL transaction engine — weighted average price recalculation
- Insufficient holdings validation
- Spring Batch nightly job — price recalculation in chunks of 10
- Redis caching for instrument prices

## Features In Progress
- Spring Security + JWT — ADVISOR and CLIENT roles
- Swagger documentation
- Docker + cloud deployment

## How to Run
1. Make sure MySQL and Redis are running locally
2. Create database: `CREATE DATABASE portfolio_db;`
3. Run the app from IntelliJ

## Author
Backend Developer — Berlin, Germany  
GitHub: github.com/UshaM22
