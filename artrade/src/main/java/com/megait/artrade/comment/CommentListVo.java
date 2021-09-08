package com.megait.artrade.comment;

import com.megait.artrade.work.Work;
import lombok.Data;

@Data
public class CommentListVo {

    private int num;
    private String contents;
    private Work work;
    private String creatAt;
}
