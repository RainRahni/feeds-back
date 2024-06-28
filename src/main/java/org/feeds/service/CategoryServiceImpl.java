package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.model.Category;
import org.feeds.repository.CategoryRepository;
import org.feeds.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }
}
