package com.practice.afisha.event;

import com.practice.afisha.category.CategoryMapper;
import com.practice.afisha.user.UserMapper;
import com.practice.afisha.utils.DateFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventShortDto toShortDto(Event event) {
        EventShortDto shortDto = new EventShortDto();
        shortDto.setId(event.getId());
        shortDto.setAnnotation(event.getAnnotation());
        shortDto.setPaid(event.isPaid());
        shortDto.setEventDate(DateFormatter.toString(event.getEventDate()));
        shortDto.setConfirmedRequests(event.getConfirmedRequests());
        shortDto.setTitle(event.getTitle());
        shortDto.setInitiator(userMapper.toShortDto(event.getInitiator()));
        shortDto.setCategory(categoryMapper.toDto(event.getCategory()));

        return shortDto;
    }

    public List<EventShortDto> toShortDto(List<Event> events) {
        return events.stream().map(this::toShortDto).toList();
    }

    public EventFullDto toFullDto(Event event, Long views) {
        EventFullDto fullDto = new EventFullDto();

        fullDto.setId(event.getId());
        fullDto.setAnnotation(event.getAnnotation());
        fullDto.setCategory(categoryMapper.toDto(event.getCategory()));
        fullDto.setConfirmedRequests(event.getConfirmedRequests());
        fullDto.setCreatedOn(DateFormatter.toString(event.getCreatedOn()));
        fullDto.setDescription(event.getDescription());
        fullDto.setEventDate(DateFormatter.toString(event.getEventDate()));
        fullDto.setInitiator(userMapper.toShortDto(event.getInitiator()));
        fullDto.setLocation(new Location(event.getLat(), event.getLon()));
        fullDto.setPaid(event.isPaid());
        fullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            fullDto.setPublishedOn(event.getPublishedOn().toString());
        }
        fullDto.setRequestModeration(event.isRequestModeration());
        fullDto.setState(event.getState().toString());
        fullDto.setTitle(event.getTitle());
        fullDto.setViews(views);

        return fullDto;
    }

    public List<EventFullDto> toFullDto(List<Event> events) {
        return events.stream().map(event -> toFullDto(event, 0L)).toList();
    }
}
