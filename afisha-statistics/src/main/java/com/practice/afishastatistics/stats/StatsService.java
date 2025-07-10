package com.practice.afishastatistics.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    public List<ViewStats> getStatsInfo(LocalDateTime startDate, LocalDateTime endDate, String[] uris, boolean unique) {
        if (uris != null && unique) {
            List<String> uriList = List.of(uris);
            return statsRepository.findAllByTimestampAfterAndTimestampBeforeAndUriInAndUniqueHits(startDate, endDate, uriList);
        } else if (uris != null) {
            List<String> uriList = List.of(uris);
            return statsRepository.findAllByTimestampAfterAndTimestampBeforeAndUriIn(startDate, endDate, uriList);
        } else if (unique) {
            return statsRepository.findAllByTimestampAfterAndTimestampBeforeAndUniqueHits(startDate, endDate);
        } else {
            return statsRepository.findAllByTimestampAfterAndTimestampBefore(startDate, endDate);
        }
    }

    public void saveRequestInfo(EndpointHit endpointHit, LocalDateTime date) {
        Visit visit = new Visit();

        visit.setIp(endpointHit.getIp());
        visit.setApp(endpointHit.getApp());
        visit.setUri(endpointHit.getUri());
        visit.setTimestamp(date);

        statsRepository.save(visit);
    }
}
