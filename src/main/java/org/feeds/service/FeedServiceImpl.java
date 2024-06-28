package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.handler.FeedHandler;
import org.feeds.model.Feed;
import org.feeds.repository.FeedRepository;
import org.feeds.service.interfaces.FeedService;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final ArticleServiceImpl articleService;
    private final CategoryServiceImpl categoryService;
    @Override
    public void requestFeed(FeedCreationDTO feedCreationDTO) throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
        String feedUrl = feedCreationDTO.url();
        URL url = new URI(feedUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        FeedHandler feedHandler = new FeedHandler(this, articleService, categoryService);

        saxParser.parse(new InputSource(url.openStream()), feedHandler);
    }
    public void createFeed(Feed feed) {
        feedRepository.save(feed);
    }
}
