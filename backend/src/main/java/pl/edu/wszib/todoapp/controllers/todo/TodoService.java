package pl.edu.wszib.todoapp.controllers.todo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.edu.wszib.todoapp.todo.Todo;
import pl.edu.wszib.todoapp.todo.TodoRepository;
import pl.edu.wszib.todoapp.todo.TodoTaskCategory;
import pl.edu.wszib.todoapp.todo.TodoTaskCategoryRepository;
import pl.edu.wszib.todoapp.user.User;
import pl.edu.wszib.todoapp.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoTaskCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TodoDto create(CreateTodoDto createTodoDto) {
        User user = getCurrentUser();
        TodoTaskCategory category = categoryRepository.findById(createTodoDto.getCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Todo todo = Todo
                .builder()
                .title(createTodoDto.getTitle())
                .description(createTodoDto.getDescription())
                .category(category)
                .user(user)
                .completed(createTodoDto.isCompleted())
                .build();

        todoRepository.save(todo);
        return convertToDto(todo);
    }

    public TodoDto findById(Long id) {
        User user = getCurrentUser();
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this todo.");
        }

        return convertToDto(todo);
    }

    public List<TodoDto> findAll() {
        User user = getCurrentUser();
        List<Todo> todos = todoRepository.findAllByUserLogin(user.getLogin(), Sort.by(Sort.Direction.DESC, "createTime"));

        return todos
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TodoDto> findAllByCategory(Long categoryId) {
        User user = getCurrentUser();
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        List<Todo> todos = todoRepository.findByCategoryIdAndUserLogin(categoryId, user.getLogin());

        return todos
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TodoDto update(Long id, CreateTodoDto createTodoDto) {
        User user = getCurrentUser();
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this todo.");
        }

        TodoTaskCategory category = categoryRepository.findById(createTodoDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        todo.setCategory(category);
        todo.setTitle(createTodoDto.getTitle());
        todo.setDescription(createTodoDto.getDescription());
        todo.setCompleted(createTodoDto.isCompleted());
        Todo updatedTodo = todoRepository.save(todo);

        return convertToDto(updatedTodo);
    }

    public void delete(Long id) {
        User user = getCurrentUser();
        Todo todo = todoRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this todo.");
        }

        todoRepository.delete(todo);
    }

    private TodoDto convertToDto(Todo todo) {
        TodoTaskCategory category = todo.getCategory();

        return TodoDto
                .builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .category(TodoTaskCategoryDto
                        .builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createTime(todo.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                .build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
