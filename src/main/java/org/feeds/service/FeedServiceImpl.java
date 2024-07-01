package org.feeds.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedRequestDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.handler.FeedHandler;
import org.feeds.mapper.FeedMapper;
import org.feeds.model.Feed;
import org.feeds.repository.FeedRepository;
import org.feeds.service.interfaces.FeedService;
import org.feeds.utils.ColorUtils;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final ValidationServiceImpl validationService;
    private final FeedMapper feedMapper;
    @Override
    public void requestFeed(FeedCreationDTO feedCreationDTO) {
        validationService.validateRequestingFeed(feedCreationDTO);
        String feedUrl = feedCreationDTO.link();
        readXML(feedUrl);
    }
    private void readXML(String feedUrl)  {
        try {
            URL url = new URI(feedUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            FeedHandler feedHandler = new FeedHandler(this, articleService, categoryService);

            saxParser.parse(new InputSource(url.openStream()), feedHandler);
        } catch (URISyntaxException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    @Transactional
    @Scheduled(cron = "${feed.cron.expression}")
    public void updateFeedArticles() {
        List<Feed> feeds = feedRepository.findAll();
        for (Feed feed : feeds) {
            articleService.deleteArticles(feed.getId());
            readXML(feed.getLink());
        }
    }
    @Override
    public void createFeed(Feed feed) {
        Set<String> usedColors = feedRepository.findAll()
                .stream()
                .map(Feed::getHexColor)
                .collect(Collectors.toSet());
        String color = ColorUtils.addColor(usedColors);
        feed.setHexColor(color);
        feedRepository.save(feed);
    }
    @Override
    public List<FeedRequestDTO> readAllFeeds() {
        List<Feed> feeds = feedRepository.findAll();
        return feedMapper.toFeedRequestDTOList(feeds);
    }
    @Override
    public void updateFeed(FeedUpdateDTO feedUpdateDTO, Long feedId) {
        validationService.validateUpdatingFeed(feedUpdateDTO, feedId);
        Feed existingFeed = feedRepository.findById(feedId).get();
        if (!existingFeed.getLink().equals(feedUpdateDTO.link())) {
            requestFeed(new FeedCreationDTO(feedUpdateDTO.link()));
            deleteFeed(feedId);
            return;
        }
        feedMapper.updateModel(feedUpdateDTO, existingFeed);
        feedRepository.save(existingFeed);
    }
    @Override
    public void deleteFeed(Long feedId) {
        validationService.validateDeletingFeed(feedId);
        articleService.deleteArticles(feedId);
        feedRepository.deleteById(feedId);
    }
    @Override
    public Feed readFeed(String link) {
        return feedRepository.findByLink(link).orElse(null);
    }
}
