package com.practice.afishastatistics.stats;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
