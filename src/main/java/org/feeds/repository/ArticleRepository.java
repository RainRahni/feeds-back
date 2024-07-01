package org.feeds.repository;

import org.feeds.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
    void deleteByFeedId(Long feedId);
}
