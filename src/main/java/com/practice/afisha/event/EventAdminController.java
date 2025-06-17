package com.practice.afisha.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> findFilteredEvents(@RequestParam(required = false) Integer[] users,
                                                 @RequestParam(required = false) String[] states,
                                                 @RequestParam(required = false) Integer[] categories,
                                                 @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return eventMapper.toFullDto(eventService.findFilteredEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable int eventId, @RequestBody @Valid UpdateEventAdminRequest updatedEvent) {
        return eventMapper.toFullDto(eventService.update(eventId, updatedEvent));
    }
}
