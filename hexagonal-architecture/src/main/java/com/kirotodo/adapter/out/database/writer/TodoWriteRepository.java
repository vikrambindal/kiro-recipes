package com.kirotodo.adapter.out.database.writer;

import com.kirotodo.adapter.out.database.TodoJpaRepository;
import com.kirotodo.adapter.out.database.entity.TodoEntity;
import com.kirotodo.port.out.todo.SaveTodoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TodoWriteRepository implements SaveTodoUseCase {

    private final TodoJpaRepository repository;

    @Override
    @Transactional
    public Response save(Request request) {
        Optional<TodoEntity> existing = repository.findById(request.id());
        TodoEntity entity;

        if (existing.isPresent()) {
            log.debug("Updating existing todo id={}", request.id());
            entity = existing.get();
            request.title().ifPresent(entity::setTitle);
            request.description().ifPresent(entity::setDescription);
            request.completed().ifPresent(entity::setCompleted);
            entity.setUpdatedAt(LocalDateTime.now());
        } else {
            log.debug("Creating new todo with title='{}'", request.title().orElse(null));
            entity = new TodoEntity(
                null,
                request.title().orElseThrow(() -> new IllegalArgumentException("Title is required")),
                request.description().orElse(null),
                request.completed().orElse(false),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
        }

        TodoEntity saved = repository.save(entity);
        return new Response(saved.getId(), saved.getTitle(), saved.getDescription(),
            saved.isCompleted(), saved.getCreatedAt(), saved.getUpdatedAt());
    }
}
