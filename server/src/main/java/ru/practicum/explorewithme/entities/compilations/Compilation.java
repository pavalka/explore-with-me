package ru.practicum.explorewithme.entities.compilations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.entities.events.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    @ManyToMany
    @JoinTable(name = "compilations_events",
               joinColumns = @JoinColumn(name = "compilation_id"),
               inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    public Compilation(String title, Boolean pinned, List<Event> events) {
        this.title = title;
        this.pinned = pinned;
        this.events = events;
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
        if (obj == null || id == null || getClass() != obj.getClass()) {
            return false;
        }
        return ((Compilation) obj).id.equals(id);
    }
}
