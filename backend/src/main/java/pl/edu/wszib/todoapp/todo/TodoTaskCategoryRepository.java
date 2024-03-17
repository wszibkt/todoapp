package pl.edu.wszib.todoapp.todo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoTaskCategoryRepository extends JpaRepository<TodoTaskCategory, Long> {
    Optional<TodoTaskCategory> findByName(String name);
    List<TodoTaskCategory> findAllByUserLogin(String login, Sort sort);
}
