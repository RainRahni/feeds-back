package org.feeds.controller;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.service.FeedServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedServiceImpl feedService;
    @PostMapping
    public void CreateFeed(@RequestBody FeedCreationDTO feedCreationDTO) {
        feedService.CreateFeed(feedCreationDTO);
    }
}
