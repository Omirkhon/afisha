package com.practice.afisha.event;

import com.practice.afisha.category.CategoryRepository;
import com.practice.afisha.error.BadRequestException;
import com.practice.afisha.error.ConflictException;
import com.practice.afisha.error.NotFoundException;
import com.practice.afisha.utils.DateFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public List<Event> findFilteredEvents(Integer[] users, String[] states,
                                          Integer[] categories, String rangeStart,
                                          String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart != null) {
            start = DateFormatter.toLocalDateTime(rangeStart);
        } else {
            start = LocalDateTime.now();
        }
        if (rangeEnd != null) {
            end = DateFormatter.toLocalDateTime(rangeEnd);
        } else {
            end = LocalDateTime.of(3000, 1, 1, 0, 0, 0);
        }

        if (states != null) {
            List<EventState> eventStates = new ArrayList<>();

            for (String state : states) {
                switch (state) {
                    case "PUBLISHED":
                        eventStates.add(EventState.PUBLISHED);
                    case "CANCELED":
                        eventStates.add(EventState.CANCELED);
                    case "PENDING":
                        eventStates.add(EventState.PENDING);
                }
            }
            if (users != null && categories != null) {
                List<Integer> usersList = List.of(users);
                List<Integer> categoriesList = List.of(categories);

                return eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBeforeOrderById(
                        usersList, eventStates, categoriesList, start, end, pageable).getContent();
            }
            if (users != null) {
                List<Integer> usersList = List.of(users);

                return eventRepository.findAllByInitiatorIdInAndStateInAndEventDateAfterAndEventDateBeforeOrderById(
                        usersList, eventStates, start, end, pageable).getContent();
            }
            if (categories != null) {
                List<Integer> categoriesList = List.of(categories);

                return eventRepository.findAllByStateInAndCategoryIdInAndEventDateAfterAndEventDateBeforeOrderById(
                        eventStates, categoriesList, start, end, pageable).getContent();
            }

            return eventRepository.findAllByStateInAndEventDateAfterAndEventDateBeforeOrderById(
                    eventStates, start, end, pageable).getContent();
        } else {
            if (users != null && categories != null) {
                List<Integer> usersList = List.of(users);
                List<Integer> categoriesList = List.of(categories);

                return eventRepository.findAllByInitiatorIdInAndCategoryIdInAndEventDateAfterAndEventDateBeforeOrderById(
                        usersList, categoriesList, start, end, pageable).getContent();
            }
            if (users != null) {
                List<Integer> usersList = List.of(users);

                return eventRepository.findAllByInitiatorIdInAndEventDateAfterAndEventDateBeforeOrderById(
                        usersList, start, end, pageable).getContent();
            }
            if (categories != null) {
                List<Integer> categoriesList = List.of(categories);

                return eventRepository.findAllByCategoryIdInAndEventDateAfterAndEventDateBeforeOrderById(
                        categoriesList, start, end, pageable).getContent();
            } else {
                return eventRepository.findAllByEventDateAfterAndEventDateBeforeOrderById(
                        start, end, pageable).getContent();
            }
        }
    }

    public Event update(int eventId, UpdateEventAdminRequest updatedEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие по id=" + eventId + " не найдено."));

        if (updatedEvent.getEventDate() != null && DateFormatter.toLocalDateTime(updatedEvent.getEventDate()).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Нельзя менять дату на уже наступившую.");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Нельзя изменять события меньше чем за час до их начала.");
        }

        if (updatedEvent.getStateAction() != null &&
                updatedEvent.getStateAction().equals(AdminStateAction.PUBLISH_EVENT.toString())
                && event.getState() != EventState.PENDING) {
            throw new ConflictException("Публиковать можно только события находящиеся в ожидании.");
        }

        if (updatedEvent.getStateAction() != null &&
                updatedEvent.getStateAction().equals(AdminStateAction.REJECT_EVENT.toString())
                && event.getState() != EventState.PENDING) {
            throw new ConflictException("Отклонять можно только события находящиеся в ожидании.");
        }

        if (updatedEvent.getStateAction() != null &&
                updatedEvent.getStateAction()
                        .equals(AdminStateAction.PUBLISH_EVENT.toString())) {
            event.setState(EventState.PUBLISHED);
        } else if (updatedEvent.getStateAction() != null && updatedEvent.getStateAction().equals(AdminStateAction.REJECT_EVENT.toString())) {
            event.setState(EventState.CANCELED);
        }

        if (updatedEvent.getAnnotation() != null) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != 0) {
            event.setCategory(categoryRepository.findById(updatedEvent.getCategory()).orElseThrow(() -> new NotFoundException("Категория по id=" + eventId + " не найдена.")));
        }
        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getEventDate() != null) {
            event.setEventDate(DateFormatter.toLocalDateTime(updatedEvent.getEventDate()));
        }
        if (updatedEvent.getLocation() != null) {
            event.setLat(updatedEvent.getLocation().getLat());
            event.setLon(updatedEvent.getLocation().getLon());
        }
        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }
        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }

        return eventRepository.save(event);
    }

    public List<Event> findFilteredAndSortedEvents(String text, Integer[] categories, Boolean paid,
                                                   String rangeStart, String rangeEnd,
                                                   boolean onlyAvailable, String sort,
                                                   int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.of(3000, 1, 1, 1, 1, 1);
        } else {
            start = DateFormatter.toLocalDateTime(rangeStart);
            end = DateFormatter.toLocalDateTime(rangeEnd);
        }

        if (start.isAfter(end)) {
            throw new BadRequestException("Некорректно указана дата.");
        }

        if (text == null) {
            text = "";
        }

        if (sort != null) {
            if (sort.equals("EVENT_DATE") && onlyAvailable && categories != null && paid != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAvailableEventsByCategoryIdInAndPaidOrderByEventDate(
                        text, text, start, end,
                        paid, categoriesIds, pageable).getContent();
            } else if (sort.equals("VIEWS") && onlyAvailable && categories != null && paid != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAvailableEventsByCategoryIdInAndPaid(text, text, start, end,
                        paid, categoriesIds, pageable).getContent();
            } else if (sort.equals("EVENT_DATE") && onlyAvailable && categories != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAvailableEventsByCategoryIdInOrderByEventDate(
                        text, text, start, end,
                        categoriesIds, pageable).getContent();
            } else if (sort.equals("VIEWS") && onlyAvailable && categories != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAvailableEventsByCategoryIdInOrderByViews(text, text, start, end,
                        categoriesIds, pageable).getContent();
            } else if (sort.equals("EVENT_DATE") && onlyAvailable && paid != null) {
                return eventRepository.findAvailableEventsByPaidOrderByEventDate(
                        text, text, start, end,
                        paid, pageable).getContent();
            } else if (sort.equals("VIEWS") && onlyAvailable && paid != null) {
                return eventRepository.findAvailableEventsByPaid(
                        text, text, start, end,
                        paid, pageable).getContent();
            } else if (sort.equals("EVENT_DATE") && onlyAvailable) {
                return eventRepository.findAvailableEventsOrderByEventDate(
                        text, text, start, end, pageable).getContent();
            } else if (sort.equals("VIEWS") && onlyAvailable) {
                return eventRepository.findAvailableEvents(
                        text, text, start, end, pageable).getContent();
            } else if (sort.equals("EVENT_DATE") && categories != null && paid != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullAndCategoryIdInOrderByEventDate(
                        text, text, start, end,
                        paid, categoriesIds, pageable).getContent();
            } else if (sort.equals("VIEWS") && categories != null && paid != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullAndCategoryIdIn(
                        text, text, start, end,
                        paid, categoriesIds, pageable).getContent();
            } else if (sort.equals("VIEWS") && paid != null) {
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNull(
                        text, text, start, end,
                        paid, pageable).getContent();
            } else if (sort.equals("EVENT_DATE") && paid != null) {
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullOrderByEventDate(
                        text, text, start, end,
                        paid, pageable).getContent();
            } else if (sort.equals("VIEWS")) {
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPublishedOnNotNull(
                        text, text, start, end, pageable).getContent();
            } else if (sort.equals("EVENT_DATE")) {
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPublishedOnNotNullOrderByEventDate(
                        text, text, start, end, pageable).getContent();
            }
        } else {
            if (onlyAvailable && categories != null && paid != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAvailableEventsByCategoryIdInAndPaid(
                        text, text, start, end,
                        paid, categoriesIds, pageable).getContent();
            } else if (onlyAvailable && categories != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAvailableEventsCategoryIdIn(
                        text, text, start, end,
                        categoriesIds, pageable).getContent();
            } else if (onlyAvailable && paid != null) {
                return eventRepository.findAvailableEventsByPaid(
                        text, text, start, end,
                        paid, pageable).getContent();
            } else if (categories != null && paid != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNullAndCategoryIdIn(
                        text, text, start, end,
                        paid, categoriesIds, pageable).getContent();
            } else if (categories != null) {
                List<Integer> categoriesIds = List.of(categories);
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPublishedOnNotNullAndCategoryIdIn(
                        text, text, start, end, categoriesIds, pageable).getContent();
            } else if (paid != null) {
                return eventRepository.findAllByAnnotationContainsOrDescriptionContainsAndEventDateAfterAndEventDateBeforeAndPaidAndPublishedOnNotNull(
                        text, text, start, end, paid, pageable).getContent();
            } else if (onlyAvailable) {
                return eventRepository.findAvailableEvents(
                        text, text, start, end, pageable).getContent();
            }
        }
        return List.of();
    }

    public Event findById(int id) {
        return eventRepository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(() -> new NotFoundException("Событие по id=" + id + " не найдено."));
    }
}
