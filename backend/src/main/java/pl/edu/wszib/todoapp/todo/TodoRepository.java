package pl.edu.wszib.todoapp.todo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByCategoryId(Long categoryId);
    List<Todo> findByCategoryIdAndUserLogin(Long categoryId, String login);

    List<Todo> findByCategoryName(String categoryName);

    List<Todo> findAllByUserLogin(String login, Sort sort);
}
