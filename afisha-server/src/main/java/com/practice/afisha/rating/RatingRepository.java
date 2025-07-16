package com.practice.afisha.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByUserIdAndEventId(int userId, int eventId);

    Page<Rating> findAllByEventId(Pageable pageable, int eventId);

    Page<Rating> findAllByUserId(Pageable pageable, int userId);

    @Query("select r from Rating r " +
            "join Event e on e.id = r.event.id " +
            "join User u on u.id = e.initiator.id " +
            "where u.id = ?1")
    Page<Rating> findAllRatingsForUsersEvents(Pageable pageable, int userId);
}
