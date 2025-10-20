# User Registration Module - Course Management Platform

RESTful API developed in Java with Spring Boot to manage user registration, authentication, and validation for the Course and Training Management Platform of Véridia.

### ✒️ Description

The city of Véridia aims to digitalize and organize the course and training offerings from public and private institutions. This module is responsible for the user registration layer of the platform, including registration of students, instructors, and administrators with validation and authentication features.

This registration module provides user management functionality including registration, authentication, profile updates, and data validation. The application follows REST API development best practices using Spring Boot's layered architecture. 

### ✨ Features

- [x] Creating new users (students, instructors, administrators)
- [x] User data validation (email, CPF, phone number, and other fields)
- [ ] User authentication and login
- [x] Reading users (by ID and retrieve all users)
- [x] Updating existing user data
- [x] Deleting users
- [x] Data persistence in repository
- [x] CORS config
- [x] Login/Registration interface development (FRONT)
- [x] Login/Registration fetch (FRONT)
- [ ] Comprehensive testing

### ⚙️ Architecture
The application is structured following the layered architecture pattern:

- Controller Layer: Handles HTTP requests and responses
- Service Layer: Contains business logic and validation rules
- Repository Layer: Manages data persistence and database operations
- Model Layer: Defines the User entity and data structures

### 👨‍💻 Frontend Repository
<table>
  <tr>
    <td valign="middle">
      <a href="https://github.com/EricMariano/projeto_de_programacao_n02_grupo1_front.git" target="_blank">
      <img src="https://www.svgrepo.com/show/484158/web-page-browser-window.svg" height="50" width="50" alt="Web page icon">
      </a>
      </td>
      <td valign="middle">
      <a href="https://github.com/EricMariano/Projeto_de_Programacao_N02_Grupo1_Front.git" target="_blank">
      Check out the application frontend
      </a>
    </td>
  </tr>
</table>

### 🚀 How to Run the Project

There are two main ways to run this application: using Docker (recommended) or locally.

#### 1. Using Docker (Recommended)

This is the simplest method, as the `docker-compose.yml` will handle the configuration of both the PostgreSQL database and the API.

**Prerequisites:**
* Docker
* Docker Compose

**Steps:**

1.  Clone this repository:
    ```bash
    git clone https://github.com/Pedronovaesdev/Projeto_de_Programacao_N02_Grupo1
    ```

2.  Navigate to the project root:
    ```bash
    cd Projeto_de_Programacao_N02_Grupo1
    ```

3.  Bring up the containers using Docker Compose:
    ```bash
    docker-compose up -d
    ```
    * This will build the API image and start a container for the application (`spring_api`) and one for the database (`postgres_db`).

4.  The application will be available at `http://localhost:8080`.

#### 2. Running Locally (Development)

This method requires you to set up the environment (Java and PostgreSQL) manually.

**Prerequisites:**
* Java JDK 21 (As per `build.gradle` and `Dockerfile`).
* A running PostgreSQL 14 (or compatible) server.

**Steps:**

1.  Clone the repository and navigate to the project root (see steps 1 and 2 above).

2.  Configure your local PostgreSQL database to have:
    * Database: `user_db`
    * User: `admin`
    * Password: `admin`

3.  Create an `application.properties` or `application.yml` file in `src/main/resources/`. (This file is ignored by `.gitignore`). Add the connection settings (assuming the database is running on `localhost:5432`):

    *Example (`application.properties`):*
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/user_db
    spring.datasource.username=admin
    spring.datasource.password=admin
    spring.jpa.hibernate.ddl-auto=update
    ```

4.  Run the application using the Gradle Wrapper (which uses Spring Boot):

    *On Linux/macOS:*
    ```bash
    ./gradlew bootRun
    ```
    *On Windows:*
    ```bash
    gradlew.bat bootRun
    ```
5.  The application will be available at `http://localhost:8080`.

### 💻 Technologies used

* **Language:** Java 17
* **Framework:** Spring Boot 3
* **Build:** Maven / Gradle
* **DataBase:** PostgreSQL
* **API:** RESTful
* **Frontend** React, Tailwind, Shadcn

### 👨‍💻 Components
GROUP 01 - CLASS N02
