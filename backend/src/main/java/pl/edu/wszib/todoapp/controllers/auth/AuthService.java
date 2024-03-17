package pl.edu.wszib.todoapp.controllers.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.wszib.todoapp.AlreadyExistsException;
import pl.edu.wszib.todoapp.jwt.JWTService;
import pl.edu.wszib.todoapp.user.Role;
import pl.edu.wszib.todoapp.user.UserRepository;
import pl.edu.wszib.todoapp.user.User;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        userRepository.findByLogin(request.getLogin())
                .ifPresent(c -> {
                    throw new AlreadyExistsException("User with such login already exists");
                });

        User user = User
                .builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.User)
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(new HashMap<>(), user);

        return RegisterResponse
                .builder()
                .token(jwtToken)
                .userId(user.getId())
                .build();
    }

    public GetTokenResponse getToken(GetTokenRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        User user = userRepository
                .findByLogin(request.getLogin())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        String jwtToken = jwtService.generateToken(new HashMap<>(), user);

        return GetTokenResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}
