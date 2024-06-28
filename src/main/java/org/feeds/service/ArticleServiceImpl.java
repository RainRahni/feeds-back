package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.model.Article;
import org.feeds.repository.ArticleRepository;
import org.feeds.service.interfaces.ArticleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    public void createArticle(Article article) {
        articleRepository.save(article);
    }
}
