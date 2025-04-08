# Classroom Allocation System API

A Spring Boot-based API for optimizing classroom allocation in educational institutions using CPLEX optimization.

## 🚀 Features

- Classroom management and allocation
- Course and class block scheduling
- Room attribute management
- User management system
- Group-based room organization
- Schedule optimization using CPLEX
- Conflict resolution

## 📋 Prerequisites

- Java 17
- Maven
- PostgreSQL
- IBM ILOG CPLEX Optimization Studio 22.1.0.0

## 🔧 Installation

1. Clone the repository
2. Configure CPLEX dependencies by running:
```bash
mvn install:install-file -Dfile='C:\Program Files\IBM\ILOG\CPLEX_Studio221\cplex\lib\cplex.jar' -DgroupId=cplex -DartifactId=cplex -Dversion='22.1.0.0' -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile='C:\Program Files\IBM\ILOG\CPLEX_Studio221\opl\lib\oplall.jar' -DgroupId=opl -DartifactId=opl -Dversion='22.1.0.0' -Dpackaging=jar -DgeneratePom=true
```
3. Configure your database connection in `application.properties`
4. Run the application using Maven:
```bash
mvn spring-boot:run
```
## 🛠️ Tech Stack
- Framework: Spring Boot 2.7.9
- Database: PostgreSQL
- ORM: Spring Data JPA
- Build Tool: Maven
- Optimization Engine: IBM ILOG CPLEX
- Other Tools:
  - Lombok
  - ModelMapper
  - Jackson
  - Spring DevTools

## 📦 Project Structure
```plaintext
src/main/java/com/tcc/backend/
├── blocoaula/          # Class block management
├── curso/              # Course management
├── exceptions/         # Custom exceptions
├── horarioaula/        # Class schedule management
├── sala/              # Classroom management
│   ├── atributosala/  # Room attributes
│   └── gruposala/     # Room groups
└── usuario/           # User management
 ```

## 🔍 Key Features Details
### Room Management
- Room attribute system for specific requirements (equipment, capacity, etc.)
- Room grouping functionality
- Capacity tracking and management
### Class Block Management
- Day of week scheduling
- Room attribute matching
- User responsibility assignment
### Course Management
- Course-user association
- Schedule management
- Resource allocation
## 🧪 Development
To run the application in development mode:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
 ```

## ✨ Contributors
- Mateus Vieira Freitas
- Gabriel Kuhnen Brylkowski