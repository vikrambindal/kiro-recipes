---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Services Conventions

## Core Domain Package
- Contains domain models and service implementations
- Services implement `port/in` interfaces
- Services use `port/out` interfaces for external interactions
- Services are Spring beans (annotated with `@Service`)

## Service Implementation Pattern
```java
package com.kirotodo.core.domain;

import com.kirotodo.port.in.todo.CreateTodoUseCase;
import com.kirotodo.port.out.todo.SaveTodoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService implements CreateTodoUseCase {

    private final SaveTodoUseCase saveTodoUseCase;

    @Override
    public Response create(Request request) {
        // Implementation
    }
}
```

## DTO Package
- Contains request/response DTOs for external interfaces
- Not used for internal domain communication
- DTOs should use Lombok annotations (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`)