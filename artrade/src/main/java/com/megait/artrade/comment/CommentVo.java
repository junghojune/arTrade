package com.megait.artrade.comment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    private String workNo;   // 작품 제목
    private String comment;
    private String count;
}
