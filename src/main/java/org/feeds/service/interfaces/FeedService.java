package org.feeds.service.interfaces;

import org.feeds.dto.FeedCreationDTO;
import org.feeds.model.Feed;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FeedService {
    void requestFeed(FeedCreationDTO feedCreationDTO)
            throws URISyntaxException, IOException, ParserConfigurationException, SAXException;
    void createFeed(Feed feed);
}
