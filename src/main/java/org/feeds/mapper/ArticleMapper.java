package org.feeds.mapper;

import org.feeds.dto.ArticleRequestDTO;
import org.feeds.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    @Mapping(source = "feed.id", target = "feedId")
    ArticleRequestDTO toArticleRequestDTO(Article article);
    List<ArticleRequestDTO> toArticleRequestDTOList(List<Article> articles);
}
