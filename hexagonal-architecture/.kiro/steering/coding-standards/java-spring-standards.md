---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Java & Spring Standards

## Java

- No wildcard imports — always use explicit class names
- PascalCase for classes and interfaces; camelCase for methods and variables; UPPER_SNAKE_CASE for constants
- Use `final` on fields and local variables wherever possible
- Use Java records for immutable data carriers — DTOs, port Request/Response types
- Throw unchecked exceptions for domain errors — never declare checked exceptions on domain methods
- One class per file; one responsibility per class

## Spring

- Constructor injection only — never field or setter injection; use `@RequiredArgsConstructor` with `final` fields
- Apply the correct stereotype per layer: `@RestController`, `@Service`, `@Repository`, `@Configuration`
- Add `@Slf4j` to all controllers, services, and repository adapters — use `log.info` for state-changing operations, `log.debug` for queries, `log.warn` for rejected business rules, `log.error` for unexpected failures
- `@Transactional` on write service methods only — never on read-only operations or controllers
- All REST endpoints versioned under `/api/v1/`
- Controllers return `ResponseEntity` — never raw objects
- Validate all inbound request bodies with `@Valid`
- All exception handling via `@ControllerAdvice` — never swallow exceptions in controllers or services
- All configuration externalised to `application.yml`; use `@ConfigurationProperties` for structured binding
- Never expose JPA entities outside `adapter/out` — map to domain models or DTOs at the adapter boundary