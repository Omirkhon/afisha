package com.practice.afisha.event;

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
        return eventMapper.toFullDto(eventService.findById(id, ip));
    }
}
