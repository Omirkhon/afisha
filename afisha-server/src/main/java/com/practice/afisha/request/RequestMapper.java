package com.practice.afisha.request;

import com.practice.afisha.utils.DateFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequest = new ParticipationRequestDto();
        participationRequest.setId(request.getId());
        participationRequest.setRequester(request.getRequester().getId());
        participationRequest.setEvent(request.getEvent().getId());
        participationRequest.setStatus(request.getStatus().toString());
        participationRequest.setCreated(DateFormatter.toString(request.getCreated()));

        return participationRequest;
    }

    public List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests.stream().map(this::toParticipationRequestDto).toList();
    }

    public EventRequestStatusUpdateResult toUpdateResult(List<Request> requests) {
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        requests.stream().forEach(request -> {
            if (request.getStatus() == RequestStatus.CONFIRMED)
                updateResult.getConfirmedRequests().add(toParticipationRequestDto(request));
            else if (request.getStatus() == RequestStatus.REJECTED)
                updateResult.getRejectedRequests().add(toParticipationRequestDto(request));
        });

        return updateResult;
    }
}
