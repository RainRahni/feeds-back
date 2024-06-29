package org.feeds.dto;

import java.util.List;

public record ArticleRequestDTO(String title, String link, String publishedDate,
                                    String description, String author, List<CategoryRequestDTO> categories,
                                    String imageUrl, Long feedId) {}

