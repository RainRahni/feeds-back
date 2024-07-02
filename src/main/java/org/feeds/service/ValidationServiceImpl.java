package org.feeds.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.model.Article;
import org.feeds.model.Category;
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
    @Value("${fields.missing.message}")
    private String fieldMissingMessage;

    @Override
    public void validateRequestingFeed(FeedCreationDTO feedCreationDTO) {
        log.info("Validate Requesting Feed Link: {}", feedCreationDTO.link());
        boolean isEmptyLink = feedCreationDTO.link().isBlank();
        boolean isDuplicateLink = feedRepository.existsByLink(feedCreationDTO.link());
        boolean isNotRssFeedLink = !feedCreationDTO.link().endsWith(".rss");
        if (isEmptyLink || isDuplicateLink || isNotRssFeedLink) {
            throw new ValidationException(invalidInputMessage);
        }
    }

    @Override
    public void validateCreatingArticle(Article article) {
        boolean isGuidNotPresent = article.getGuid() == null || article.getGuid().isBlank();
        boolean isFeedNotPresent = article.getFeed() == null;
        if (isGuidNotPresent || isFeedNotPresent) {
            throw new ValidationException(fieldMissingMessage);
        }
    }

    @Override
    public void validateCreatingFeed(Feed feed) {
        boolean isTitleNotPresent = feed.getTitle() == null || feed.getTitle().isBlank();
        boolean isLinkNotPresent = feed.getLink() == null || feed.getLink().isBlank();
        if (isTitleNotPresent || isLinkNotPresent) {
            throw new ValidationException(fieldMissingMessage);
        }
    }

    @Override
    public void validateCreatingCategory(Category category) {
        boolean isNameNotPresent = category.getName() == null || category.getName().isBlank();
        boolean isLinkNotPresent = category.getLink() == null || category.getLink().isBlank();
       if (isNameNotPresent || isLinkNotPresent) {
            throw new ValidationException(fieldMissingMessage);
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
    public void validateFeedExists(Long feedId) {
        log.info("Validate Existing Feed With id: {}", feedId);
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
