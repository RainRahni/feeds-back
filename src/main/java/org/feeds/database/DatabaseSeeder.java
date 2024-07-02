package org.feeds.database;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.service.FeedServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final FeedServiceImpl feedServiceImpl;
    /**
     * Seed applications to the database.
     */
    @PostConstruct
    public void seedFeed() {
        FeedCreationDTO feedCreationDTO
                = new FeedCreationDTO("https://flipboard.com/@raimoseero/feed-nii8kd0sz.rss");
        feedServiceImpl.requestFeed(feedCreationDTO);
    }
}