---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Domain Layer

- Place all business logic in `core/domain/` — this is the application's core
- Services implement `port/in` interfaces and declare `port/out` interfaces as constructor dependencies
- Never import Spring Web, JPA, or any adapter-layer type in the domain
- Domain entities live in `core/domain/` — they model business concepts, not persistence structure
- Use `core/converter/` for mapping between domain models and DTOs or port types
- DTOs in `core/dto/` define the external data contract — domain entities are never exposed directly