package org.feeds.controller;

import lombok.RequiredArgsConstructor;
import org.feeds.service.CategoryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;
    @GetMapping
    public List<String> readCategoryNames() {
        return categoryService.readCategoryNames();
    }
}
