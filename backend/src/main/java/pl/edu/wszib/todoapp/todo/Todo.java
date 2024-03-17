package pl.edu.wszib.todoapp.todo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import pl.edu.wszib.todoapp.user.User;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "category_id", nullable = false)
    private TodoTaskCategory category;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
