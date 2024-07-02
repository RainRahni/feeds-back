package org.feeds.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.feeds.dto.ArticleRequestDTO;
import org.feeds.mapper.ArticleMapper;
import org.feeds.model.Article;
import org.feeds.repository.ArticleRepository;
import org.feeds.service.interfaces.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ValidationServiceImpl validationService;
    private final ArticleMapper articleMapper;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void createArticle(Article article) {
        validationService.validateCreatingArticle(article);
        articleRepository.save(article);
        log.info("Article created: {}", article);
    }

    @Override
    public List<ArticleRequestDTO> readAllArticles() {
        List<Article> articles = articleRepository.findAll();
        articles = articles.stream()
                .sorted((a1, a2) -> a2.getPublishedDate().compareTo(a1.getPublishedDate()))
                .toList();
        return articleMapper.toArticleRequestDTOList(articles);
    }

    @Override
    public String readArticleContent(String link) {
        validationService.validateReadingArticleContent(link);
        String decodedLink = URLDecoder.decode(link, StandardCharsets.UTF_8);
        String clutterFree = restTemplate.getForObject(decodedLink, String.class);
        log.info("Get clutter free article");
        return clutterFree;
    }

    @Override
    @Transactional
    public void deleteArticles(Long feedId) {
        validationService.validateFeedExists(feedId);
        articleRepository.deleteByFeedId(feedId);
    }
}
