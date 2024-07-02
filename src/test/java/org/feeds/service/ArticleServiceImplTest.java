package org.feeds.service;

import org.feeds.model.Article;
import org.feeds.model.Category;
import org.feeds.model.Feed;
import org.feeds.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ValidationServiceImpl validationService;
    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void Should_CreateArticle_When_CorrectInput() {
        Feed feed = new Feed();
        Article article = Article.builder()
                .guid("RandomGuid")
                .feed(feed)
                .build();

        articleService.createArticle(article);

        verify(validationService, times(1)).validateCreatingArticle(article);
        verify(articleRepository, times(1)).save(article);

        ArgumentCaptor<Article> articleCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).save(articleCaptor.capture());
        Article savedArticle = articleCaptor.getValue();
        assertEquals("RandomGuid", savedArticle.getGuid());
        assertEquals(feed, savedArticle.getFeed());
    }
}