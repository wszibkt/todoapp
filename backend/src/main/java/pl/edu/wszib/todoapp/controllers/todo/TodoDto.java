package pl.edu.wszib.todoapp.controllers.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private TodoTaskCategoryDto category;
    private long createTime;
}
