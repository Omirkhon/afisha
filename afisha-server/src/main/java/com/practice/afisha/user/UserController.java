package com.practice.afisha.user;

import com.practice.afisha.event.*;
import com.practice.afisha.request.EventRequestStatusUpdateRequest;
import com.practice.afisha.request.EventRequestStatusUpdateResult;
import com.practice.afisha.request.ParticipationRequestDto;
import com.practice.afisha.request.RequestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> findAllUsersEvents(@PathVariable int userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return eventMapper.toShortDto(userService.findAllUsersEvents(userId, from, size));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable int userId, @RequestBody @Valid NewEventDto newEvent) {
        return eventMapper.toFullDto(userService.createEvent(userId, newEvent), 0L);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto findEventById(@PathVariable int userId, @PathVariable int eventId) {
        return eventMapper.toFullDto(userService.findEventById(userId, eventId), 0L);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable int userId, @PathVariable int eventId, @RequestBody @Valid UpdateEventUserRequest eventUpdate) {
        return eventMapper.toFullDto(userService.updateEvent(userId, eventId, eventUpdate), 0L);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findUserRequestsForEvent(@PathVariable int userId, @PathVariable int eventId) {
        return requestMapper.toParticipationRequestDto(userService.findUserRequestsForEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable int userId, @PathVariable int eventId, @RequestBody EventRequestStatusUpdateRequest requestUpdate) {
        return requestMapper.toUpdateResult(userService.updateRequestStatus(userId, eventId, requestUpdate));
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findUserRequests(@PathVariable int userId) {
        return requestMapper.toParticipationRequestDto(userService.findUserRequests(userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequest(@PathVariable int userId, @RequestParam int eventId) {
        return requestMapper.toParticipationRequestDto(userService.createRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelOwnRequest(@PathVariable int userId, @PathVariable int requestId) {
        return requestMapper.toParticipationRequestDto(userService.cancelOwnRequest(userId, requestId));
    }
}
