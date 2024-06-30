package org.feeds.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final RestTemplate restTemplate;
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
    @Override
    public String readArticleContent(String link) {
        String decodedLink = URLDecoder.decode(link, StandardCharsets.UTF_8);
        String clutterFree = restTemplate.getForObject(decodedLink, String.class);
        return clutterFree;
    }
}
