package com.practice.afisha.event;

import com.practice.afisha.category.CategoryDto;
import com.practice.afisha.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    int id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    UserShortDto initiator;
    Location location;
    boolean paid;
    int participantLimit;
    String publishedOn;
    boolean requestModeration;
    String state;
    String title;
    Long views;
}
