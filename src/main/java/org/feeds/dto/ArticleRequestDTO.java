package org.feeds.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record ArticleRequestDTO(String title, String link, String publishedDate,
                                    String description, String author, List<CategoryRequestDTO> categories,
                                    String imageUrl, Long feedId) {}

