package ru.practicum.explorewithme.entities.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "lat", column = @Column(name = "lat")),
        @AttributeOverride(name = "lon", column = @Column(name = "lon"))
})
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Double lat;
    private Double lon;
}
