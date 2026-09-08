package com.kirotodo.adapter.in;

import com.kirotodo.port.in.todo.CreateTodoUseCase;
import com.kirotodo.port.in.todo.ListTodoItemsUseCase;
import com.kirotodo.port.in.todo.UpdateTodoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final CreateTodoUseCase createTodoUseCase;
    private final ListTodoItemsUseCase listTodoItemsUseCase;
    private final UpdateTodoUseCase updateTodoUseCase;

    @PostMapping
    public ResponseEntity<CreateTodoUseCase.Response> createTodo(@RequestBody CreateTodoUseCase.Request request) {
        CreateTodoUseCase.Response response = createTodoUseCase.create(request);

        return ResponseEntity
            .created(URI.create("/api/v1/todos/" + response.id()))
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ListTodoItemsUseCase.Response>> listTodos(
        @RequestParam(required = false) Boolean status,
        @RequestParam(defaultValue = "desc") ListTodoItemsUseCase.SortOrder dueDateOrder
    ) {
        ListTodoItemsUseCase.Request listRequest = new ListTodoItemsUseCase.Request(status, dueDateOrder);
        List<ListTodoItemsUseCase.Response> response = listTodoItemsUseCase.list(listRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateTodoUseCase.Response> updateTodo(
        @PathVariable("id") java.util.UUID id,
        @RequestBody UpdateTodoUseCase.Request request
    ) {
        // Ensure the ID in the path matches the ID in the request body
        if (!id.equals(request.id())) {
            return ResponseEntity.badRequest().build();
        }
        UpdateTodoUseCase.Response response = updateTodoUseCase.update(request);
        return ResponseEntity.ok(response);
    }
}