package com.practice.afisha.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByEventInitiatorId(int initiatorId);

    Optional<Request> findAllByRequesterIdAndEventId(int requesterId, int eventId);

    List<Request> findAllByRequesterId(int requesterId);
}
