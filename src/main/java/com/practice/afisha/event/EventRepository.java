package com.practice.afisha.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiatorId(int initiatorId, Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(List<Integer> users, List<EventState> states,
                                                                                                   List<Integer> categories, LocalDateTime rangeStart,
                                                                                                   LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndCategoryIdInAndEventDateAfterAndEventDateBefore(List<Integer> users, List<Integer> categories,
                                                                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndStateInAndEventDateAfterAndEventDateBefore(List<Integer> users, List<EventState> states,
                                                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(List<EventState> states, List<Integer> categories, LocalDateTime rangeStart,
                                                                                   LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByInitiatorIdInAndEventDateAfterAndEventDateBefore(List<Integer> users, LocalDateTime rangeStart,
                                                                          LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByCategoryIdInAndEventDateAfterAndEventDateBefore(List<Integer> categories, LocalDateTime rangeStart,
                                                                         LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByEventDateAfterAndEventDateBefore(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Event> findAllByStateInAndEventDateAfterAndEventDateBefore(List<EventState> states, LocalDateTime rangeStart,
                                                                    LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullAndCategoryIdInOrderByEventDate(String text, String text2,
                                                                                                                                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                                               boolean paid, List<Integer> categoryIds,
                                                                                                                                                               Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullAndCategoryIdInOrderByViews(String text, String text2,
                                                                                                                                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                                           boolean paid, List<Integer> categoryIds,
                                                                                                                                                           Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullOrderByEventDate(String text, String text2,
                                                                                                                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                                boolean paid, Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullOrderByViews(String text, String text2,
                                                                                                                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                            boolean paid, Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPublishedOnNotNullOrderByEventDate(String text, String text2,
                                                                                                                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                         Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPublishedOnNotNullOrderByViews(String text, String text2,
                                                                                                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                     Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.paid = ?5 " +
            "and e.category.id in ?6 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.views")
    Page<Event> findAvailableEventsByCategoryIdInAndPaidOrderByViews(String text, String text2,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          boolean paid, List<Integer> categoryIds,
                                          Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.category.id in ?5 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.views")
    Page<Event> findAvailableEventsByCategoryIdInOrderByViews(String text, String text2,
                                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                              List<Integer> categoryIds, Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.paid = ?5 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.views")
    Page<Event> findAvailableEventsByPaidOrderByViews(String text, String text2,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      boolean paid, Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.views")
    Page<Event> findAvailableEventsOrderByViews(String text, String text2,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.paid = ?5 " +
            "and e.category.id in ?6 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.eventDate")
    Page<Event> findAvailableEventsByCategoryIdInAndPaidOrderByEventDate(String text, String text2,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    boolean paid, List<Integer> categoryIds,
                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.category.id in ?5 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.eventDate")
    Page<Event> findAvailableEventsByCategoryIdInOrderByEventDate(String text, String text2,
                                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                  List<Integer> categoryIds, Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.paid = ?5 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.eventDate")
    Page<Event> findAvailableEventsByPaidOrderByEventDate(String text, String text2,
                                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                          boolean paid, Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests " +
            "order by e.eventDate")
    Page<Event> findAvailableEventsOrderByEventDate(String text, String text2,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.paid = ?5 " +
            "and e.category.id in ?6 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests ")
    Page<Event> findAvailableEventsByCategoryIdInAndPaid(String text, String text2,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    boolean paid, List<Integer> categoryIds,
                                                    Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.category.id in ?5 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests ")
    Page<Event> findAvailableEventsCategoryIdIn(String text, String text2,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       List<Integer> categoryIds, Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.paid = ?5 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests ")
    Page<Event> findAvailableEventsByPaid(String text, String text2,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               boolean paid, Pageable pageable);

    @Query("select e from Event e " +
            "where e.annotation like %?1% " +
            "and e.description like %?2% " +
            "and e.eventDate > ?3 " +
            "and e.eventDate < ?4 " +
            "and e.state = 'PUBLISHED' " +
            "and e.participantLimit > e.confirmedRequests ")
    Page<Event> findAvailableEvents(String text, String text2,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Pageable pageable);

    Optional<Event> findByIdAndState(int id, EventState state);

    List<Event> findByIdIn(Collection<Integer> ids);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullAndCategoryIdIn(String text, String text2,
                                                                                                                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                               boolean paid, List<Integer> categoryIds,
                                                                                                                                               Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPublishedOnNotNullAndCategoryIdIn(String text, String text2,
                                                                                                                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                        List<Integer> categoryIds,
                                                                                                                                        Pageable pageable);

    Page<Event> findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNull(String text, String text2,
                                                                                                                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                                                                                               boolean paid, Pageable pageable);
}
