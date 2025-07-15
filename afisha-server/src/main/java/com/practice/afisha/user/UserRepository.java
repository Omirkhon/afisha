package com.practice.afisha.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findByIdIn(List<Integer> ids, Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("select u.id, u.name, " +
            "(count(case r.status when 'LIKED' then 1 end) - count(case r.status when 'DISLIKED' then 1 end)) " +
            "from User u " +
            "join Event e on u.id = e.initiator.id " +
            "join Rating r on e.id = r.event.id " +
            "group by u.id, u.name " +
            "order by (count(case r.status when 'LIKED' then 1 end) - count(case r.status when 'DISLIKED' then 1 end)) desc ")
    Page<UserRatingDto> findAllInitiatorsSortedByLikesRatio(Pageable pageable);

    @Query("select u.id, u.name, " +
            "(count(case r.status when 'LIKED' then 1 end) - count(case r.status when 'DISLIKED' then 1 end)) " +
            "from User u " +
            "join Event e on u.id = e.initiator.id " +
            "join Rating r on e.id = r.event.id " +
            "group by u.id, u.name " +
            "order by (count(case r.status when 'LIKED' then 1 end) - count(case r.status when 'DISLIKED' then 1 end)) desc " +
            "limit 1")
    UserRatingDto findMostLikedInitiator();
}
