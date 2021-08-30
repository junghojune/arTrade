package com.megait.artrade.controller;

import com.megait.artrade.action.Auction;
import com.megait.artrade.action.AuctionService;
import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.authentication.EmailService;
import com.megait.artrade.authentication.SignUpForm;
import com.megait.artrade.authentication.SignUpFormValidator;
import com.megait.artrade.like.Like;
import com.megait.artrade.like.LikeService;
import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import com.megait.artrade.member.MemberService;
import com.megait.artrade.offerprice.OfferPrice;
import com.megait.artrade.offerprice.OfferPriceService;
import com.megait.artrade.offerprice.UploadVo;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;



@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {



    private final AuctionService auctionService;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final WorkService workService;

    private final OfferPriceService offerPriceService;

    private final EmailService emailService;

    private final LikeService likeService;

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

// 오픈마켓 들어가기

    @GetMapping("/openMarket")
    public String openMarket(@AuthenticationMember Member member, Model model) {
        List<Work> workList = workService.getAllWorkList();

        model.addAttribute("workList", workList);
        return "auction/market";
    }






//    경매

    @GetMapping("/auction/search")
    public String auctionSearchpage(String search,@AuthenticationMember Member member ,Model model){
        System.out.println(search);
        Work work = workService.findByTitle(search);
        return auctionPage(work.getId(), model);
    }


    @GetMapping("/auction/{id}")
    public String auctionPage(@PathVariable Long id ,Model model){

        try{
            Work work = workService.getWork(id);
            double maxPrice = auctionService.findMaxPrice(id);
            model.addAttribute("work", work);
            model.addAttribute("maxPrice", Double.toString(maxPrice));

        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return "auction/workdetail";
    }

    @GetMapping("/regauction")
    public String registerAuction( @AuthenticationMember Member member , Model model){


        model.addAttribute("uploadVo" ,new UploadVo());
        model.addAttribute("now" , LocalDate.now());
        model.addAttribute("nextMonth" , LocalDate.now().plusMonths(1));
        Member member_ = memberService.getMember(member.getId());

        List<Work> workList = member_.getWorks();
        if(workList.size() == 0){
            model.addAttribute("status","notExist");
            model.addAttribute("workList" ,"등록된 작품이 없습니다");
        }else{
            model.addAttribute("status","Exist");
            model.addAttribute("workList" , workList);
        }

        return "auction/regAuction";
    }





    @ResponseBody
    @GetMapping("/auction/computeAutionTime/{id}")
    public String computeAutionTime(@PathVariable Long id){

        JsonObject object = new JsonObject();
        Work work = null;
        try{ work = workService.getWork(id);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }


        LocalDateTime closingTime = LocalDateTime.now();
        if (work != null) {
            Auction auction = work.getAuction();
            if(auction == null) {
                object.addProperty("status", "notExistAuction");
                object.addProperty("result", "해당 작품에 등록된 경매가 없습니다");
                return object.toString();
            }else{
                closingTime = auction.getAuctionClosingTime();
            }
        }

        LocalDateTime localDateTimeNow = LocalDateTime.now();


        int year =  localDateTimeNow.getYear();
        int month = localDateTimeNow.getMonth().getValue();
        int day = localDateTimeNow.getDayOfMonth();
        int hour = localDateTimeNow.getHour();
        int minute = localDateTimeNow.getMinute();
        int second = localDateTimeNow.getSecond();

        LocalDateTime remainDateTime = closingTime
                .minusYears(year)
                .minusMonths(month)
                .minusDays(day)
                .minusHours(hour)
                .minusMinutes(minute)
                .minusSeconds(second);

        String days = String.valueOf(remainDateTime.getDayOfMonth());
        String hours = String.valueOf(remainDateTime.getHour());
        String minutes = String.valueOf(remainDateTime.getMinute());
        String seconds = String.valueOf(remainDateTime.getSecond());


        object.addProperty("days", days);
        object.addProperty("hours", hours);
        object.addProperty("minutes", minutes);
        object.addProperty("seconds", seconds);

        return object.toString();
    }


    @ResponseBody
    @GetMapping("/auction/suggest")
    public String suggestAuctionPrice(@AuthenticationMember Member member){
        JsonObject object = new JsonObject();
        if(member == null){
            object.addProperty("status","notLogin");
            object.addProperty("message","로그인이 필요한 기능입니다.");
            object.addProperty("guide","로그인 페이지로 이동");
            return object.toString();
        }

        if(member.getWalletId() == null){
            object.addProperty("status","notWallet");
            object.addProperty("message","등록된 가상화폐가 없습니다. ");
            object.addProperty("guide","지갑 생성하기");
            return object.toString();
        }

        try{
            object.addProperty("status","offer");
            object.addProperty("message", "경매가 제시하기");
            object.addProperty("guide","제안하기");

        }catch (IllegalArgumentException e){
            object.addProperty("message", e.getMessage());
        }

        return object.toString();
    }



    @ResponseBody
    @PostMapping("/auction/suggest/detail/{id}")
    public String suggestAuctionPriceDetail(@RequestBody OfferPrice offerPrice , @PathVariable Long id , @AuthenticationMember Member member ){

        Work work = null;
        Auction auction = null;
        try{

            work = workService.getWork(id);
            auction = work.getAuction();
            Member member_ =memberService.getMember(member.getId());

            offerPrice.setAuction(auction);
            offerPrice.setMember(member_);

        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        OfferPrice offerPrice2 = auctionService.saveOfferPrice(offerPrice);
        List<OfferPrice> list = auction.getOfferPrice();
        list.add(offerPrice2);

        JsonObject object = new JsonObject();
        double maxPrice = auctionService.findMaxPrice(id);
        auction.setWiningBid(maxPrice);

        auctionService.saveAuction(auction);

        object.addProperty("maxPrice", Double.toString(maxPrice));
        object.addProperty("status", "경매 제안이 완료되었습니다");
        return object.toString();
    }



    @ResponseBody
    @PostMapping("/auction/registration")
    public String registerAuction(@AuthenticationMember Member member , @RequestBody Work work_ ){
        System.out.println(work_+"work_");
        Work work = workService.getWork(work_.getId());
        Auction auction = work.getAuction();

        boolean checkToken = work.isCheckToken();


        JsonObject object = new JsonObject();
        LocalDate now = LocalDate.now();
        System.out.println(now);
        object.addProperty("now", now.toString());
        object.addProperty("nextMonth", now.plusMonths(1).toString());

        if(member == null){
            object.addProperty("status","notLogin");
            object.addProperty("message","로그인이 필요한 기능입니다.");
            object.addProperty("guide","로그인 페이지로 이동");

            return object.toString();
        }

        if(checkToken == false){
            object.addProperty("status","notCheckToken");
            object.addProperty("message","NFT 등록된 작품만 오픈 마켓에 경매 작품으로 등록할 수 있습니다.");
            object.addProperty("guide","NFT 발급받기");
            return object.toString();
        }


        if(auction != null){
            object.addProperty("status","alreadyUploadedWork");
            object.addProperty("message","이미 경매에 올라간 작품입니다");
            object.addProperty("guide","확인");
            return object.toString();
        }
        object.addProperty("status","ok");
        object.addProperty("message", "등록이 완료되었습니다");
        object.addProperty("guide"," 확인 ");

        object.addProperty("contents",work.getContents());

        System.out.println(work.getTitle()+"work.getTitle()");

        return object.toString();
    }


    @ResponseBody
    @PostMapping("/auction/upload")
    public String uploadWork(@AuthenticationMember Member member , @RequestBody  UploadVo uploadVo  ){

        LocalDate date = uploadVo.getDate();
        LocalTime time = uploadVo.getTime();

        int year = date.getYear();
        int month = date.getMonth().getValue();
        int dayOfMonth = date.getDayOfMonth();

        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        Work work = workService.getWork(uploadVo.getId());


        Auction auction = Auction.builder()
                .auctionClosingTime(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second))
                .winingBid(uploadVo.getDefaultValue())
                .auctionProduct(work)
                .build();
        Auction auction_ = workService.saveAuction(auction);

        OfferPrice offerPrice = OfferPrice.builder()
                  .offerAt(LocalDateTime.now())
                  .auction(auction_)
                  .offerPrice(uploadVo.getDefaultValue())
                  .member(member)
                  .build();


        OfferPrice offerPrice_ = offerPriceService.saveOfferPrice(offerPrice);
        auction_.setOfferPrice(List.of(offerPrice_));

        work.setAuction(auction_);
        workService.saveWork(work);

        JsonObject object = new JsonObject();
        if(member == null){
            object.addProperty("status","notLogin");
            object.addProperty("message","로그인이 필요한 기능입니다.");
            object.addProperty("guide","로그인 페이지로 이동");
            return object.toString();
        }


        object.addProperty("status","ok");
        object.addProperty("message", "등록이 완료되었습니다");
        object.addProperty("guide"," 확인 ");



        return object.toString();
    }

    @GetMapping("/member/findpw")
    public String findPWpage(){
        return "member/findPW";
    }

    //비밀번호찾기
    @ResponseBody
    @PostMapping("/member/findpw")
    public String findPw(@RequestBody Member member ) {


        JsonObject object = new JsonObject();
        Member member_ = memberService.getMemberByUserName(member.getUsername());

        // 가입된 아이디가 없으면
        if (member_ == null) {
            object.addProperty("status", "fail");
            object.addProperty("message", "등록되지 않은 아이디입니다.");
            return object.toString();

        }
        // 가입된 이메일이 아니면
        else if (!member_.getEmail().equals(member.getEmail()) ) {
            object.addProperty("status", "fail");
            object.addProperty("message", "등록되지 않은 이메일입니다.");
            return object.toString();
        } else {
            String ramdomPassword = memberService.getRamdomPassword(10);
            // 비밀번호 변경
            emailService.sendfindPWEmail(member_, ramdomPassword);
            // 비밀번호 변경 메일 발송
            object.addProperty("status", "success");
            object.addProperty("message", "이메일로 임시 비밀번호를 발송하였습니다.");

            return object.toString();
        }
    }

    // 좋아요 버튼 늘렀을시
    @GetMapping("/work/like/{id}")
    @ResponseBody
    public String countLike(@AuthenticationMember Member member, @PathVariable Long id){

        Like like = likeService.addLike(member, workService.getWork(id));

        int countLike = like.getWork().getPopularity();

        JsonObject object = new JsonObject();
        try{

            object.addProperty("count", countLike);

        }catch (IllegalArgumentException e){
            object.addProperty("message", e.getMessage());
        }
        return object.toString();
    }
}
