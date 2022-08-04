package com.sparta.springcoredb.repository;

import com.sparta.springcoredb.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByUserId(String username);

}