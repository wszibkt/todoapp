package pl.edu.wszib.todoapp.controllers.auth;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Not allowed character")
    @Length(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    @NotBlank(message = "Login cannot be empty")
    private String login;

    @NotBlank(message = "Password cannot be empty")
    @Length(min = 4, max = 50, message = "Password must be between 4 and 50 characters")
    private String password;
}
