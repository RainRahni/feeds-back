package org.feeds.controller;

import lombok.RequiredArgsConstructor;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.service.FeedServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedServiceImpl feedService;
    @PostMapping
    public void requestFeed(@RequestBody FeedCreationDTO feedCreationDTO) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        feedService.requestFeed(feedCreationDTO);
    }
}
