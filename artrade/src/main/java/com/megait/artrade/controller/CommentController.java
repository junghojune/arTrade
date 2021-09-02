package com.megait.artrade.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.comment.Comment;
import com.megait.artrade.comment.CommentService;
import com.megait.artrade.comment.CommentVo;
import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    // service
    private final CommentService commentService;
    private final WorkService workService;



    @ResponseBody
    @GetMapping("/auction/list/{id}")
    public String getCommentList(@PathVariable Long id){
        List<Comment> commentList = commentService.getList(workService.getWork(id));

        JsonArray array = new JsonArray();
        for(Comment comment : commentList){

            JsonObject object = new JsonObject();

            String comment1 = comment.getContents();                  // 댓글 내용 가져옴
            String nickName = comment.getMember().getNickname();      // 회원 닉네임 가져옴
            LocalDateTime localDateTime = comment.getCreateAt();         // 현재 시각
            String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String sNewCnt = Integer.toString(comment.getComment_cnt());
            object.addProperty("comment", comment1);
            object.addProperty("nickname", nickName);
            object.addProperty("time", time );
            object.addProperty("sNewCnt", sNewCnt);
            array.add(object);
        }
        return array.toString();

    }

    @ResponseBody
    @PostMapping("/comment/add")
    public String addComment(@AuthenticationMember Member member, @RequestBody CommentVo commentVo) {

        // 댓글 개수
        String sCnt = commentVo.getCount();
        String workNo1 = commentVo.getWorkNo();

        int cnt = Integer.parseInt(sCnt);
        Long workNo = Long.parseLong(workNo1);
        // 댓글 개수, 내용 데이터 출력
        int newCnt = cnt + 1;


        // 댓글 내용을 엔티티에 저장
        Work work = workService.getWork(workNo);          // 작품 조회(Id)
        commentService.saveComment(commentVo.getComment(),member, work, newCnt);      // 댓글 정보 DB에 저장

        // 댓글에 대한 정보 가져옴
        Comment comment = commentService.getComment(member, commentVo.getComment(), work);    // 댓글 조회

        // 회원 닉네임, 댓글 꺼내오기
        String comment1 = comment.getContents();                  // 댓글 내용 가져옴
        String nickName = comment.getMember().getNickname();      // 회원 닉네임 가져옴
        LocalDateTime localDateTime = comment.getCreateAt();         // 현재 시각
        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //json 형식
        JsonObject object = new JsonObject();
        //로그인 되어 있지 않으면 "로그인을 해야합니다"라는 창을 띄워줌
        if (member == null) {
            object.addProperty("message", "로그인을 해야합니다.");
            return object.toString();
        }
        // 최근 댓글 개수
        String sNewCnt = Integer.toString(newCnt);
        //로그인 되어 있으면
        object.addProperty("workNo", work.getId());
        object.addProperty("comment",  comment1);
        object.addProperty("nickName", nickName);
        object.addProperty("time", time);
        object.addProperty("sNewCnt", sNewCnt);



        return object.toString();
    }

}
