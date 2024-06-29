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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    @ManyToMany(mappedBy = "categories")
    private Set<Article> articles = new HashSet<>();
    public void addArticle(Article article) {
        articles.add(article);
    }
}
