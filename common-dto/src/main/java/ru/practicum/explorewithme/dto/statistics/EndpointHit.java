package ru.practicum.explorewithme.dto.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EndpointHit {
    private Long id;

    @NotEmpty
    private String app;

    @NotEmpty
    private String uri;

    @NotEmpty
    private String ip;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    public EndpointHit(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
    }
}
