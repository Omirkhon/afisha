package com.practice.afisha.rating;

import com.practice.afisha.utils.DateFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingMapper {
    public RatingDto toDto(Rating rating) {
        RatingDto ratingDto = new RatingDto();

        ratingDto.setStatus(rating.getStatus().toString());
        ratingDto.setRatedAt(DateFormatter.toString(rating.getRatedAt()));
        ratingDto.setUserName(rating.getUser().getName());
        ratingDto.setEventTitle(rating.getEvent().getTitle());

        return ratingDto;
    }

    public List<RatingDto> toDto(List<Rating> ratings) {
        return ratings.stream().map(this::toDto).toList();
    }
}
