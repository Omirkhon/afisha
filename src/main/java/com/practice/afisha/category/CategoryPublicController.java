package com.practice.afisha.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        return categoryMapper.toDto(categoryService.findAll(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto findById(@PathVariable int catId) {
        return categoryMapper.toDto(categoryService.findById(catId));
    }
}
