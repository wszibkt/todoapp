package pl.edu.wszib.todoapp.controllers.todo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.wszib.todoapp.AlreadyExistsException;
import pl.edu.wszib.todoapp.todo.TodoTaskCategory;
import pl.edu.wszib.todoapp.todo.TodoTaskCategoryRepository;
import pl.edu.wszib.todoapp.user.User;
import pl.edu.wszib.todoapp.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final TodoTaskCategoryRepository repository;
    private final UserRepository userRepository;

    public TodoTaskCategoryDto create(String name) {
        User user = getCurrentUser();
        repository.findByName(name)
                .ifPresent(c -> {
                    throw new AlreadyExistsException("Category with such name already exists");
                });

        TodoTaskCategory category = TodoTaskCategory
                .builder()
                .name(name)
                .user(user)
                .build();

        repository.save(category);
        return convertToDto(category);
    }

    public TodoTaskCategoryDto findById(Long id) {
        User user = getCurrentUser();
        TodoTaskCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this category.");
        }

        return convertToDto(category);
    }

    public List<TodoTaskCategoryDto> findAll() {
        User user = getCurrentUser();
        List<TodoTaskCategory> categories = repository.findAllByUserLogin(user.getLogin(), Sort.by("id"));

        return categories
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TodoTaskCategoryDto update(Long id, String name) {
        User user = getCurrentUser();
        TodoTaskCategory category = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this category.");
        }

        category.setName(name);
        TodoTaskCategory updatedCategory = repository.save(category);

        return convertToDto(updatedCategory);
    }

    public void delete(Long id) {
        User user = getCurrentUser();
        TodoTaskCategory category = repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this category.");
        }

        repository.delete(category);
    }

    private TodoTaskCategoryDto convertToDto(TodoTaskCategory category) {
        return TodoTaskCategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
