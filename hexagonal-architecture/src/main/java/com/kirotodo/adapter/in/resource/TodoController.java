package com.kirotodo.adapter.in.resource;

import com.kirotodo.port.in.todo.CreateTodoUseCase;
import com.kirotodo.port.in.todo.SearchTodoUseCase;
import com.kirotodo.port.in.todo.UpdateTodoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final CreateTodoUseCase createTodoUseCase;
    private final SearchTodoUseCase searchTodoUseCase;
    private final UpdateTodoUseCase updateTodoUseCase;

    @PostMapping
    public ResponseEntity<CreateTodoUseCase.Response> create(@Valid @RequestBody CreateTodoUseCase.Request request) {
        log.info("POST /api/v1/todos - title='{}'", request.title());
        CreateTodoUseCase.Response response = createTodoUseCase.create(request);
        return ResponseEntity
            .created(URI.create("/api/v1/todos/" + response.id()))
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SearchTodoUseCase.Response>> search(
        @RequestParam(required = false) Boolean status,
        @RequestParam(defaultValue = "DESC") SearchTodoUseCase.SortOrder dueDateOrder
    ) {
        log.debug("GET /api/v1/todos - status={}, dueDateOrder={}", status, dueDateOrder);
        List<SearchTodoUseCase.Response> response = searchTodoUseCase.search(new SearchTodoUseCase.Request(status, dueDateOrder));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateTodoUseCase.Response> update(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateTodoUseCase.Request request
    ) {
        log.info("PUT /api/v1/todos/{} ", id);
        if (!id.equals(request.id())) {
            log.warn("Path id={} does not match body id={}", id, request.id());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updateTodoUseCase.update(request));
    }
}
