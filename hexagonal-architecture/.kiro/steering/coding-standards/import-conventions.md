---
inclusion: auto
inclusionMode: fileMatch
fileMatchPattern: "*.md"
---

# Import Conventions

## No Wildcard Imports

All imports must use explicit class names, not wildcard imports (`import package.*`).

### Why
- Wildcard imports make it harder to understand dependencies
- Can cause naming conflicts when multiple packages have classes with the same name
- Makes code navigation and IDE auto-completion less precise
- Goes against Java best practices

### Correct

```java
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
```

### Incorrect

```java
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
```

### Exceptions
- Static imports from the same class or test utilities are acceptable
- Imports from `java.lang` are implicit and don't need to be declared