package com.practice.afisha.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ApiError {
    final List<String> errors = new ArrayList<>();
    String message;
    String reason;
    String status;
    String timestamp;
}
