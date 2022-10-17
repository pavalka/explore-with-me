package ru.practicum.explorewithme.entities.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "hits")
@Getter
@Setter
@NoArgsConstructor
public class Hit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri", nullable = false, length = 1024)
    private String uri;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "app", nullable = false, length = 100)
    private String app;

    @Column(name = "ip", nullable = false, length = 16)
    private String ip;

    public Hit(String uri, LocalDateTime timestamp, String app, String ip) {
        this.uri = uri;
        this.timestamp = timestamp;
        this.app = app;
        this.ip = ip;
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
        return id.equals(((Hit) obj).id);
    }
}
