package org.feeds.service;

import org.feeds.handler.FeedHandler;
import org.feeds.model.Article;
import org.feeds.model.Feed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestFeedTest {
    @Mock
    private FeedServiceImpl feedService;

    @Mock
    private ArticleServiceImpl articleService;

    @Mock
    private CategoryServiceImpl categoryService;

    private FeedHandler feedHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        feedHandler = new FeedHandler(feedService, articleService, categoryService);
    }

    @Test
    void testFeedHandler() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        Feed mockFeed = new Feed();
        when(feedService.readFeed(anyString())).thenReturn(null).thenReturn(mockFeed);

        File file = new File("src/test/resources/feed.xml");
        FileInputStream fis = new FileInputStream(file);
        saxParser.parse(new InputSource(fis), feedHandler);

        verify(feedService, times(1)).createFeed(any(Feed.class));
        verify(articleService, atLeastOnce()).createArticle(any(Article.class));
    }

}
