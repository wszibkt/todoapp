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
public class GetTokenRequest {
    @Pattern(regexp = "^[a-z]+$", message = "Only lowercase characters are allowed")
    @Length(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    @NotBlank(message = "Login cannot be empty")
    private String login;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
