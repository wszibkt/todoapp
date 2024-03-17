package pl.edu.wszib.todoapp.properties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
@RequiredArgsConstructor
public class CorsProperties {
    private final List<String> allowedOrigins;
    private final List<String> allowedMethods;

    public String[] getAllowedOrigins() {
        return allowedOrigins.toArray(new String[0]);
    }

    public String[] getAllowedMethods() {
        return allowedMethods.toArray(new String[0]);
    }
}