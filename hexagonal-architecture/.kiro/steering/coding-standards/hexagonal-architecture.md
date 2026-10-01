---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Hexagonal Architecture

## Package Structure

```
src/main/java/com/kirotodo/
├── adapter/
│   ├── in/{type}/               # Incoming adapters: controller, kafka
│   └── out/{type}/              # Outgoing adapters: database, cache, messaging
│       ├── entity/              # Persistence entities — never cross this boundary
│       ├── reader/              # Read-side adapter implementations
│       └── writer/              # Write-side adapter implementations
├── configuration/               # Spring bean wiring and app configuration
├── core/
│   ├── domain/                  # Business services and domain models
│   ├── converter/               # Mapping between domain models and DTOs
│   └── dto/                     # Data Transfer Objects for external contracts
└── port/
    ├── in/{domain}/             # Inbound use case interfaces (driven by adapters/in)
    └── out/{domain}/            # Outbound dependency interfaces (implemented by adapters/out)
```

## Dependency Rules

- `adapter/in` depends on `port/in` — never on `core/domain` directly
- `core/domain` depends on `port/out` — never on `adapter/out` directly
- `adapter/out` implements `port/out` — never imports from `core/domain`
- `port/in` and `port/out` have no dependencies on any other layer
- Framework annotations (`@Service`, `@Repository`, `@RestController`) belong in adapters and services — never in ports

## Standards

- [Java & Spring Standards](java-spring-standards.md)
- [Ports Layer](hexagonal/ports-layer.md)
- [Adapter Layer](hexagonal/adapter-layer.md)
- [Domain Layer](hexagonal/domain-layer.md)