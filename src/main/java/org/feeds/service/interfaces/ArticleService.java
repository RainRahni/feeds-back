package org.feeds.service.interfaces;

import org.feeds.dto.ArticleRequestDTO;
import org.feeds.model.Article;

import java.util.List;

public interface ArticleService {
    void createArticles(List<Article> articles);
    List<ArticleRequestDTO> readAllArticles();
    String readArticleContent(String link);
    void deleteArticles(Long feedId);
}
