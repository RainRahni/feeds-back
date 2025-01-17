package org.feeds.controller;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedRequestDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.service.FeedServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedServiceImpl feedService;

    @PostMapping
    public void requestFeed(@RequestBody FeedCreationDTO feedCreationDTO) {
        feedService.requestFeed(feedCreationDTO);
    }

    @GetMapping
    public List<FeedRequestDTO> readAllFeeds() {
        return feedService.readAllFeeds();
    }

    @PutMapping("/{feedId}")
    public void updateFeed(@RequestBody FeedUpdateDTO feedUpdateDTO,
                           @PathVariable  Long feedId) {
        feedService.updateFeed(feedUpdateDTO, feedId);
    }

    @DeleteMapping("/{feedId}")
    public void deleteFeed(@PathVariable  Long feedId) {
        feedService.deleteFeed(feedId);
    }
}
