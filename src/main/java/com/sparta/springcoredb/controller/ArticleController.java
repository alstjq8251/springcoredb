package com.sparta.springcoredb.controller;


import com.sparta.springcoredb.dto.ArticleDto;
import com.sparta.springcoredb.dto.UserInfoDto;
import com.sparta.springcoredb.model.Article;
import com.sparta.springcoredb.model.User;
import com.sparta.springcoredb.security.UserDetailsImpl;
import com.sparta.springcoredb.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    @GetMapping("api/articles")
    public List<Article> getArticle() {
        return articleService.getArticles();
    }

    @PostMapping("api/articles")
    public Article createArticle(
            @RequestBody ArticleDto ArticleDto,
            @RequestBody UserInfoDto userInfoDto
            ) {
        return articleService.createArticle(ArticleDto, userInfoDto);
    }
    @PutMapping("api/articles/{id}")
    public String updateArticle(@PathVariable Long id, @RequestBody ArticleDto articleDto){
        return articleService.updateArticle(id,articleDto);
    }
    @DeleteMapping("api/articles/{id}")
    public String deleteArticle(@PathVariable Long id, @RequestBody ArticleDto articleDto){
        return articleService.deleteArticle(id, articleDto);
    }



}
