package org.feeds.service.interfaces;

import org.feeds.dto.ArticleRequestDTO;
import org.feeds.model.Article;

import java.util.List;

public interface ArticleService {
    void createArticle(Article article);
    List<ArticleRequestDTO> readAllArticles();
}
