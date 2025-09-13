package com.practice.afisha.user;

import com.practice.afisha.category.Category;
import com.practice.afisha.category.CategoryRepository;
import com.practice.afisha.error.BadRequestException;
import com.practice.afisha.error.ConflictException;
import com.practice.afisha.error.NotFoundException;
import com.practice.afisha.event.*;
import com.practice.afisha.rating.Rating;
import com.practice.afisha.rating.RatingRepository;
import com.practice.afisha.rating.RatingStatus;
import com.practice.afisha.request.*;
import com.practice.afisha.utils.DateFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final RatingRepository ratingRepository;

    public List<Event> findAllUsersEvents(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return eventRepository.findAllByInitiatorId(userId, pageable).getContent();
    }

    public Event createEvent(int userId, NewEventDto newEvent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        LocalDateTime eventDate = DateFormatter.toLocalDateTime(newEvent.getEventDate());
        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Событие не может быть создано меньше чем за 2 часа до его начала.");
        }

        Event event = Event.builder()
                .annotation(newEvent.getAnnotation())
                .createdOn(LocalDateTime.now())
                .eventDate(eventDate)
                .category(category)
                .initiator(user)
                .description(newEvent.getDescription())
                .title(newEvent.getTitle())
                .lon(newEvent.getLocation().getLon())
                .lat(newEvent.getLocation().getLat())
                .state(EventState.PENDING)
                .build();

        if (newEvent.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }

        if (newEvent.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(newEvent.getPaid());
        }

        if (newEvent.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(newEvent.getRequestModeration());
        }

        eventRepository.save(event);
        return event;
    }

    public Event findEventById(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие по id=" + eventId + " не найдено."));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Событие по id=" + eventId + " не найдено.");
        }
        return event;
    }

    public Event updateEvent(int userId, int eventId, UpdateEventUserRequest eventUpdate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие по id=" + eventId + " не найдено."));

        if (event.getState() != EventState.CANCELED && event.getState() != EventState.PENDING) {
            throw new ConflictException("Только отмененные или события в ожидании могут быть изменены.");
        }
        if (eventUpdate.getEventDate() != null && DateFormatter.toLocalDateTime(eventUpdate.getEventDate()).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Переназначать можно только на 2 часа раньше времени события.");
        }
        if (eventUpdate.getAnnotation() != null) {
            event.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getEventDate() != null) {
            event.setEventDate(DateFormatter.toLocalDateTime(eventUpdate.getEventDate()));
        }
        if (eventUpdate.getCategory() != 0) {
            event.setCategory(categoryRepository.findById(eventUpdate.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        event.setInitiator(user);
        if (eventUpdate.getDescription() != null && !eventUpdate.getDescription().isBlank()) {
            event.setDescription(eventUpdate.getDescription());
        }
        if (eventUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdate.getParticipantLimit());
        }
        if (eventUpdate.getTitle() != null) {
            event.setTitle(eventUpdate.getTitle());
        }
        if (eventUpdate.getLocation() != null) {
            event.setLon(eventUpdate.getLocation().getLon());
            event.setLat(eventUpdate.getLocation().getLat());
        }
        if (eventUpdate.getPaid() != null) {
            event.setPaid(eventUpdate.getPaid());
        }
        if (eventUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdate.getRequestModeration());
        }
        if (eventUpdate.getStateAction() != null && eventUpdate.getStateAction().equals(UserStateAction.CANCEL_REVIEW.toString())) {
            event.setState(EventState.CANCELED);
        } else if (eventUpdate.getStateAction() != null && eventUpdate.getStateAction().equals(UserStateAction.SEND_TO_REVIEW.toString())) {
            event.setState(EventState.PENDING);
        }

        return eventRepository.save(event);
    }

    public List<Request> findUserRequestsForEvent(int userId, int eventId) {
        if (eventRepository.findById(eventId).
                orElseThrow(() -> new NotFoundException("Событие по id=" + eventId + "не найдеено."))
                .getInitiator().getId() != userId) {
            throw new ConflictException("Вы не организатор события.");
        }
        return requestRepository.findAllByEventInitiatorId(userId);
    }

    public List<Request> updateRequestStatus(int userId, int eventId, EventRequestStatusUpdateRequest requestUpdate) {
        List<Request> requests = new ArrayList<>();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие по id=" + eventId + " не найдено."));
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Вы не организатор события");
        }
        for (Integer requestId : requestUpdate.getRequestIds()) {
            Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос по id=" + requestId + " не найден."));

            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Только запросы в ожидании могут быть отклонены или подтверждены.");
            }
            if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()
                    && event.isRequestModeration()) {
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                throw new ConflictException("Запросы на участие больше не принимаются");
            } else if (requestUpdate.getStatus().equals("CONFIRMED") || !event.isRequestModeration()) {
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                requestRepository.save(request);
                requests.add(request);
            } else if (requestUpdate.getStatus().equals("REJECTED")) {
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                requests.add(request);
            }
        }
        return requests;
    }

    public List<Request> findUserRequests(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + "не найден."));
        return requestRepository.findAllByRequesterId(userId);
    }

    public Request createRequest(int userId, int eventId) {
        if (requestRepository.findAllByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Запрос на участие в событии уже существует.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + " не найден."));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие по id=" + userId + " не найдено."));
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Нельзя подать запрос на участие в своем событии.");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ConflictException("Лимит запросов на участие в событии превышен.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Событие не опубликовано");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now().withNano(0))
                .requester(user)
                .event(event)
                .build();

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return requestRepository.save(request);
    }

    public Request cancelOwnRequest(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + "не найден."));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос по id=" + userId + "не найден."));
        if (request.getRequester().getId() != userId) {
            throw new NotFoundException("Запрос не доступен");
        }
        request.setStatus(RequestStatus.CANCELED);

        return requestRepository.save(request);
    }

    public List<User> findFilteredUsers(Integer[] ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids == null) {
            return userRepository.findAll(pageable).getContent();
        }
        List<Integer> usersIds = List.of(ids);
        return userRepository.findByIdIn(usersIds, pageable).getContent();
    }

    public User create(NewUserRequest newUser) {
        if (!userRepository.findByEmail(newUser.getEmail()).equals(Optional.empty())) {
            throw new ConflictException("Пользователь с такой эл. почтой уже сущестует");
        }

        User user = User.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .build();

        return userRepository.save(user);
    }

    public void delete(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + "не найден."));
        userRepository.deleteById(userId);
    }

    public Rating createRating(boolean liked, int userId, int eventId) {
        Request request = requestRepository.findAllByRequesterIdAndEventId(userId, eventId)
                .orElseThrow(() -> new ConflictException("Оценку можно оставить только в случае участия в событии."));

        if (request.getStatus() != RequestStatus.CONFIRMED) {
            throw new ConflictException("Оценку можно оставить только в случае участия в событии.");
        }

        if (ratingRepository.findByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Вы уже оставили оценку этому событию.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь по id=" + userId + " не найден."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие по id=" + eventId + " не найдено."));

        Rating rating = Rating.builder()
                .user(user)
                .event(event)
                .ratedAt(LocalDateTime.now()
                .withNano(0)).build();

        if (liked) {
            rating.setStatus(RatingStatus.LIKED);
        } else {
            rating.setStatus(RatingStatus.DISLIKED);
        }

        return ratingRepository.save(rating);
    }

    public Rating updateRating(boolean liked, int ratingId, int userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Не найдено"));

        if (rating.getUser().getId() != userId) {
            throw new ConflictException("Нельзя менять оценку оставленную не вами.");
        }

        if (liked && rating.getStatus() != RatingStatus.LIKED) {
            rating.setStatus(RatingStatus.LIKED);
            rating.setRatedAt(LocalDateTime.now().withNano(0));
            return ratingRepository.save(rating);
        } else if (!liked && rating.getStatus() != RatingStatus.DISLIKED) {
            rating.setStatus(RatingStatus.DISLIKED);
            rating.setRatedAt(LocalDateTime.now().withNano(0));
            return ratingRepository.save(rating);
        }
        return rating;
    }

    public void deleteRating(int ratingId, int userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Не найдено."));
        if (rating.getUser().getId() != userId) {
            throw new ConflictException("Нельзя удалять оценку оставленную не вами.");
        }
        ratingRepository.deleteById(ratingId);
    }

    public List<Rating> findRatingsByEventId(int eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return ratingRepository.findAllByEventId(pageable, eventId).getContent();
    }

    public List<Rating> findRatingsByUserId(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return ratingRepository.findAllByUserId(pageable, userId).getContent();
    }

    public List<Rating> findRatingsForAllUsersEvents(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return ratingRepository.findAllRatingsForUsersEvents(pageable, userId).getContent();
    }

    public List<EventRatingDto> findAllEventsSortedByLikesRatio(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findAllSortedByLikesRatio(pageable).getContent();
    }

    public EventRatingDto findTheMostLikedEvent() {
        return eventRepository.findTheMostLikedEvent()
                .orElseThrow(() -> new NotFoundException("Рейтинг отсутствует."));
    }

    public List<UserRatingDto> findAllInitiatorsSortedByLikesRatio(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return userRepository.findAllInitiatorsSortedByLikesRatio(pageable).getContent();
    }

    public UserRatingDto findMostLikedInitiator() {
        return userRepository.findMostLikedInitiator()
                .orElseThrow(() -> new NotFoundException("Рейтинг отсутствует."));
    }
}
