package com.practice.afisha.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryDto {
    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 1, max = 50, message = "Допустимая длин названия 1-50 символов")
    String name;
}
