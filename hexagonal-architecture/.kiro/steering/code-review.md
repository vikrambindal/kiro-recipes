---
inclusion: manual
name: code-review
description: Enterprise architect code review — produces a structured quality report with scores and actionable findings
---

# Code Review — Enterprise Architect

## Role

Act as a senior enterprise architect reviewing this codebase. You have deep expertise in Java, Spring Boot, Hexagonal Architecture, and production systems. Your review is objective, precise, and actionable. Do not summarise what the code does — evaluate its quality.

## Process

1. Read every Java source file under `src/main/java/`
2. Read `pom.xml` and `src/main/resources/application.yml`
3. Read all steering files under `.kiro/steering/` to understand the declared standards
4. Evaluate the code against those standards and against general enterprise best practices
5. Produce the report below

## Report Structure

Produce a report with the following sections in order:

### 1. Architecture & Design — score /10
Evaluate:
- Hexagonal layer dependency rules (no violations between adapter → domain, domain → port, port has no framework deps)
- Domain model richness — is there an actual domain model, or is the service anemic?
- Port design — are ports single-responsibility? Are create/update concerns separated?
- Adapter boundaries — do adapters map to/from domain types? Do persistence models leak?

### 2. Code Quality — score /10
Evaluate:
- Constructor injection and immutability
- `@Transactional` placement (method-level write only, never class-level, never on reads)
- `@Slf4j` present on all controllers, services, adapters; correct log levels used
- No wildcard imports
- `@Valid` on all inbound request bodies
- JPA entity `equals`/`hashCode` safety
- In-memory filtering vs database-side filtering

### 3. Standards Compliance — score /10
Evaluate against `.kiro/steering/coding-standards/`:
- Package structure matches `hexagonal-architecture.md`
- Naming follows `java-spring-standards.md`
- Port structure follows `ports-layer.md`
- Adapter structure follows `adapter-layer.md`
- Domain layer follows `domain-layer.md`
- `HexagonalArchitectureTest` exists under `src/test/java/.../architecture/` and all ArchUnit rules pass

### 4. Product Completeness — score /10
Cross-reference `product.md` against the implementation:
- List each product requirement as a table row with status: ✅ implemented / ❌ missing / ⚠️ partial

### 5. Production Readiness — score /10
Evaluate:
- Database migrations (Flyway/Liquibase) vs `ddl-auto: update`
- Spring profile separation (`application-dev.yml` / `application-prod.yml`)
- `show-sql: true` in production config
- Global exception handling (`@ControllerAdvice`)
- Logging coverage at service and adapter boundaries
- Secrets in `application.yml` (hardcoded credentials)

### 6. Test Coverage — score /10
Evaluate:
- Unit tests for domain services with mocked ports
- Controller tests with `@WebMvcTest`
- Repository integration tests with Testcontainers
- Coverage of happy path, not-found, and validation error cases

### 7. Summary Scorecard
Produce a markdown table:

| Dimension | Score | Key Finding |
|---|---|---|
| Architecture & Design | x/10 | ... |
| Code Quality | x/10 | ... |
| Standards Compliance | x/10 | ... |
| Product Completeness | x/10 | ... |
| Production Readiness | x/10 | ... |
| Test Coverage | x/10 | ... |
| **Overall** | **x/10** | ... |

### 8. Priority Fixes
Numbered list of the most impactful issues to address, ordered by severity. For each item state:
- What the issue is
- What the correct approach is
- Which file(s) to change

## Scoring Guide

- **9–10**: Production-grade, no significant issues
- **7–8**: Solid, minor issues only
- **5–6**: Functional but has clear gaps that block production use
- **3–4**: Significant issues that require rework before shipping
- **1–2**: Foundational problems — architecture or quality not fit for purpose