package com.practice.afisha.user;

import com.practice.afisha.event.Event;
import com.practice.afisha.request.Request;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String email;
    String name;
    @OneToMany(mappedBy = "requester")
    final List<Request> requests = new ArrayList<>();
    @OneToMany(mappedBy = "initiator")
    final List<Event> events = new ArrayList<>();
}
