package com.kirotodo.port.in.todo;

import com.kirotodo.port.out.todo.SaveTodoUseCase;

public interface CreateTodoUseCase {
    CreateTodoUseCase.Response create(CreateTodoUseCase.Request request);

    record Request(String title, String description) {}

    record Response(UUID id, String title, String description, boolean completed,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {}
}