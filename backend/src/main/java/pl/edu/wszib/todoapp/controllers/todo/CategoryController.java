package pl.edu.wszib.todoapp.controllers.todo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryDto categoryDto) {
        TodoTaskCategoryDto createdCategory = service.create(categoryDto.getName());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        TodoTaskCategoryDto categoryDto = service.findById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    public ResponseEntity<List<TodoTaskCategoryDto>> getAllCategories() {
        List<TodoTaskCategoryDto> categories = service.findAll();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CreateCategoryDto categoryDto) {
        TodoTaskCategoryDto updatedCategory = service.update(id, categoryDto.getName());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
