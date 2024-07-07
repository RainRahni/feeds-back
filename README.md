# Feeds back-End

This is a backend application for managing RSS feeds. The application allows you to request filters,
edit and delete them. On every hour's first minute runs a cron job, that updates all the feeds in the database.

## Requirements

- Spring Boot 3.3
- Java 21
- Liquibase
- PostgreSQL
- MapStruct 1.5.5

## Getting Started

1. Clone the repository

2. Add H2 database dependency to run project locally.

3. Navigate into the project directory

4. Run the application:
    - ./gradlew bootrun

* Database is hosted on render.com. For development purposes i used H2 database.
Deployed website's backend takes ~5 minutes to start, because render shuts it down due to inactivity.

Springdoc: http://localhost:8080/swagger-ui/index.html#/

