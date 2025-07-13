package com.practice.afisha.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "Допустимая длина аннотации 20-2000 символов")
    @NotBlank(message = "Аннотация не может быть пустой")
    String annotation;
    int category;
    @Size(min = 20, max = 7000, message = "Допустимая длина описания 20-7000 символов")
    @NotBlank(message = "Описание не может быть пустым")
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;
    @Size(min = 3, max = 120, message = "Допустимая длина название 3-120 символов")
    @NotBlank(message = "Название не может быть пустым")
    String title;
}
