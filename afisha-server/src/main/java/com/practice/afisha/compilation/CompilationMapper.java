package com.practice.afisha.compilation;

import com.practice.afisha.event.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationDto toDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(eventMapper.toShortDto(compilation.getEvents()));

        return compilationDto;
    }

    public List<CompilationDto> toDto(List<Compilation> compilations) {
        return compilations.stream().map(this::toDto).toList();
    }
}
