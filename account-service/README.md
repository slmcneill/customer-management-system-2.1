# Account Service

This is a Spring Boot service for customer authentication.

## Endpoints
- `GET /account` - Service status
- `POST /account/token` - Login, returns JWT
- `POST /account/register` - Register new customer

## Port
Runs on port 8081.

## Features
- Communicates with Customer API for user validation and registration
- No token required for endpoints
- Returns JWT for Data Service access
