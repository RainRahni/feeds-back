package org.feeds.service.interfaces;

import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;

public interface ValidationService {
    void validateRequestingFeed(FeedCreationDTO feedCreationDTO);
    void validateUpdatingFeed(FeedUpdateDTO feedUpdateDTO, Long feedId);
    void validateDeletingFeed(Long feedId);
    void validateReadingArticleContent(String link);
}
