package com.sparta.springcoredb.controller;


import com.sparta.springcoredb.dto.MemoDto;
import com.sparta.springcoredb.model.Article;
import com.sparta.springcoredb.model.Memo;
import com.sparta.springcoredb.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MemoController {
    private final MemoService memoService;

    @Autowired
    public MemoController(MemoService memoService){
        this.memoService= memoService;
    }

    @GetMapping("api/memos")
    public List<Memo> getMemo() {
        return memoService.getMemo();
    }

    @PostMapping("api/memos/{articleId}")
    public Memo createMemo(@PathVariable Long articleId,@RequestBody MemoDto memoDto){
        return memoService.createMemo(articleId,memoDto);
    }

    @PutMapping("api/memos/{articleId}{memoId}")
    public String updateMemo(@PathVariable Long articleId,@PathVariable Long memoId, @RequestBody MemoDto memoDto){
        return memoService.update(articleId,memoId, memoDto);
    }

    @DeleteMapping("api/memos/{articleId}{memoId}")
    public String deleteMemo(@PathVariable Long articleId,@PathVariable Long memoId ,@RequestBody MemoDto memoDto){
        return memoService.delete(articleId, memoId,memoDto);
    }

}
