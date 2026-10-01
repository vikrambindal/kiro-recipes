---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Adapter Layer

## Incoming (`adapter/in/{type}/`)

- Each class handles one entry point — REST controller, event consumer, CLI command
- Inject and call `port/in` interfaces only — no direct service or repository references
- No business logic — delegate entirely to the domain via ports
- Map request payloads to port `Request` records and port `Response` records back to HTTP responses

## Outgoing (`adapter/out/{type}/`)

- Each class implements one or more `port/out` interfaces
- Separate read and write responsibilities:
  - `{Domain}ReadRepository` implements read `port/out` interfaces
  - `{Domain}WriteRepository` implements write `port/out` interfaces
  - `{Domain}JpaRepository` extends Spring Data `JpaRepository` — used only within the adapter
- Place JPA entities in `adapter/out/{type}/entity/` — never reference them outside the adapter package
- Map JPA entities to domain models or port `Response` records before returning — never leak persistence types