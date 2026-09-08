package com.kirotodo.port.in.todo;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UpdateTodoUseCase {
    Response update(Request request);

    record Request(UUID id, String title, String description, Boolean completed) {}

    record Response(UUID id, String title, String description, boolean completed,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
