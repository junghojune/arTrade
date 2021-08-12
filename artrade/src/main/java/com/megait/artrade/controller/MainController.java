package com.megait.artrade.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @RequestMapping("/")
    public String index() { return "index";}



    @GetMapping("/mypage")
    public String mypage(){
        return "fragments/mypage";
    }
    @GetMapping("/mypage/comment")
    public String mycomment(){
        return "mypage/my_comment";
    }
    @GetMapping("/mypage/recent")
    public String myrecent(){
        return "mypage/my_recent";
    }
    @GetMapping("/mypage/like")
    public String mylike(){
        return "mypage/my_like";
    }
}
