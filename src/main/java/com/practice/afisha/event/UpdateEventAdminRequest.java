package com.practice.afisha.event;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "Допустимая длина аннотации 20-2000 символов")
    String annotation;
    int category;
    @Size(min = 20, max = 7000, message = "Допустимая длина описания 20-7000 символов")
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    @Size(min = 3, max = 120, message = "Допустимая длина название 3-120 символов")
    String title;
}
