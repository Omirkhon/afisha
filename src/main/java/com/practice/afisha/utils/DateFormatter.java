package com.practice.afisha.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateFormatter {
    final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String dateInString) {
        return LocalDateTime.parse(dateInString, FORMATTER);
    }

    public static String toString(LocalDateTime datetime) {
        return datetime.toLocalDate().toString() + " " + datetime.toLocalTime().toString();
    }
}
