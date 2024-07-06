package org.feeds.service;

import org.feeds.model.Article;
import org.feeds.model.Feed;
import org.feeds.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        String expectedGuid = "flipboard-CFS2ZUrnTSCUx63phD2j9A:a:1376366263-1716494118";
        Feed expectedFeed =  new Feed();
        Article article = Article.builder()
                .guid(expectedGuid)
                .feed(expectedFeed)
                .build();

        articleService.createArticles(List.of(article));

        verify(validationService, times(1)).validateCreatingArticle(article);
        verify(articleRepository, times(1)).saveAll(List.of(article));

        ArgumentCaptor<List<Article>> articleListCaptor = ArgumentCaptor.forClass(List.class);
        verify(articleRepository).saveAll(articleListCaptor.capture());
        List<Article> savedArticles = articleListCaptor.getValue();

        Article savedArticle = savedArticles.get(0);
        String actualGuid = savedArticle.getGuid();
        Feed actualFeed = savedArticle.getFeed();
        assertEquals(expectedGuid, actualGuid);
        assertEquals(expectedFeed, actualFeed);
    }
}
