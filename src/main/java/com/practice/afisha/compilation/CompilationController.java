package com.practice.afisha.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return compilationMapper.toDto(compilationService.findAll(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto findById(@PathVariable int compId) {
        return compilationMapper.toDto(compilationService.findById(compId));
    }
}
