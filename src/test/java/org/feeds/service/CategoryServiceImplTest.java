package org.feeds.service;

import org.feeds.model.Category;
import org.feeds.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private ValidationServiceImpl validationService;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void Should_CreateCategory_When_CorrectInput() {
        Category category = Category.builder()
                .name("Technology")
                .link("RandomLink")
                .build();

        categoryService.createCategory(category);

        verify(validationService, times(1)).validateCreatingCategory(category);
        verify(categoryRepository, times(1)).findAll();
        verify(categoryRepository, times(1)).save(category);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category savedCategory = categoryCaptor.getValue();
        assertEquals("Technology", savedCategory.getName());
        assertEquals("RandomLink", savedCategory.getLink());
    }
}