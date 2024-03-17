package pl.edu.wszib.todoapp.controllers.todo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTodoDto todoDto) {
        TodoDto createdTodo = service.create(todoDto);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        TodoDto todo = service.findById(id);
        return ResponseEntity.ok(todo);
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> getAll() {
        List<TodoDto> todos = service.findAll();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getAllByCategory(@PathVariable Long id) {
        List<TodoDto> todos = service.findAllByCategory(id);
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CreateTodoDto todoDto) {
        TodoDto updatedTodo = service.update(id, todoDto);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
