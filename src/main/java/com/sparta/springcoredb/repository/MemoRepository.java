package com.sparta.springcoredb.repository;

import com.sparta.springcoredb.model.Article;
import com.sparta.springcoredb.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository  extends JpaRepository<Memo, Long> {
    List<Article> findAllByArticleId(Long ArticleId);
    Memo findByArticle_IdAndId(Long articleId, Long id);


}
