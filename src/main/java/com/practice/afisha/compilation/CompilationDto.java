package com.practice.afisha.compilation;

import com.practice.afisha.event.EventShortDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    boolean pinned;
    int id;
    @NotBlank(message = "Название не может быть пустым")
    String title;
    List<EventShortDto> events = new ArrayList<>();
}
