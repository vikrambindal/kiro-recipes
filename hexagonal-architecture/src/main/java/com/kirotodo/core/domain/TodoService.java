package com.kirotodo.core.domain;

import com.kirotodo.port.in.todo.CreateTodoUseCase;
import com.kirotodo.port.in.todo.SearchTodoUseCase;
import com.kirotodo.port.in.todo.UpdateTodoUseCase;
import com.kirotodo.port.out.todo.FindTodoByIdUseCase;
import com.kirotodo.port.out.todo.SaveTodoUseCase;
import com.kirotodo.port.out.todo.SearchTodoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService implements CreateTodoUseCase, SearchTodoUseCase, UpdateTodoUseCase {

    private final SaveTodoUseCase saveTodoUseCase;
    private final FindTodoByIdUseCase findTodoByIdUseCase;
    private final SearchTodoPort searchTodoPort;

    @Override
    @Transactional
    public CreateTodoUseCase.Response create(CreateTodoUseCase.Request request) {
        log.info("Creating todo with title='{}'", request.title());
        SaveTodoUseCase.Response saved = saveTodoUseCase.save(new SaveTodoUseCase.Request(
            null,
            Optional.of(request.title()),
            Optional.of(request.description()),
            Optional.empty()
        ));
        return new CreateTodoUseCase.Response(
            saved.id(), saved.title(), saved.description(),
            saved.completed(), saved.createdAt(), saved.updatedAt());
    }

    @Override
    public List<SearchTodoUseCase.Response> search(SearchTodoUseCase.Request request) {
        log.debug("Searching todos with status={}, dueDateOrder={}", request.status(), request.dueDateOrder());
        SearchTodoPort.SortOrder sortOrder = request.dueDateOrder() == SearchTodoUseCase.SortOrder.ASC
            ? SearchTodoPort.SortOrder.ASC
            : SearchTodoPort.SortOrder.DESC;

        return searchTodoPort.search(new SearchTodoPort.Request(request.status(), sortOrder))
            .stream()
            .map(r -> new SearchTodoUseCase.Response(
                r.id(), r.title(), r.description(),
                r.completed(), r.createdAt(), r.updatedAt()))
            .toList();
    }

    @Override
    @Transactional
    public UpdateTodoUseCase.Response update(UpdateTodoUseCase.Request request) {
        log.info("Updating todo id={}", request.id());
        FindTodoByIdUseCase.Response existing = findTodoByIdUseCase.findById(
            new FindTodoByIdUseCase.Request(request.id()))
            .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + request.id()));

        if (existing.completed()) {
            log.warn("Attempted to update completed todo id={}", request.id());
            throw new IllegalStateException("Cannot update a completed todo");
        }

        SaveTodoUseCase.Response saved = saveTodoUseCase.save(new SaveTodoUseCase.Request(
            request.id(),
            Optional.ofNullable(request.title()),
            Optional.ofNullable(request.description()),
            Optional.ofNullable(request.completed())
        ));
        return new UpdateTodoUseCase.Response(
            saved.id(), saved.title(), saved.description(),
            saved.completed(), saved.createdAt(), saved.updatedAt());
    }
}
