package org.feeds.service;

import lombok.RequiredArgsConstructor;
import org.feeds.config.Constants;
import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.model.Feed;
import org.feeds.repository.ArticleRepository;
import org.feeds.repository.FeedRepository;
import org.feeds.service.interfaces.ValidationService;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final FeedRepository feedRepository;
    private final ArticleRepository articleRepository;
    @Override
    public void validateRequestingFeed(FeedCreationDTO feedCreationDTO) {
        boolean isEmptyLink = feedCreationDTO.link().isBlank();
        boolean isDuplicateLink = feedRepository.existsByLink(feedCreationDTO.link());
        if (isEmptyLink || isDuplicateLink) {
            throw new ValidationException(Constants.INVALID_INPUT_MESSAGE);
        }
    }
    @Override
    public void validateUpdatingFeed(FeedUpdateDTO feedUpdateDTO, Long feedId) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        boolean isFeedNull = optionalFeed.isEmpty();
        boolean isFeedEqual = false;
        if (optionalFeed.isPresent()) {
            Feed feed = optionalFeed.get();
            isFeedEqual = feed.getLink().equals(feedUpdateDTO.link()) &&
            feed.getTitle().equals(feedUpdateDTO.title());
        }
        if (isFeedNull || isFeedEqual) {
            throw new ValidationException(Constants.INVALID_INPUT_MESSAGE);
        }
    }
    @Override
    public void validateDeletingFeed(Long feedId) {
        boolean isFeedNull = !feedRepository.existsById(feedId);
        if (isFeedNull) {
            throw new ValidationException(Constants.INVALID_INPUT_MESSAGE);
        }
    }
    @Override
    public void validateReadingArticleContent(String link) {
        boolean isArticleNull = !articleRepository.existsByLink(link);
        if (isArticleNull) {
            throw new ValidationException(Constants.INVALID_INPUT_MESSAGE);
        }
    }
}
