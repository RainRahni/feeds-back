package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.model.Category;
import org.feeds.repository.CategoryRepository;
import org.feeds.service.interfaces.CategoryService;
import org.feeds.utils.ColorUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public void createCategory(Category category) {
        Set<String> usedColors = categoryRepository.findAll().stream().map(Category::getHexColor)
                .collect(Collectors.toSet());
        String color = ColorUtils.addColor(usedColors);
        category.setHexColor(color);
        categoryRepository.save(category);
    }
    @Override
    public Category readCategory(String name) {
        return categoryRepository.findByName(name);
    }
    @Override
    public List<String> readCategoryNames() {
        return categoryRepository.findAll().stream().map(Category::getName).toList();
    }
}
