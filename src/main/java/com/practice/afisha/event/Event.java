package com.practice.afisha.event;

import com.practice.afisha.category.Category;
import com.practice.afisha.compilation.Compilation;
import com.practice.afisha.request.Request;
import com.practice.afisha.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    String description;
    @JoinColumn(name = "event_date")
    LocalDateTime eventDate;
    @JoinColumn(name = "created_on")
    LocalDateTime createdOn;
    @JoinColumn(name = "published_on")
    LocalDateTime publishedOn;
    boolean paid;
    @JoinColumn(name = "request_moderation")
    boolean requestModeration;
    String annotation;
    @JoinColumn(name = "participant_limit")
    int participantLimit;
    @Enumerated
    EventState state;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    int views;
    @JoinColumn(name = "confirmed_requests")
    int confirmedRequests;
    float lon;
    float lat;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @OneToMany(mappedBy = "event")
    final List<Request> requests = new ArrayList<>();
    @ManyToMany
    final List<Compilation> compilations = new ArrayList<>();
}
