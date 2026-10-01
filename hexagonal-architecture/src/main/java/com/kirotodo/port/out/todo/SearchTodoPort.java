package com.kirotodo.port.out.todo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SearchTodoPort {
    List<Response> search(Request request);

    record Request(Boolean status, SortOrder sortOrder) {}

    enum SortOrder {
        ASC, DESC
    }

    record Response(UUID id, String title, String description, boolean completed,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
