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
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String hexColor;
    @ManyToMany(mappedBy = "categories")
    private Set<Article> articles = new HashSet<>();
    public void addArticle(Article article) {
        articles.add(article);
    }
}
