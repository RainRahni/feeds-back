package org.feeds.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "articles")
public class Article {
    @Id
    private String guid;
    private String title;
    private String link;
    private Date publishedDate;
    private String description;
    private String author;
    @ManyToMany
    @JoinTable(
            name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;
    public void addCategory(Category category) {
        categories.add(category);
    }
}
