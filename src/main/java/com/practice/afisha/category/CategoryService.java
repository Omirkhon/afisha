package com.practice.afisha.category;

import com.practice.afisha.error.ConflictException;
import com.practice.afisha.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category create(NewCategoryDto newCategory) {
        if (!categoryRepository.findByName(newCategory.getName()).equals(Optional.empty())) {
            throw new ConflictException("Категория с таким названием уже существует.");
        }
        Category category = new Category();
        category.setName(newCategory.getName());

        return categoryRepository.save(category);
    }

    public void delete(int catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория по id" + catId +" не найдена."));
        if (!category.getEvents().isEmpty()) {
            throw new ConflictException("Существуют события связанные с категорией.");
        }
        categoryRepository.deleteById(catId);
    }

    public Category update(int catId, NewCategoryDto newCategory) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория по id" + catId +" не найдена."));

        if (!categoryRepository.findByName(newCategory.getName()).equals(Optional.empty())
        && !newCategory.getName().equals(category.getName())) {
            throw new ConflictException("Категория с таким названием уже существует.");
        }

        category.setName(newCategory.getName());

        return categoryRepository.save(category);
    }

    public List<Category> findAll(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return categoryRepository.findAll(pageable).getContent();
    }

    public Category findById(int catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория по id="+catId+" не найдена."));
    }
}
