package com.practice.afisha.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByUserIdAndEventId(int userId, int eventId);

    Page<Rating> findAllByEventId(Pageable pageable, int eventId);
}
