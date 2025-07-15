package com.practice.afisha.rating;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RatingDto {
    String status;
    String ratedAt;
    String userName;
    String eventTitle;
}
