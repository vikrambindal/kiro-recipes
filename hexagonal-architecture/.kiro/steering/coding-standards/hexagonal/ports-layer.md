---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Ports Layer

- Place all `port/in` interfaces in `port/in/{domain}/`; all `port/out` interfaces in `port/out/{domain}/`
- Name each port by its intent — `CreateTodoUseCase`, `SaveTodoUseCase`, `FindTodoByIdUseCase`
- Each port interface defines exactly one method
- Nest `Request` and `Response` as Java records inside the port interface — declare method signature first, then `Request`, then `Response`
- For search/list ports, nest a `SortOrder` enum inside the interface; default sort order is `DESC`
- Ports must not import Spring, JPA, or any infrastructure type — pure Java only