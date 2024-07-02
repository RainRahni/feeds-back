package org.feeds.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ValidationServiceImpl validationService;

    /**
     * Validate, assign color and save category to the database.
     * @param category to be saved.
     */
    @Override
    @Transactional
    public void createCategory(Category category) {
        validationService.validateCreatingCategory(category);
        Set<String> usedColors = categoryRepository.findAll().stream().map(Category::getHexColor)
                .collect(Collectors.toSet());
        String color = ColorUtils.addColor(usedColors);
        category.setHexColor(color);
        categoryRepository.save(category);
        log.info("Category created: {}", category);
    }

    /**
     * Read a category with given name from the database.
     * @param name of the category.
     * @return category with given name.
     */
    @Override
    public Category readCategory(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Read all category names in the database.
     * @return list of category names.
     */
    @Override
    public List<String> readCategoryNames() {
        return categoryRepository.findAll().stream().map(Category::getName).toList();
    }
}
