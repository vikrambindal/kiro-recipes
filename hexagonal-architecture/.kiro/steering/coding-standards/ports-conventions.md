---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Port Conventions

## Package Structure
- Ports are defined in `port/in` and `port/out` subpackages
- Each domain gets its own subpackage (e.g., `port/in/todo`, `port/out/todo`)

## Naming
- Each Interface should be named by its intent (e.g., `CreateTodoUseCase`, `SaveTodoUseCase`)
- **port/in** interfaces: Used by `adapter/in` (incoming adapters like REST controllers)
- **port/out** interfaces: Used by `adapter/out` (outgoing adapters like database)

## Record Order
Inside each port interface, use Java Records in this order:
1. Method signature first (e.g., `Response save(Request request);`)
2. Request record with various properties for method argument
3. Response record with various properties for method return

Example:
```java
package com.kirotodo.port.out.todo;

public interface SaveTodoUseCase {
    Response save(Request request);

    record Request(String title, String description) {}

    record Response(UUID id, String title, String description, boolean completed,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
```

## Sorting Conventions
- For list operations, use `SortOrder` enum with `ASC` and `DESC` values
- Default sorting order should be `DESC` (descending) for date-based sorting
- Filter by status using `Boolean status` parameter
- Order parameters should use `@RequestParam(defaultValue = "desc")` for DESC as default

Example:
```java
package com.kirotodo.port.in.todo;

public interface SearchTodoUseCase {
    List<Response> search(Request request);

    record Request(
        Boolean status,
        SortOrder dueDateOrder
    ) {}

    enum SortOrder {
        ASC, DESC
    }

    record Response(
        UUID id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}
}
```