package com.practice.afisha.event;

import com.practice.afisha.utils.DateFormatter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE)
public class EventRatingDto {
    int id;
    String title;
    String description;
    String annotation;
    int confirmedRequests;
    String eventDate;
    boolean paid;
    long likesRatio;

    public EventRatingDto(int id, String title, String description, String annotation,
                          int confirmedRequests, LocalDateTime eventDate,
                          boolean paid, long likesRatio) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.annotation = annotation;
        this.confirmedRequests = confirmedRequests;
        this.eventDate = DateFormatter.toString(eventDate);
        this.paid = paid;
        this.likesRatio = likesRatio;
    }
}
