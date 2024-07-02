package org.feeds.service.interfaces;

import org.feeds.dto.FeedCreationDTO;
import org.feeds.dto.FeedUpdateDTO;
import org.feeds.model.Article;
import org.feeds.model.Category;
import org.feeds.model.Feed;

public interface ValidationService {
    void validateRequestingFeed(FeedCreationDTO feedCreationDTO);
    void validateUpdatingFeed(FeedUpdateDTO feedUpdateDTO, Long feedId);
    void validateFeedExists(Long feedId);
    void validateReadingArticleContent(String link);
    void validateCreatingArticle(Article article);
    void validateCreatingFeed(Feed feed);
    void validateCreatingCategory(Category category);
}
