package com.megait.artrade.authentication;

import com.megait.artrade.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;


    @Transactional
    public void sendEmail(Member member){
        member.generateEmailCheckToken();
        String url = "http://127.0.0.1:8080/email_check_token?token="
                +member.getEmailCheckToken()+"&email=" + member.getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setFrom("admin_no_reply@artTrade.com");
        message.setSubject("[artTrade] 회원가입 이메일 인증 링크입니다");
        message.setText("다음 링크를 클릭해 주세요. => " + url);
        javaMailSender.send(message);


    };

}
