package ru.practicum.explorewithme.entities.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.entities.categories.Category;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.requests.Request;
import ru.practicum.explorewithme.entities.users.User;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @OneToMany(mappedBy = "event")
    private List<Request> requests;

    @OneToMany(mappedBy = "event")
    private List<Comment> comments;

//    @Column(name = "location", nullable = false)
//    @Type(type = "org.hibernate.spatial.JTSGeometryType")
//    private Point location;

    @Embedded
    private Location location;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @Column(name = "participants_limit", nullable = false)
    private Integer participantsLimit;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    private EventState state = EventState.PENDING;

    @Column(name = "available", nullable = false)

    private Boolean available = true;

    public Event(String title, String description, String annotation, Category category, User initiator, Location location,
                 LocalDateTime eventDate, Boolean paid, Integer participantsLimit, Boolean requestModeration) {
        this.title = title;
        this.description = description;
        this.annotation = annotation;
        this.category = category;
        this.initiator = initiator;
        this.location = location;
        this.eventDate = eventDate;
        this.paid = paid;
        this.participantsLimit = participantsLimit;
        this.requestModeration = requestModeration;
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
        if (obj == null || id == null || obj.getClass() != getClass()) {
            return false;
        }
        return ((Event) obj).id.equals(id);
    }
}
