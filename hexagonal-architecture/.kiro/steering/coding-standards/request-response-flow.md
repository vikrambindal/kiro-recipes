---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Request/Response Flow

The request flows through the Hexagonal architecture layers as follows:

```
┌─────────────────────────────────────────────────────────────┐
│                      adapter/in                             │
│                   (REST Controller)                         │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ Uses port/in interface
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                       core/domain                           │
│                    (Service)                                │
│              Implements port/in, uses port/out              │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ Uses port/out interface
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                      adapter/out                            │
│               (Database Repository)                         │
│                Implements port/out                          │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ Persists to database
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                      database                               │
│                    (PostgreSQL)                             │
└─────────────────────────────────────────────────────────────┘
```

## Flow Steps

1. **adapter/in/Controller** uses `port/in/UseCase` interface
2. **core/domain/Service** implements `port/in/UseCase` and uses `port/out/UseCase`
3. **core/domain/Service** uses `port/out/UseCase` interface
4. **adapter/out/.../WriteRepository** implements `port/out/UseCase`
5. **adapter/out/database/...** interacts with database