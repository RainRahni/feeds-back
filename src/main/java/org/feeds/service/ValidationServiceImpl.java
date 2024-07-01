package org.feeds.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.model.Feed;
import org.feeds.repository.ArticleRepository;
import org.feeds.repository.FeedRepository;
import org.feeds.service.interfaces.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImpl implements ValidationService {
    private final FeedRepository feedRepository;
    private final ArticleRepository articleRepository;
    @Value("${invalid.input.message}")
    private String invalidInputMessage;

    @Override
    public void validateRequestingFeed(FeedCreationDTO feedCreationDTO) {
        log.info("Validate Requesting Feed Link: {}", feedCreationDTO.link());
        boolean isEmptyLink = feedCreationDTO.link().isBlank();
        boolean isDuplicateLink = feedRepository.existsByLink(feedCreationDTO.link());
        if (isEmptyLink || isDuplicateLink) {
            throw new ValidationException(invalidInputMessage);
        }
    }

    @Override
    public void validateUpdatingFeed(FeedUpdateDTO feedUpdateDTO, Long feedId) {
        log.info("Validate feed update: {}, for feed with id: {}", feedUpdateDTO, feedId);
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        boolean isFeedNull = optionalFeed.isEmpty();
        boolean isFeedEqual = false;
        if (optionalFeed.isPresent()) {
            Feed feed = optionalFeed.get();
            isFeedEqual = feed.getLink().equals(feedUpdateDTO.link()) &&
            feed.getTitle().equals(feedUpdateDTO.title());
        }
        if (isFeedNull || isFeedEqual) {
            throw new ValidationException(invalidInputMessage);
        }
    }

    @Override
    public void validateDeletingFeed(Long feedId) {
        log.info("Validate Deleting Feed With id: {}", feedId);
        boolean isFeedNull = !feedRepository.existsById(feedId);
        if (isFeedNull) {
            throw new ValidationException(invalidInputMessage);
        }
    }

    @Override
    public void validateReadingArticleContent(String link) {
        log.info("Validate Reading article with Link: {}", link);
        boolean isArticleNull = !articleRepository.existsByLink(link);
        if (isArticleNull) {
            throw new ValidationException(invalidInputMessage);
        }
    }
}
