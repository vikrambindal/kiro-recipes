---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Lombok Conventions

## Usage
- Use Lombok annotations consistently across all Hexagonal architecture layers to reduce boilerplate code
- Apply to all layers: `adapter/in`, `adapter/out`, `core/domain`

## Layer-Specific Guidelines

### Adapter Layer
- **Incoming adapters (REST controllers)**: Use `@RequiredArgsConstructor` for dependency injection
- **Outgoing adapters (repositories)**: Use `@RequiredArgsConstructor` for dependency injection
- Add `@Repository`, `@RestController` as appropriate

### Core Layer
- **Services**: Use `@RequiredArgsConstructor` for dependency injection
- Add `@Service` annotation for Spring beans

### Port Layer
- **Interfaces**: Records inside interfaces use Java record syntax (no Lombok needed)
- **Request/Response records**: Defined inline in interfaces

### Entity Layer
- Use `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` on entities

## Annotation Reference
| Annotation | Purpose | Applicable Layers |
|------------|---------|-------------------|
| `@Data` | Generates getters, setters, toString, equals, hashCode | Entity, DTO |
| `@NoArgsConstructor` | Generates no-args constructor | Entity, DTO |
| `@AllArgsConstructor` | Generates constructor with all fields | Entity, DTO |
| `@RequiredArgsConstructor` | Generates constructor with final fields | Controller, Repository, Service |
| `@Service` | Marks class as Spring service bean | Service |
| `@Repository` | Marks class as Spring repository bean | Repository |
| `@RestController` | Marks class as REST controller | Controller |