package com.kirotodo.adapter.out.database.reader;

import com.kirotodo.adapter.out.database.entity.TodoEntity;
import com.kirotodo.port.in.todo.SearchTodoUseCase;
import com.kirotodo.port.out.todo.FindTodoByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoReadRepository implements FindTodoByIdUseCase, SearchTodoUseCase {

    private final TodoJpaRepository repository;

    @Override
    public Optional<FindTodoByIdUseCase.Response> findById(FindTodoByIdUseCase.Request request) {
        return repository.findById(request.id())
            .map(entity -> new FindTodoByIdUseCase.Response(
                entity.getId(), entity.getTitle(),
                entity.getDescription(), entity.isCompleted(),
                entity.getCreatedAt(), entity.getUpdatedAt()));
    }

    @Override
    public List<SearchTodoUseCase.Response> search(SearchTodoUseCase.Request request) {
        List<TodoEntity> entities = repository.findAll();

        if (request.status() != null) {
            entities = entities.stream()
                .filter(e -> e.isCompleted() == request.status())
                .toList();
        }

        if (request.dueDateOrder() != null) {
            if (request.dueDateOrder() == SearchTodoUseCase.SortOrder.DESC) {
                entities.sort(Comparator.comparing(TodoEntity::getUpdatedAt).reversed());
            } else {
                entities.sort(Comparator.comparing(TodoEntity::getUpdatedAt));
            }
        }

        return entities.stream()
            .map(entity -> new SearchTodoUseCase.Response(
                entity.getId(), entity.getTitle(),
                entity.getDescription(), entity.isCompleted(),
                entity.getCreatedAt(), entity.getUpdatedAt()))
            .toList();
    }
}