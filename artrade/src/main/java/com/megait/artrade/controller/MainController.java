package com.megait.artrade.controller;

import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.authentication.SignUpForm;
import com.megait.artrade.authentication.SignUpFormValidator;
import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import com.megait.artrade.member.MemberService;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import javax.validation.Valid;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final WorkService workService;

    @InitBinder("signUpForm")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new SignUpFormValidator(memberRepository));
    }

    @RequestMapping("/")
    public String index() { return "index";}


    @GetMapping("/login")
    public String loginPage() {
        return "member/loginForm";
    }

    @Transactional
    @GetMapping("/email_check_token")
    public String emailCheckToken(String token , String email , Model model){
        Optional<Member> optional = memberRepository.findByEmail(email);

        if(optional.isEmpty()){
            model.addAttribute("error", "잘못된 이메일");
            System.out.println("email" + email  +", Member email" + optional.get() + "해당하는 메일 없음");
            return "member/checked_email";
        }

        Member member = optional.get();

        if(!(member.isValidToken(token))){
            model.addAttribute("error" , "잘못된 토큰");
            System.out.println("token" + token +", Member token" + member.getEmailCheckToken());
            return "member/checked_email";
        }

        model.addAttribute("username" , member.getUsername());
        member.completeSingUp();
        return "member/checked_email";

    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "member/signUpForm";
    }


    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        // 유효성 검사 시작. - initBinder() 가 실행됨.
        if (errors.hasErrors()) {
            log.error("errors : {}", errors.getAllErrors());
            return "member/signUpForm";
        }
        log.info("올바른 회원 정보.");

        Member member = memberService.processNewMember(signUpForm);
        memberService.login(member);

        return "redirect:/";
    }

    @GetMapping("/workUpload")
    public String upload(@AuthenticationMember Member member){
        return "work/uploadForm";
    }


    @PostMapping("/workUpload")
    public String uploadMulti(@RequestParam("uploadFile") MultipartFile file, @AuthenticationMember Member member
            , @RequestParam("title") String title, @RequestParam("contents") String contents) throws Exception {
        String rootPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
        String basePath = rootPath + "/testFileUpload";



        if (!file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String originalFileExtension = originalName.substring(originalName.lastIndexOf("."));
            String originalFileTilte = originalName.substring(0, originalName.lastIndexOf("."));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = simpleDateFormat.format(new Date());
            String storedFileName = currentDate + "-" + originalFileTilte + originalFileExtension;
            String filePath = basePath + "/" + storedFileName;
            Work work = workService.processNewWork(member, title, contents, filePath);
            File dest = new File(filePath);
            file.transferTo(dest);
        }

        return "redirect:/";
    }


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
