package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.model.Category;
import org.feeds.repository.CategoryRepository;
import org.feeds.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public void createCategory(Category category) {
        category.setHexColor(addColor());
        categoryRepository.save(category);
    }
    private String addColor() {
        List<String> colors = categoryRepository.findAll().stream().map(Category::getHexColor).toList();
        Random random = new Random();
        String color = "";
        while (colors.contains(color)) {
            color = String.format("#%06x", random.nextInt(0xFFFFFF + 1));
        }
        return color;
    }
}
