package com.kirotodo.port.out.todo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface FindTodoByIdUseCase {
    Optional<Response> findById(Request request);

    record Request(UUID id) {}

    record Response(UUID id, String title, String description, boolean completed,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {}
}