package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.ArticleRequestDTO;
import org.feeds.mapper.ArticleMapper;
import org.feeds.model.Article;
import org.feeds.repository.ArticleRepository;
import org.feeds.service.interfaces.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    @Override
    public void createArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public List<ArticleRequestDTO> readAllArticles() {
        List<Article> articles = articleRepository.findAll();
        articles = articles.stream()
                .sorted((a1, a2) -> a2.getPublishedDate().compareTo(a1.getPublishedDate()))
                .toList();
        return articleMapper.toArticleRequestDTOList(articles);
    }
}
