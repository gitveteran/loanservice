
# Loan Service

**Loan Service** is a Spring Boot application designed to manage loan operations and integrate with third-party loan providers, including Credit and Stripe APIs. It features REST endpoints for managing loans, profile-based configuration, and robust testing support.

---

## Features
- **Loan Management**: Create, retrieve, and manage loan records.
- **Third-Party Integration**: Integrates with external APIs for loan processing.
- **Profile-Based Configuration**: Supports environment-specific configurations using Spring profiles.
- **Testing**: Comprehensive unit and integration tests with a dedicated `test` profile.

---

## Prerequisites
To build and run the application, ensure you have the following installed:
- **Java**: 17
- **Java Spring**: 3.4
- **Postgresql**
---

## Getting Started

### Clone the Repository
```bash
git clone <https://github.com/gitveteran/loanservice>
cd loanservice
```

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

---

## Configuration

### Default Profiles
- **`application.properties`**: Default configurations.
- **`application-test.properties`**: Configurations for testing.

#### `application-test.properties` Example:
```properties
# Server Configuration
server.port=5000

# PostgreSQL Test Database
spring.datasource.url=jdbc:postgresql://localhost:5432/test_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop

# Third-Party APIs
loanservice.thirdparty.credit.url=http://localhost:5000/thirdparty/api/credit
loanservice.thirdparty.stripe.url=http://localhost:5000/thirdparty/api/stripe
```

### Running with Profiles
- **Default**: Run without additional arguments for the default profile.
- **Test**: Use the `test` profile for integration tests.
  ```bash
  mvn test -Dspring.profiles.active=test
  ```

## Testing

### Run Tests
To run all tests, use:
```bash
mvn test
```

## Known Issues
- The `test_db` must be manually created in PostgreSQL before running integration tests. Alternatively, use an in-memory database like H2.
