package com.kirotodo.adapter.out.database.reader;

import com.kirotodo.adapter.out.database.TodoJpaRepository;
import com.kirotodo.adapter.out.database.entity.TodoEntity;
import com.kirotodo.port.out.todo.FindTodoByIdUseCase;
import com.kirotodo.port.out.todo.SearchTodoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TodoReadRepository implements FindTodoByIdUseCase, SearchTodoPort {

    private final TodoJpaRepository repository;

    @Override
    public Optional<FindTodoByIdUseCase.Response> findById(FindTodoByIdUseCase.Request request) {
        log.debug("findById id={}", request.id());
        return repository.findById(request.id())
            .map(this::toFindResponse);
    }

    @Override
    public List<SearchTodoPort.Response> search(SearchTodoPort.Request request) {
        log.debug("search status={}, sortOrder={}", request.status(), request.sortOrder());
        List<TodoEntity> entities = repository.findAll();

        if (request.status() != null) {
            entities = entities.stream()
                .filter(e -> e.isCompleted() == request.status())
                .toList();
        }

        Comparator<TodoEntity> byUpdatedAt = Comparator.comparing(TodoEntity::getUpdatedAt);
        entities = (request.sortOrder() == SearchTodoPort.SortOrder.ASC)
            ? entities.stream().sorted(byUpdatedAt).toList()
            : entities.stream().sorted(byUpdatedAt.reversed()).toList();

        return entities.stream().map(this::toSearchResponse).toList();
    }

    private FindTodoByIdUseCase.Response toFindResponse(TodoEntity entity) {
        return new FindTodoByIdUseCase.Response(
            entity.getId(), entity.getTitle(), entity.getDescription(),
            entity.isCompleted(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private SearchTodoPort.Response toSearchResponse(TodoEntity entity) {
        return new SearchTodoPort.Response(
            entity.getId(), entity.getTitle(), entity.getDescription(),
            entity.isCompleted(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
