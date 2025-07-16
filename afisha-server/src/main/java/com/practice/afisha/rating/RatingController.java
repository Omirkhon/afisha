package com.practice.afisha.rating;

import com.practice.afisha.event.EventRatingDto;
import com.practice.afisha.user.UserRatingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final RatingMapper ratingMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/{eventId}")
    public RatingDto create(@RequestParam boolean liked, @PathVariable int userId, @PathVariable int eventId) {
        return ratingMapper.toDto(ratingService.create(liked, userId, eventId));
    }

    @PatchMapping("/{ratingId}/{userId}")
    public RatingDto update(@RequestParam boolean liked, @PathVariable int ratingId, @PathVariable int userId) {
        return ratingMapper.toDto(ratingService.update(liked, ratingId, userId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{ratingId}{userId}/")
    public void delete(@PathVariable int ratingId, @PathVariable int userId) {
        ratingService.delete(ratingId, userId);
    }

    @GetMapping("/events/{eventId}")
    public List<RatingDto> findByEventId(@PathVariable int eventId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return ratingMapper.toDto(ratingService.findByEventId(eventId, from, size));
    }

    @GetMapping("/events/likes")
    public List<EventRatingDto> findAllByLikesRatio(@RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ratingService.findAllSortedByLikesRatio(from, size);
    }

    @GetMapping("/events/top")
    public EventRatingDto findTheMostLikedEvent() {
        return ratingService.findTheMostLikedEvent();
    }

    @GetMapping("/users/{userId}")
    public List<RatingDto> findByUserId(@PathVariable int userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return ratingMapper.toDto(ratingService.findByUserId(userId, from, size));
    }

    @GetMapping("/initiators/{userId}")
    public List<RatingDto> findRatingsForAllUsersEvents(@PathVariable int userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return ratingMapper.toDto(ratingService.findRatingsForAllUsersEvents(userId, from, size));
    }

    @GetMapping("/initiators/likes")
    public List<UserRatingDto> findAllInitiatorsSortedByLikesRatio(@RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ratingService.findAllInitiatorsSortedByLikesRatio(from, size);
    }

    @GetMapping("/initiators/top")
    public UserRatingDto findMostLikedInitiator() {
        return ratingService.findMostLikedInitiator();
    }
}
