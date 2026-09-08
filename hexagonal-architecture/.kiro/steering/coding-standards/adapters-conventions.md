---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Adapter Conventions

## Incoming Adapters (adapter/in)
- REST controllers, CLI handlers, message consumers
- Use port/in interfaces for business logic interaction
- Use `@RestController` and Spring annotations

### Database Adapters
```
adapter/out/database/
├── entity/              # Entity classes (no Jpa prefix)
│   └── TodoEntity.java
├── TodoJpaRepository.java  # Spring Data JPA repository
├── reader/              # Read operations
│   └── TodoReadRepository.java
└── writer/              # Write operations
    └── TodoWriteRepository.java
```

### Naming
- **Entity naming**: `TodoEntity`, `UserEntity` (no "Jpa", "Entity", "Db" prefixes)
- **Repository naming**: 
  - Read: `TodoReadRepository`
  - Write: `TodoWriteRepository`
  - Repository interface: `TodoJpaRepository` (Spring Data JPA)

## Service Implementation
- Service implementations go in `core/domain/` (e.g., `TodoService`)
- Services implement `port/in` interfaces
- Services use `port/out` interfaces to interact with adapters
- Services must be annotated with `@Service` (Spring bean)