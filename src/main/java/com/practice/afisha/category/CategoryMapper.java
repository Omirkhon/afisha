package com.practice.afisha.category;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }

    public List<CategoryDto> toDto(List<Category> categories) {
        return categories.stream().map(this::toDto).toList();
    }
}
