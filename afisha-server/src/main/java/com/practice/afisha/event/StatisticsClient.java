package com.practice.afisha.event;

import com.practice.afisha.utils.EndpointHit;
import com.practice.afisha.utils.ViewStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatisticsClient {
    private final RestTemplate restTemplate;

    public StatisticsClient(@Value("http://localhost:9090") String url,
                      RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public void create(EndpointHit endpointHit) {
        restTemplate.postForEntity("/hit", endpointHit, Object.class);
    }

    public ResponseEntity<List<ViewStats>> getStatsInfo(String start, String end,
                                                        String[] uris, boolean unique) {
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("uris", uris);
        map.put("unique", unique);

        return restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {}, map);
    }
}
