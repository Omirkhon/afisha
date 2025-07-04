package com.practice.afisha.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newCompilation) {
        return compilationMapper.toDto(compilationService.create(newCompilation));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void delete(@PathVariable int compId) {
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable int compId, @RequestBody @Valid UpdateCompilationRequest compilationUpdate) {
        return compilationMapper.toDto(compilationService.update(compId, compilationUpdate));
    }
}
