# Technology Stack

## Build System & Language

- Language: Java 17+
- Framework: Spring Framework 6.x
- Build Tool: Maven
- Database: PostgreSQL (via Spring Data JPA)
- Utilities: Lombok, Mapstruct
- Testing: Mockito, JUnit 5, Testcontainers
- API Documentation: Springdoc OpenAPI (Swagger)

## Common Commands

| Command | Purpose |
|---------|---------|
| `mvn clean package` | Build the project |
| `mvn test` | Run unit tests |
| `mvn spring-boot:run` | Run development server |
| `mvn test -Dtestcontainers.enabled=true` | Run tests with Testcontainers |

## Notes

- Domain layer should be independent of framework concerns
- Prefer composition over inheritance
- Use constructor injection for dependencies