package com.practice.afisha.compilation;

import com.practice.afisha.event.Event;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String title;
    Boolean pinned;
    @ManyToMany
    @JoinTable(
            name = "events_compilations",
    joinColumns = @JoinColumn(name = "compilation_id"),
    inverseJoinColumns = @JoinColumn(name = "event_id"))
    final List<Event> events = new ArrayList<>();
}
