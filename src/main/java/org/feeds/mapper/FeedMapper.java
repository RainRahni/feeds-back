package org.feeds.mapper;

import org.feeds.dto.FeedRequestDTO;
import org.feeds.model.Feed;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMapper {
    FeedRequestDTO toFeedRequestDTO(Feed feed);
    List<FeedRequestDTO> toFeedRequestDTOList(List<Feed> feeds);
}
