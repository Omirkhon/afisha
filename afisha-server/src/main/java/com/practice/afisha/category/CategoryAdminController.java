package com.practice.afisha.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto create(@RequestBody @Valid NewCategoryDto category) {
        return categoryMapper.toDto(categoryService.create(category));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void delete(@PathVariable int catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable int catId, @RequestBody @Valid NewCategoryDto newCategory) {
        return categoryMapper.toDto(categoryService.update(catId, newCategory));
    }
}
