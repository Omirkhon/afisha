package com.practice.afisha.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException e) {
        return new ApiError(e.getMessage(), "For the requested operation the conditions are not met.", "CONFLICT", LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConflict(final BadRequestException e) {
        return new ApiError(e.getMessage(), "Incorrectly made request.", "BAD_REQUEST", LocalDateTime.now().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleConflict(final NotFoundException e) {
        return new ApiError(e.getMessage(), "The required object was not found.", "NOT_FOUND", LocalDateTime.now().toString());
    }
}
