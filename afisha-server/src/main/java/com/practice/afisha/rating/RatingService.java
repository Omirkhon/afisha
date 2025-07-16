package com.practice.afisha.rating;

import com.practice.afisha.error.ConflictException;
import com.practice.afisha.error.NotFoundException;
import com.practice.afisha.event.Event;
import com.practice.afisha.event.EventRatingDto;
import com.practice.afisha.event.EventRepository;
import com.practice.afisha.request.Request;
import com.practice.afisha.request.RequestRepository;
import com.practice.afisha.request.RequestStatus;
import com.practice.afisha.user.User;
import com.practice.afisha.user.UserRatingDto;
import com.practice.afisha.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public Rating create(boolean liked, int userId, int eventId) {
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
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setEvent(event);

        if (liked) {
            rating.setStatus(RatingStatus.LIKED);
        } else {
            rating.setStatus(RatingStatus.DISLIKED);
        }
        rating.setRatedAt(LocalDateTime.now().withNano(0));

        return ratingRepository.save(rating);
    }

    public Rating update(boolean liked, int ratingId, int userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Не найдено"));

        if (rating.getUser().getId() != userId) {
            throw new ConflictException("Нельзя менять оценку оставленную не вами.");
        }

        if (liked && rating.getStatus() != RatingStatus.LIKED) {
            rating.setStatus(RatingStatus.LIKED);
            rating.setRatedAt(LocalDateTime.now().withNano(0));
            return ratingRepository.save(rating);
        } else if (!liked && rating.getStatus() != RatingStatus.DISLIKED){
            rating.setStatus(RatingStatus.DISLIKED);
            rating.setRatedAt(LocalDateTime.now().withNano(0));
            return ratingRepository.save(rating);
        }
        return rating;
    }

    public void delete(int ratingId, int userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Не найдено."));
        if (rating.getUser().getId() != userId) {
            throw new ConflictException("Нельзя удалять оценку оставленную не вами.");
        }
        ratingRepository.deleteById(ratingId);
    }

    public List<Rating> findByEventId(int eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return ratingRepository.findAllByEventId(pageable, eventId).getContent();
    }

    public List<Rating> findByUserId(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return ratingRepository.findAllByUserId(pageable, userId).getContent();
    }

    public List<Rating> findRatingsForAllUsersEvents(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return ratingRepository.findAllRatingsForUsersEvents(pageable, userId).getContent();
    }

    public List<EventRatingDto> findAllSortedByLikesRatio(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findAllSortedByLikesRatio(pageable).getContent();
    }

    public EventRatingDto findTheMostLikedEvent() {
        return eventRepository.findTheMostLikedEvent();
    }

    public List<UserRatingDto> findAllInitiatorsSortedByLikesRatio(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return userRepository.findAllInitiatorsSortedByLikesRatio(pageable).getContent();
    }

    public UserRatingDto findMostLikedInitiator() {
        return userRepository.findMostLikedInitiator();
    }
}
