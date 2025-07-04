package com.practice.afisha.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final EventRepository eventRepository;
    private final Map<String, List<Integer>> ipMap = new HashMap<>();

    public void increaseViews(Event event, String ip) {
        if (ip == null) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        } else if (ipMap.get(ip) == null || !ipMap.get(ip).contains(event.getId())) {
            if (ipMap.get(ip) == null) {
                ipMap.put(ip, List.of(event.getId()));
            }
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }
    }

    public void increaseViews(List<Event> events) {
        events.forEach(event -> increaseViews(event, null));
    }
}
