package com.practice.afisha.event;

import com.practice.afisha.category.CategoryDto;
import com.practice.afisha.user.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String eventDate;
    int id;
    UserShortDto initiator;
    boolean paid;
    String title;
    int views;
}
