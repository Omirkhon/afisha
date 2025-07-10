package com.practice.afishastatistics.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Visit, Integer> {

    @Query("select distinct v.uri, v.app, count(v.ip) from Visit v " +
            "where v.timestamp > ?1 " +
            "and v.timestamp < ?2 " +
            "and v.uri in ?3 " +
            "group by v.uri, v.app " +
            "order by count(v.ip) desc")
    List<ViewStats> findAllByTimestampAfterAndTimestampBeforeAndUriIn(LocalDateTime start,
                                                                      LocalDateTime end,
                                                                      List<String> uri);

    @Query("select distinct v.uri, v.app, count(distinct v.ip) from Visit v " +
            "where v.timestamp > ?1 " +
            "and v.timestamp < ?2 " +
            "and v.uri in ?3 " +
            "group by v.uri, v.app " +
            "order by count(distinct v.ip) desc")
    List<ViewStats> findAllByTimestampAfterAndTimestampBeforeAndUriInAndUniqueHits(LocalDateTime start,
                                                                                   LocalDateTime end,
                                                                                   List<String> uri);

    @Query("select distinct v.uri, v.app, count(v.ip) from Visit v " +
            "where v.timestamp > ?1 " +
            "and v.timestamp < ?2 " +
            "group by v.uri, v.app " +
            "order by count(v.ip) desc")
    List<ViewStats> findAllByTimestampAfterAndTimestampBefore(LocalDateTime start,
                                                              LocalDateTime end);

    @Query("select distinct v.uri, v.app, count(distinct v.ip) from Visit v " +
            "where v.timestamp > ?1 " +
            "and v.timestamp < ?2 " +
            "group by v.uri, v.app " +
            "order by count(distinct v.ip) desc")
    List<ViewStats> findAllByTimestampAfterAndTimestampBeforeAndUniqueHits(LocalDateTime start,
                                                                           LocalDateTime end);
}
