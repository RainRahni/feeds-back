package org.feeds.dto;

import lombok.Builder;

@Builder
public record FeedUpdateDTO(String title, String link) {
}
