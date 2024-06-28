package org.feeds.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "feeds")
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String link;
    private String description;
    @OneToMany(mappedBy = "feed")
    private Set<Article> articles = new HashSet<>();
    public void addArticle(Article article) {
        articles.add(article);
    }
}
