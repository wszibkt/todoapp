package pl.edu.wszib.todoapp.controllers.todo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTodoDto {
    @Length(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Length(max = 1024, message = "Description length exceeded")
    private String description;

    private boolean completed = false;

    @NotNull
    private Long categoryId;
}
