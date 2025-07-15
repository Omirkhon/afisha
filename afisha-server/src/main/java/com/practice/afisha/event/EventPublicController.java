package com.practice.afisha.event;

import com.practice.afisha.utils.EndpointHit;
import com.practice.afisha.utils.ViewStats;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final StatisticsClient statisticsClient;

    @GetMapping
    public List<EventShortDto> findFilteredAndSortedEvents(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) Integer[] categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        return eventMapper.toShortDto(eventService.findFilteredAndSortedEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size));
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable int id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String uri = "/events/{id}";

        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(ip);
        endpointHit.setApp("afisha-app");
        endpointHit.setUri(uri);
        String[] uris = new String[]{uri};

        statisticsClient.create(endpointHit);
        List<ViewStats> statsList = statisticsClient.getStatsInfo("1900-01-01 00:00:00", "2100-01-01 00:00:00", uris, true).getBody();
        long hits = 0;
        if (statsList != null && !statsList.isEmpty()) {
            hits = statsList.get(0).getHits();
        }
        return eventMapper.toFullDto(eventService.findById(id), hits);
    }

    @GetMapping("/likes")
    public List<EventRatingDto> findAllByLikesRatio(@RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return eventService.findAllSortedByLikesRatio(from, size);
    }

    @GetMapping("/mostliked")
    public EventRatingDto findTheMostLikedEvent() {
        return eventService.findTheMostLikedEvent();
    }
}
