package com.practice.afisha.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class EndpointHit {
    Integer id;
    String app;
    String uri;
    String ip;
    final String timestamp = DateFormatter.toString(LocalDateTime.now().withNano(0));
}
