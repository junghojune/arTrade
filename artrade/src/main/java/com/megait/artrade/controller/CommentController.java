package com.megait.artrade.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.comment.Comment;
import com.megait.artrade.comment.CommentService;
import com.megait.artrade.comment.CommentVo;
import com.megait.artrade.comment.PostVo;
import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    // service
    private final CommentService commentService;
    private final WorkService workService;


    @ResponseBody
    @GetMapping("/comment/list")
    public String commentList(@RequestBody PostVo postVo) {

        //Comment 에 등록되어 있는 댓글들 불러와서 list에 담고 작품의 댓글 목록에 뿌려야함
        String workNo1 = postVo.getWorkNo();
        Long workNo = Long.parseLong(workNo1);      // 게시물 번호를 작품 번호로 봄


        Work work = workService.getWork(workNo);   // 작품번호로 작품 불러옴.
        // 리스트 불러옴

        List<Comment> commentList = commentService.getList(work);

        JsonArray array = new JsonArray();
        for (int i = 0; i < commentList.size(); i++) {
            array.add(commentList.get(i));

        }
        JsonObject object = new JsonObject();         // json 형식으로 html에 응답을 보내야 함.
        return null;

    }



    @ResponseBody
    @GetMapping(value = "/comment/replies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> commentList(@PathVariable Long id) {

         return new ResponseEntity<>(commentService.getList(workService.getWork(id)), HttpStatus.OK);

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
        System.out.println(commentVo.getComment());   // 댓글 받음
        System.out.println(cnt);        // 댓글 달기 전 개수
        int newCnt = cnt + 1;
        System.out.println(newCnt);        // 댓글 달은 후 개수

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
        object.addProperty("cnt", sNewCnt);



        return object.toString();
    }



    // 대댓글 추가
    @ResponseBody
    @PostMapping("/comment/reply")
    public String replyComment(){

        return null;
    }


    // 삭제
    @ResponseBody
    @PostMapping("/comment/delete")
    public String deleteComment(){

        return "";

    }

    // 수정
      @ResponseBody
      @PostMapping("/comment/modify")
      public String modifyComment(){
        return "";
    }


}
