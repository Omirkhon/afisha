package com.practice.afisha.user;

import com.practice.afisha.event.*;
import com.practice.afisha.rating.RatingDto;
import com.practice.afisha.rating.RatingMapper;
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
    private final RatingMapper ratingMapper;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/ratings/{eventId}")
    public RatingDto createRating(@RequestParam boolean liked, @PathVariable int userId, @PathVariable int eventId) {
        return ratingMapper.toDto(userService.createRating(liked, userId, eventId));
    }

    @PatchMapping("/{userId}/ratings/{ratingId}")
    public RatingDto updateRating(@RequestParam boolean liked, @PathVariable int ratingId, @PathVariable int userId) {
        return ratingMapper.toDto(userService.updateRating(liked, ratingId, userId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/ratings/{ratingId}")
    public void deleteRating(@PathVariable int ratingId, @PathVariable int userId) {
        userService.deleteRating(ratingId, userId);
    }

    @GetMapping("/ratings/events/{eventId}")
    public List<RatingDto> findRatingsByEventId(@PathVariable int eventId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return ratingMapper.toDto(userService.findRatingsByEventId(eventId, from, size));
    }

    @GetMapping("/ratings/events/likes")
    public List<EventRatingDto> findAllEventsByLikesRatio(@RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return userService.findAllEventsSortedByLikesRatio(from, size);
    }

    @GetMapping("/ratings/events/top")
    public EventRatingDto findTheMostLikedEvent() {
        return userService.findTheMostLikedEvent();
    }

    @GetMapping("/{userId}/ratings")
    public List<RatingDto> findRatingsByUserId(@PathVariable int userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return ratingMapper.toDto(userService.findRatingsByUserId(userId, from, size));
    }

    @GetMapping("{userId}/ratings/events")
    public List<RatingDto> findRatingsForAllUsersEvents(@PathVariable int userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ratingMapper.toDto(userService.findRatingsForAllUsersEvents(userId, from, size));
    }

    @GetMapping("/ratings/likes")
    public List<UserRatingDto> findAllInitiatorsSortedByLikesRatio(@RequestParam(defaultValue = "0") int from,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return userService.findAllInitiatorsSortedByLikesRatio(from, size);
    }

    @GetMapping("/ratings/top")
    public UserRatingDto findMostLikedInitiator() {
        return userService.findMostLikedInitiator();
    }
}
