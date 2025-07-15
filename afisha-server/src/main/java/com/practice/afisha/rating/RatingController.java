package com.practice.afisha.rating;

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
}
