package com.megait.artrade.controller;

import com.megait.artrade.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


//    @ResponseBody
//    @PostMapping("/auction/comment")
//    public String commentAuction(){
//
//    }
}
