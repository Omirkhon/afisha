package com.practice.afisha.compilation;

import com.practice.afisha.error.NotFoundException;
import com.practice.afisha.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public List<Compilation> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (pinned == null) {
            return compilationRepository.findAll(pageable).getContent();
        } else {
            return compilationRepository.findAllByPinned(pageable, pinned).getContent();
        }
    }

    public Compilation findById(int compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка по id=" + compId + " не найдена."));
    }

    public Compilation create(NewCompilationDto newCompilation) {
        Compilation compilation = new Compilation();
        if (newCompilation.getPinned() != null) {
            compilation.setPinned(newCompilation.getPinned());
        } else {
            compilation.setPinned(false);
        }
        compilation.setTitle(newCompilation.getTitle());
        compilation.getEvents().addAll(eventRepository.findByIdIn(newCompilation.getEvents()));

        return compilationRepository.save(compilation);
    }

    public void delete(int compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка по id=" + compId + "не найдена."));
        compilationRepository.deleteById(compId);
    }

    public Compilation update(int compId, UpdateCompilationRequest compilationUpdate) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка по id=" + compId + " не найдена."));
        if (compilationUpdate.getPinned() != null) {
            compilation.setPinned(compilationUpdate.getPinned());
        }
        if (compilationUpdate.getTitle() != null) {
            compilation.setTitle(compilationUpdate.getTitle());
        }
        if (!compilationUpdate.getEvents().isEmpty()) {
            compilation.getEvents().addAll(eventRepository.findByIdIn(compilationUpdate.getEvents()));
        }

        return compilationRepository.save(compilation);
    }
}
