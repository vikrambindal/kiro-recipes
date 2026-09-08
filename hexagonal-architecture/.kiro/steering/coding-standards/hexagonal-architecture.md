---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "README*|*.md"
---

# Hexagonal Architecture (Ports and Adapters)

The project should follow a Hexagonal architecture with clear separation of concerns.

## Package Structure

```
src/main/java/com/kirotodo/
├── adapter/              # External system adapters
│   ├── in/              # Incoming adapters (REST controllers, CLI)
│   └── out/             # Outgoing adapters (database, APIs)
├── configuration/       # Spring configuration and bean wiring
├── core/                # Domain logic and business rules
│   ├── domain/          # Domain models and service implementations
│   ├── converter/       # Conversion logic
│   ├── utility/         # Utility classes
│   └── dto/             # Data Transfer Objects
└── port/                # Port interfaces
    ├── in/              # Incoming ports (used by adapter/in)
    └── out/             # Outgoing ports (used by adapter/out)
```

## Quick Reference

See the individual documentation files for detailed guidelines:
- [Ports Conventions](ports-conventions.md)
- [Adapters Conventions](adapters-conventions.md)
- [Services Conventions](services-conventions.md)
- [Lombok Conventions](lombok-conventions.md)
- [Naming Conventions](naming-conventions.md)
- [Request/Response Flow](request-response-flow.md)