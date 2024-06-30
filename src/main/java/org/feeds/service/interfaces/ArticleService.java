package org.feeds.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.feeds.dto.ArticleRequestDTO;
import org.feeds.model.Article;

import java.util.List;

public interface ArticleService {
    void createArticle(Article article);
    List<ArticleRequestDTO> readAllArticles();
    String readArticleContent(String link);
}
