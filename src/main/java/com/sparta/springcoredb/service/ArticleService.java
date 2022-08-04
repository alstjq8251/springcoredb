package com.sparta.springcoredb.service;

import com.sparta.springcoredb.dto.ArticleDto;
import com.sparta.springcoredb.dto.UserInfoDto;
import com.sparta.springcoredb.model.Article;
import com.sparta.springcoredb.model.User;
import com.sparta.springcoredb.repository.ArticleRepository;
import com.sparta.springcoredb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final UserInfoDto userInfoDto;

    @Autowired
    public ArticleService(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            UserInfoDto userInfoDto
    ){
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.userInfoDto = userInfoDto;
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    @Transactional
    public Article createArticle(ArticleDto articleDto, UserInfoDto userInfoDto) {
        User username = userRepository.findByUsername(userInfoDto.getUsername())
                .orElseThrow(()-> new IllegalArgumentException("잘못된 사용자입니다. 다시 로그인 후 시도해주세요."));
        Article article = new Article(articleDto,username);
        username.addArticle(article);
        articleRepository.save(article);
        return article;
    }

    @Transactional
    public String updateArticle(Long id, ArticleDto articleDto) {
        Article articleId = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));
        articleId.update(articleDto);
        return "수정 끝";
    }

    @Transactional
    public String deleteArticle(Long id, ArticleDto articleDto) {
        Article blogById = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));

        User byUsername = userRepository.findByUsername(userInfoDto.getUsername())
                .orElseThrow(()-> new IllegalArgumentException("잘못된 사용자입니다. 다시 로그인 후 시도해주세요."));

        byUsername.removeArticle(articleDto);
        articleRepository.deleteById(id);
        return "삭제 완료 : " + blogById.getId();
    }
}
