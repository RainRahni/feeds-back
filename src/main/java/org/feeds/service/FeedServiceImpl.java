package org.feeds.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final ArticleServiceImpl articleService;
    private final CategoryServiceImpl categoryService;
    private final ValidationServiceImpl validationService;
    private final FeedMapper feedMapper;

    @Override
    @Transactional
    public void requestFeed(FeedCreationDTO feedCreationDTO) {
        log.info("Request feed");
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
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    @Scheduled(cron = "${feed.cron.expression}")
    public void updateFeedArticles() {
        log.info("Update feed using cron");
        List<Feed> feeds = feedRepository.findAll();
        for (Feed feed : feeds) {
            articleService.deleteArticles(feed.getId());
            readXML(feed.getLink());
        }
    }

    @Override
    @Transactional
    public void createFeed(Feed feed) {
        validationService.validateCreatingFeed(feed);
        Set<String> usedColors = feedRepository.findAll()
                .stream()
                .map(Feed::getHexColor)
                .collect(Collectors.toSet());
        String color = ColorUtils.addColor(usedColors);
        feed.setHexColor(color);
        feedRepository.save(feed);
        log.info("Feed created: {}", feed);
    }

    @Override
    public List<FeedRequestDTO> readAllFeeds() {
        log.info("Read all feeds");
        List<Feed> feeds = feedRepository.findAll();
        return feedMapper.toFeedRequestDTOList(feeds);
    }

    @Override
    @Transactional
    public void updateFeed(FeedUpdateDTO feedUpdateDTO, Long feedId) {
        validationService.validateUpdatingFeed(feedUpdateDTO, feedId);
        log.info("Update feed");
        Feed existingFeed = feedRepository.findById(feedId).get();
        boolean linkChanged = !existingFeed.getLink().equals(feedUpdateDTO.link());
        if (linkChanged) {
            requestFeed(new FeedCreationDTO(feedUpdateDTO.link()));
            deleteFeed(feedId);
        } else {
            feedMapper.updateModel(feedUpdateDTO, existingFeed);
            feedRepository.save(existingFeed);
        }
    }

    @Override
    @Transactional
    public void deleteFeed(Long feedId) {
        log.info("Delete feed");
        validationService.validateFeedExists(feedId);
        articleService.deleteArticles(feedId);
        feedRepository.deleteById(feedId);
    }

    @Override
    public Feed readFeed(String link) {
        log.info("Read single feed with link");
        return feedRepository.findByLink(link).orElse(null);
    }
}
