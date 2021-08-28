package com.megait.artrade.controller;

import com.google.gson.JsonObject;
import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.comment.Comment;
import com.megait.artrade.comment.CommentService;
//import com.megait.artrade.comment.CommentType;
//import com.megait.artrade.comment.CommentVo;
import com.megait.artrade.comment.CommentVo;
import com.megait.artrade.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    // service
    private final CommentService commentService;





    @ResponseBody
    @PostMapping("/auction/comment")
    public String addComment(@AuthenticationMember Member member, @RequestBody CommentVo commentVo) {

        // 댓글 데이터 출력
        System.out.println(commentVo.getComment());   // 댓글 받음

        // 댓글 내용을 엔티티에 저장
        Comment comment0 = commentService.saveComment(commentVo.getComment(),member);

        // 댓글에 대한 정보 가져옴
        Comment comment = commentService.getComment(comment0.getId());

        // 회원 닉네임, 댓글 꺼내오기
        String comment1 = comment.getContents();                  // 댓글 내용 가져옴
        String nickName = comment.getMember().getNickname();      // 회원 닉네임 가져옴
        LocalDateTime localDateTime = LocalDateTime.now();         // 현재 시각
        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //json 형식
        JsonObject object = new JsonObject();
        //로그인 되어 있지 않으면 "로그인을 해야합니다"라는 창을 띄워줌
        if (member == null) {
            object.addProperty("result", false);
            object.addProperty("message", "로그인을 해야합니다.");
            return object.toString();
        }

        //로그인 되어 있으면
        object.addProperty("result", true);
        object.addProperty("id", comment.getId());
        object.addProperty("comment", comment1);
        object.addProperty("nickName", nickName);
        object.addProperty("time", time);


        return object.toString();
    }
}
