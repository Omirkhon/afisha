package com.practice.afisha.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @Size(min = 6, max = 254, message = "Допустимая длина эл. почты 6-254 символов")
    @NotBlank(message = "Эл. почта не может быть пустой")
    String email;
    @Size(min = 2, max = 250, message = "Допустимая длина названия 2-250 символов")
    @NotBlank(message = "Имя не может быть пустым")
    String name;
}
