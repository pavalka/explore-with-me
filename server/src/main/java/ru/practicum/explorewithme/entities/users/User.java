package ru.practicum.explorewithme.entities.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.requests.Request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Request> requests;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    public User(@NonNull String email, @NotNull String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (id == null || obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return ((User) obj).id.equals(id);
    }
}
