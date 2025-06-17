package com.practice.afisha.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByEventInitiatorId(int initiatorId);

    List<Request> findAllByRequesterIdAndEventId(int requesterId, int eventId);

    List<Request> findAllByRequesterId(int requesterId);
}
