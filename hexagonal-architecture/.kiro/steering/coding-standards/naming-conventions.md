---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Naming Conventions

| Component | Naming Pattern | Example |
|-----------|---------------|---------|
| Entity | `DomainEntity` | `TodoEntity` |
| Read Repository | `DomainReadRepository` | `TodoReadRepository` |
| Write Repository | `DomainWriteRepository` | `TodoWriteRepository` |
| Port Interface (in) | `DomainUseCase` | `CreateTodoUseCase` |
| Port Interface (out) | `DomainUseCase` | `SaveTodoUseCase` |
| Service | `DomainService` | `TodoService` |
| Repository JPA | `DomainJpaRepository` | `TodoJpaRepository` |