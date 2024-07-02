package org.feeds.dto;

import lombok.Builder;

@Builder
public record FeedRequestDTO(Long id, String title, String link, String hexColor, String description) {
}
