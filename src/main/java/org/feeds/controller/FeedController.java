package org.feeds.controller;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedRequestDTO;
import org.feeds.service.FeedServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedServiceImpl feedService;
    @PostMapping
    public void requestFeed(@RequestBody FeedCreationDTO feedCreationDTO) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        feedService.requestFeed(feedCreationDTO);
    }
    @GetMapping
    public List<FeedRequestDTO> readAllFeeds() {
        return feedService.readAllFeeds();
    }
}
