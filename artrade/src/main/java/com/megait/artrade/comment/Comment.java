package com.megait.artrade.comment;


import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Work work;

    @ManyToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    private CommentType commentClass;     // 댓글 상태

    private int oder;

    private String contents;

    private LocalDateTime createAt;

//    private LocalDateTime modifiedAt;


    private int comment_cnt;



}
