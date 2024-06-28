package org.feeds.handler;

import lombok.RequiredArgsConstructor;
import org.feeds.repository.FeedRepository;
import org.springframework.stereotype.Component;
import org.xml.sax.helpers.DefaultHandler;

@Component
@RequiredArgsConstructor
public class FeedHandler extends DefaultHandler {
    private final FeedRepository feedRepository;
}
