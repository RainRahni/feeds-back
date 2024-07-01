package org.feeds.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.feeds.dto.ArticleRequestDTO;
import org.feeds.service.ArticleServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleServiceImpl articleService;

    @GetMapping("all")
    public List<ArticleRequestDTO> readAllArticles() {
        return articleService.readAllArticles();
    }

    @GetMapping("content")
    public String readArticleContent(@RequestParam String link) {
        return articleService.readArticleContent(link);
    }
}
