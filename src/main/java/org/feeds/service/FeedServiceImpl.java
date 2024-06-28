package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.repository.FeedRepository;
import org.feeds.service.interfaces.FeedService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    @Override
    public void CreateFeed(FeedCreationDTO feedCreationDTO) {

    }
}
