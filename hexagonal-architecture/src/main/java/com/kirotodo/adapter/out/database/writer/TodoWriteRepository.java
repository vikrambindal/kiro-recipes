package com.kirotodo.adapter.out.database.writer;

import com.kirotodo.adapter.out.database.entity.TodoEntity;
import com.kirotodo.port.out.todo.SaveTodoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class TodoWriteRepository implements SaveTodoUseCase {

    private final TodoJpaRepository repository;

    @Override
    public Response save(Request request) {
        // Check if todo exists (update vs create)
        Optional<TodoEntity> optionalEntity = repository.findById(request.id());
        TodoEntity entity;

        if (optionalEntity.isPresent()) {
            entity = optionalEntity.get();
            // Apply partial updates
            request.title().ifPresent(entity::setTitle);
            request.description().ifPresent(entity::setDescription);
            request.completed().ifPresent(entity::setCompleted);
            entity.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new todo
            entity = new TodoEntity(
                request.id(),
                request.title().orElseThrow(() -> new RuntimeException("Title is required for new todo")),
                request.description().orElseThrow(() -> new RuntimeException("Description is required for new todo")),
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