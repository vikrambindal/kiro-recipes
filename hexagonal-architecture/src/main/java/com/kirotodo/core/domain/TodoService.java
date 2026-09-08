package com.kirotodo.core.domain;

import com.kirotodo.port.in.todo.CreateTodoUseCase;
import com.kirotodo.port.in.todo.ListTodoItemsUseCase;
import com.kirotodo.port.in.todo.UpdateTodoUseCase;
import com.kirotodo.port.out.todo.SaveTodoUseCase;
import com.kirotodo.port.out.todo.FindTodoByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService implements CreateTodoUseCase, ListTodoItemsUseCase, UpdateTodoUseCase {

    private final SaveTodoUseCase saveTodoUseCase;
    private final FindTodoByIdUseCase findTodoByIdUseCase;

    @Override
    public Response create(Request request) {
        SaveTodoUseCase.Request saveRequest = new SaveTodoUseCase.Request(
            null,
            java.util.Optional.of(request.title()),
            java.util.Optional.of(request.description()),
            java.util.Optional.empty()
        );
        SaveTodoUseCase.Response saveResponse = saveTodoUseCase.save(saveRequest);
        return new Response(
            saveResponse.id(),
            saveResponse.title(),
            saveResponse.description(),
            saveResponse.completed(),
            saveResponse.createdAt(),
            saveResponse.updatedAt()
        );
    }

    @Override
    public List<Response> list(Request request) {
        FindTodoByIdUseCase.Request findRequest = new FindTodoByIdUseCase.Request(null);
        FindTodoByIdUseCase.Response response = findTodoByIdUseCase.findById(findRequest);

        if (response == null) {
            return List.of();
        }

        List<Response> allTodos = List.of(response);

        if (request.status() != null) {
            allTodos = allTodos.stream()
                .filter(t -> t.completed() == request.status())
                .collect(Collectors.toList());
        }

        return allTodos;
    }

    @Override
    @Transactional
    public Response update(Request request) {
        // First, find the existing todo
        FindTodoByIdUseCase.Request findRequest = new FindTodoByIdUseCase.Request(request.id());
        java.util.Optional<FindTodoByIdUseCase.Response> optionalResponse = findTodoByIdUseCase.findById(findRequest);

        if (optionalResponse.isEmpty()) {
            throw new RuntimeException("Todo not found with id: " + request.id());
        }

        FindTodoByIdUseCase.Response existingTodo = optionalResponse.get();

        // Check if the todo is completed - if so, prevent updates
        if (existingTodo.completed()) {
            throw new IllegalStateException("Cannot update a todo item that is already marked as DONE");
        }

        // Build the update request with partial updates
        SaveTodoUseCase.Request saveRequest = new SaveTodoUseCase.Request(
            request.id(),
            request.title() != null ? java.util.Optional.of(request.title()) : java.util.Optional.empty(),
            request.description() != null ? java.util.Optional.of(request.description()) : java.util.Optional.empty(),
            request.completed() != null ? java.util.Optional.of(request.completed()) : java.util.Optional.empty()
        );

        SaveTodoUseCase.Response saveResponse = saveTodoUseCase.save(saveRequest);
        return new Response(
            saveResponse.id(),
            saveResponse.title(),
            saveResponse.description(),
            saveResponse.completed(),
            saveResponse.createdAt(),
            saveResponse.updatedAt()
        );
    }
}