package org.feeds.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles")
public class Article {
    @Id
    private String guid;
    private String title;
    private Date publishedDate;
    private String description;
    private String author;
    @ManyToMany
    @JoinTable(
            name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

}
