# User Registration Module - Course Management Platform

RESTful API developed in Java with Spring Boot to manage user registration, authentication, and validation for the Course and Training Management Platform of Véridia.

### ✒️ Description

The city of Véridia aims to digitalize and organize the course and training offerings from public and private institutions. This module is responsible for the user registration layer of the platform, including registration of students, instructors, and administrators with validation and authentication features.

This registration module provides user management functionality including registration, authentication, profile updates, and data validation. The application follows REST API development best practices using Spring Boot's layered architecture.

### ✨ Features

- [x] Creating new users (students, instructors, administrators)
- [x] User data validation (email, CPF, phone number, and other fields)
- [ ] User authentication and login
- [ ] Reading users (by ID and retrieve all users)
- [x] Updating existing user data
- [x] Deleting users
- [x] Data persistence in repository
- [ ] Login/Registration interface development (FRONT)
- [ ] Comprehensive testing

### ⚙️ Architecture
The application is structured following the layered architecture pattern:

- Controller Layer: Handles HTTP requests and responses
- Service Layer: Contains business logic and validation rules
- Repository Layer: Manages data persistence and database operations
- Model Layer: Defines the User entity and data structures

### 💻 Technologies used

* **Language:** Java 17
* **Framework:** Spring Boot 3
* **Build:** Maven / Gradle
* **DataBase:** PostgreSQL
* **API:** RESTful
* **Frontend** React, Tailwind, Shadcn

### 👨‍💻 Components
GROUP 01 - CLASS N02
