package com.megait.artrade.like;

import lombok.Data;

@Data
public class LikeVo {

    private String title;
    private String filePath;
    private int popularity;
    private int comment_cnt;
    private String autionType;
    private String date;
}
