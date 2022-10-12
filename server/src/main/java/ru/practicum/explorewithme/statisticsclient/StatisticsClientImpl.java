package ru.practicum.explorewithme.statisticsclient;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.statistics.EndpointHit;
import ru.practicum.explorewithme.dto.statistics.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StatisticsClientImpl implements StatisticsClient {
    private static final String APP_NAME = "Main";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
    private static final String GET_STAT_ENDPOINT = "/stats?start={rangeStart}&end={rangeEnd}&uris={uris}";
    private static final String POST_STAT_ENDPOINT = "/hit";

    private final RestTemplate client;

    @Autowired
    public StatisticsClientImpl(@Value("${explore-with-me.stat.url}") String url, RestTemplateBuilder builder) {
        client = builder.requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .uriTemplateHandler(new DefaultUriBuilderFactory(url)).build();
    }

    @Override
    public void sendStatistics(@NonNull String uri, @NonNull String ip) {
        var hit = new EndpointHit(APP_NAME, uri, ip);
        var httpEntity = new HttpEntity<>(hit, getDefaultHeaders());

        ResponseEntity<Void> response = client.exchange(POST_STAT_ENDPOINT, HttpMethod.POST, httpEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("При отправке статистики сервер вернул ошибку: {}", response.getStatusCode());
        }
    }

    @Override
    public List<ViewStats> getStatistics(@NonNull List<String> uris, @NonNull LocalDateTime rangeStart,
                                         @NonNull LocalDateTime rangeEnd) {
        var urisString = String.join(",", uris);
        var httpEntity = new HttpEntity<>(getDefaultHeaders());

        ResponseEntity<List<ViewStats>> response = client.exchange(GET_STAT_ENDPOINT, HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<>() {}, Map.of("rangeStart", rangeStart.format(DATE_TIME_FORMATTER),
                        "rangeEnd", rangeEnd.format(DATE_TIME_FORMATTER), "uris", urisString));

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("При получении статистики сервер вернул ошибку {}", response.getStatusCode());
            return new ArrayList<>();
        }

        return response.getBody().stream().filter(view -> view.getApp().equals(APP_NAME)).collect(Collectors.toList());
    }

    private HttpHeaders getDefaultHeaders() {
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
