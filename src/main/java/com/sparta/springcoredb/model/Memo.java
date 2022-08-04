package com.sparta.springcoredb.model;

import com.sparta.springcoredb.dto.UserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.sparta.springcoredb.dto.MemoDto;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Memo extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "Article_ID", nullable = false)
    private Article article;

    @Column(nullable = false)
    private String check_id;

    public Memo(MemoDto memodto, Article article, UserInfoDto userInfoDto){
        this.title = memodto.getTitle();
        this.content = memodto.getContent();
        this.article = article;
        this.check_id = userInfoDto.getUsername();
    }
    public void update(MemoDto memoDto){
        this.title = memoDto.getTitle();
        this.content = memoDto.getContent();
    }
}
