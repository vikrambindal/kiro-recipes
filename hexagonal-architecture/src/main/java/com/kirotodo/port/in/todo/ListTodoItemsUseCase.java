package com.kirotodo.port.in.todo;

import java.util.List;
import java.util.UUID;

public interface ListTodoItemsUseCase {
    List<ListTodoItemsUseCase.Response> list(ListTodoItemsUseCase.Request request);

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
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt
    ) {}
}