package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.model.Category;
import org.feeds.repository.CategoryRepository;
import org.feeds.service.interfaces.CategoryService;
import org.feeds.utils.ColorUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public void createCategory(Category category) {
        List<String> usedColors = categoryRepository.findAll().stream().map(Category::getHexColor).toList();
        String color = ColorUtils.addColor(usedColors);
        category.setHexColor(color);
        categoryRepository.save(category);
    }
}
