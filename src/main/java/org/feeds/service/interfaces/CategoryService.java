package org.feeds.service.interfaces;

import org.feeds.model.Category;

import java.util.List;

public interface CategoryService {
    void createCategory(Category category);
    Category readCategory(String name);
    List<String> readCategoryNames();
}
