package pl.edu.wszib.todoapp.controllers.todo;

import jakarta.validation.constraints.Pattern;
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
public class CreateCategoryDto {
    @Pattern(regexp = "^[a-zA-Z0-9_ ]+$", message = "Not allowed character")
    @Length(min = 1, max = 50, message = "Category name must be between 1 and 50 characters")
    @NotBlank(message = "Category name cannot be empty")
    private String name;
}
