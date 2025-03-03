# TransPro - Transport Management System

## Project description
TransPro is an integrated fleet management system developed as part of my BSc Thesis. It streamlines vehicle fleet operations, load management, driver communication, and cost tracking. The system includes a web-based platform for dispatchers and administrators to manage routes, drivers, loads, and vehicles, as well as a mobile app for drivers to enable real-time communication and operational updates (e.g., load details, messaging). Built with Spring Boot for the backend and Thymeleaf + Tailwind CSS for the frontend, TransPro ensures a seamless workflow—from route creation to cost finalization.

## Features
- **Driver Management**: Add, edit, and delete driver records. Include license details, tacho card info, categories, etc.
- **Vehicle Fleet**: Manage various vehicles and trailers, associate them with drivers, view/update technical condition.
- **Route Planning**: Create, modify, and remove routes, specifying start/end locations, distance, and assigned driver/vehicle.
- **Load Management**: Add loads with pickup/delivery times, link them to routes/drivers, track status (pending, in progress, delivered).
- **Cost Tracking**: Keep records of expenses (e.g., fuel, tolls) with invoice details, amounts, driver, and vehicle associations.
- **Real-Time Communication**: Mobile chat for drivers to coordinate with dispatchers or other users, using WebSockets (SockJS & STOMP).
- **Authentication & Authorization**: Secure logins via JWT tokens (Spring Boot), with role-based access control.
- **Responsive Web UI**: DataTables-based interface for listing and filtering records, using modals for details/edits.
- **Mobile Driver App**: Built in React Native, providing chat, login, home screen, and potential expansions for route or load details.

## Aplikacja showcase
Below is a short demonstration the key functionalities of TransPro in action.  


https://github.com/user-attachments/assets/dec1fd54-8dee-494c-9102-ffca3a3fc834


---

## Full Project Documentation
For a detailed explanation of TransPro, including its architecture, implementation, and technical decisions, you can read my BSc Thesis:
[TransPro_Thesis.pdf](https://github.com/user-attachments/files/19061417/TransPro_Thesis.pdf)


## Technology used

### Backend
- **Java Spring Boot** (REST controllers, JPA repositories, security)
- **Spring Security** (JWT-based authentication)
- **Java Persistence API (JPA) with Hibernate** for ORM
- **H2** in test environment
- **PostgreSQL**

### Frontend
- **Thymeleaf** with **Tailwind CSS**
- **React Native** (Expo) for the mobile driver application
- **Axios** for HTTP requests
- **FullCalendar**
- **sweetalert2**

### Build and test tools
- **Maven** or **Gradle** for building the backend
- **npm** / **yarn** for the frontend
- **JUnit 5** and **Mockito** for unit tests
- **Spring Boot Test** with **MockMvc** for integration tests

---

## Installation Guide

### Prerequisites
- **Java 17+**
- **Node.js** (preferably 14+ or 16+)
- **npm** / **yarn**
- **Maven** or **Gradle**

### Backend Setup
1. Clone the repository.
2. Navigate to the backend directory:
   ```sh
   cd schedule backend
   ```
3. Set up your **application.properties** file for database connection:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/schedule_app
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Run the backend application:
   ```sh
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```sh
   cd schedule frontend
   ```
2. Install the dependencies:
   ```sh
   npm install
   ```
3. Start the frontend development server:
   ```sh
   npm start
   ```
4. Access the application at **http://localhost:3000**.
---

## Usage
Once the backend is running (e.g., on port 8080) and the web frontend is served (often on port 3000), open the web UI in a browser. The login screen (if integrated) will appear; after logging in, you can access routes, loads, drivers, vehicles, and costs modules. For the mobile driver app, install the Expo app on a device or use an Android/iOS emulator, then point it to the correct backend IP (e.g., `http://192.168.1.19:8080`).

---

## API Endpoints
TransPro exposes a series of REST endpoints for resources like:
- **Drivers**: `/api/drivers`, `/api/drivers/{id}`, `POST /api/drivers/add`, `PUT /api/drivers/edit/{id}`, `DELETE /api/drivers/delete/{id}`
- **Vehicles**: `/api/vehicles`, `/api/vehicles/{id}`, etc.
- **Loads**: `/api/loads`, `/api/loads/{id}`, ...
- **Routes**: `/api/routes`, `/api/routes/{id}`, ...
- **Costs**: `/api/costs`, `/api/costs/{id}`, ...
- **Auth**: `/api/auth/login`

---

## Development
Active development includes:
- **Refining** DataTables-based UI for drivers, vehicles, costs, loads, routes
- **Enhancing** mobile chat features with real-time route updates or driver schedules
- **Testing** with JUnit + Mockito + Spring Boot Test for integration, unit, and E2E coverage
- **Exploring** container-based DB setup (Testcontainers) to replicate production environment during CI

---


## Contact
For any questions or suggestions, feel free to contact the developer:
- **Email**: marcin.golonka21@gmail.com  
- **GitHub**: [TransProWeb Repository](https://github.com/Golonka-Ma/TransProWeb)  
- **LinkedIn**: [Marcin Golonka](https://www.linkedin.com/in/marcin-golonka-4510a928b/)  

### Test
Run `mvn test` to execute unit and integration tests. The in-memory database (H2) or a Docker container may be used automatically, ensuring minimal manual config.

### UniTest
Unit tests focus on individual service methods or repositories with `@MockBean` or `Mockito`.

### IntegrationTest
Integration tests spin up the entire Spring context, possibly an in-memory DB, to check the end-to-end flow from controllers to the real repository layer.


---


