package com.kirotodo.port.out.todo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface SaveTodoUseCase {
    Response save(Request request);

    record Request(UUID id, Optional<String> title, Optional<String> description, Optional<Boolean> completed) {}

    record Response(UUID id, String title, String description, boolean completed,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {}
}