package com.sparta.springcoredb.service;


import com.sparta.springcoredb.dto.MemoDto;
import com.sparta.springcoredb.dto.UserInfoDto;
import com.sparta.springcoredb.model.Article;
import com.sparta.springcoredb.model.Memo;
import com.sparta.springcoredb.model.User;
import com.sparta.springcoredb.repository.ArticleRepository;
import com.sparta.springcoredb.repository.MemoRepository;
import com.sparta.springcoredb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final UserInfoDto userInfoDto;

    @Autowired
    public MemoService(
            MemoRepository memoRepository,
            ArticleRepository articleRepository,
            UserRepository userRepository,
            UserInfoDto userInfoDto
    ){
        this.memoRepository = memoRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.userInfoDto = userInfoDto;
    }

    public List<Memo> getMemo() {
        return memoRepository.findAll();
    }

    @Transactional
    public Memo createMemo(Long articleId, MemoDto memoDto) {
        Article articleid = articleRepository.findById(articleId)
                .orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        Memo memo = new Memo(memoDto, articleid, userInfoDto);
        articleid.addMemo(memo);
        return memo;
    }

    @Transactional
    public String update(Long articleId, Long memoId, MemoDto memoDto) {
        Memo memo = memoRepository.findByArticle_IdAndId(articleId, memoId);
        memo.update(memoDto);
        return "수정 끝";
    }

    @Transactional
    public String delete(Long ArticleId,Long memoId, MemoDto memoDto) {
        Article articleid = articleRepository.findById(ArticleId)
                .orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다."));

        articleid.removeMemo(memo);

        memoRepository.deleteById(memoId);

        return " 삭제 " + memo.getId();
    }
}
