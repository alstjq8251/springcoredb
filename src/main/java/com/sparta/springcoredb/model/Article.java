package com.sparta.springcoredb.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.springcoredb.dto.ArticleDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Article extends Timestamped {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @JoinColumn(name = "username")
    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String context;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Memo> memolist;


    public Article(ArticleDto articleDto,User user) {
        this.title = articleDto.getTitle();
        this.context = articleDto.getContext();
        this.user = user;
    }
    public void addMemo(Memo memo){
        this.memolist.add(memo);
    }
    public void removeMemo(Memo memo){
        this.memolist.remove(memo);
    }

    public void update(ArticleDto articleDto){
        this.title = articleDto.getTitle();
        this.context = articleDto.getContext();
    }

}
