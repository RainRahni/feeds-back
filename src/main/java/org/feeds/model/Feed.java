package org.feeds.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "feeds")
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String link;
    private String description;
    @OneToMany(mappedBy = "feed")
    private Set<Article> articles;
    public void addArticle(Article article) {
        articles.add(article);
    }
}
