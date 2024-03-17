package pl.edu.wszib.todoapp.controllers.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wszib.todoapp.controllers.todo.TodoDto;
import pl.edu.wszib.todoapp.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
    @GetMapping
    public ResponseEntity<?> get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return ResponseEntity.ok(UserDto
                    .builder()
                            .id(user.getId())
                            .login(user.getLogin())
                            .role(user.getRole())
                    .build());
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
