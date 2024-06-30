package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedRequestDTO;
import org.feeds.handler.FeedHandler;
import org.feeds.mapper.FeedMapper;
import org.feeds.model.Feed;
import org.feeds.repository.FeedRepository;
import org.feeds.service.interfaces.FeedService;
import org.feeds.utils.ColorUtils;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final ArticleServiceImpl articleService;
    private final CategoryServiceImpl categoryService;
    private final FeedMapper feedMapper;
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
    @Override
    public void createFeed(Feed feed) {
        Set<String> usedColors = feedRepository.findAll().stream().map(Feed::getHexColor).collect(Collectors.toSet());
        String color = ColorUtils.addColor(usedColors);
        feed.setHexColor(color);
        feedRepository.save(feed);
    }
    @Override
    public List<FeedRequestDTO> readAllFeeds() {
        List<Feed> feeds = feedRepository.findAll();
        return feedMapper.toFeedRequestDTOList(feeds);
    }
}
