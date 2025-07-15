package com.practice.afisha.rating;

import com.practice.afisha.event.Event;
import com.practice.afisha.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Enumerated(value = EnumType.STRING)
    RatingStatus status;
    @JoinColumn(name = "rated_at")
    LocalDateTime ratedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
}
