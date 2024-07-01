package org.feeds.service.interfaces;

import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedRequestDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.model.Feed;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface FeedService {
    void requestFeed(FeedCreationDTO feedCreationDTO);
    void updateFeedArticles();
    void createFeed(Feed feed);
    List<FeedRequestDTO> readAllFeeds();
    void updateFeed(FeedUpdateDTO feedUpdateDTO, Long feedId);
    void deleteFeed(Long feedId);
    Feed readFeed(String link);
}
