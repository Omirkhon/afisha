package com.practice.afishastatistics.stats;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatsMapper {
    public ViewStats toView(Visit visit, Long hits) {
        return new ViewStats(visit.getApp(), visit.getUri(), hits);
    }

    public List<ViewStats> toView(List<Visit> visits, List<Long> hits) {
        return visits.stream().map(visit -> toView(visit, hits.get(visits.indexOf(visit)))).toList();
    }
}
