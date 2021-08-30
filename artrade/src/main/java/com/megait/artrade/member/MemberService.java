package com.megait.artrade.member;



import com.megait.artrade.authentication.EmailService;
import com.megait.artrade.authentication.MemberUser;
import com.megait.artrade.authentication.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;




    public Member createNewMember(){

        Member  member = Member.builder()
                .username("admin")
                .email("yhr05008@naver.com")
                .password(passwordEncoder.encode("Password!"))
                .nickname("테스트 관리자")
                .type(MemberType.일반회원)
                .registerDateTime(LocalDateTime.now())
                .walletId("aaaa")
                .build();

        Member newMember = memberRepository.save(member);
        emailService.sendEmail(newMember);

        return newMember;
    }


    public Member processNewMember(SignUpForm signUpForm){

        Member member = Member.builder()
                .username(signUpForm.getUsername())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .nickname(signUpForm.getNickname())
                .type(MemberType.일반회원)
                .registerDateTime(LocalDateTime.now())
                .build();

        System.out.println("signUpForm.getIsWriter()" + signUpForm.getIsWriter());
        if(("true").equals(signUpForm.getIsWriter())){
            member.setType(MemberType.작가);
            member.setSellerAuthority(true);
        }
        Member newMember = memberRepository.save(member);
        emailService.sendEmail(newMember);
        return newMember;
    }

    public void login(Member member){

        MemberUser memberUser = new MemberUser(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        memberUser,
                        memberUser.getMember().getPassword(),
                        memberUser.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(token);

    }

    public Member getMember(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 멤버를 조회할 수 없습니다");
        });
        return member;
    }

    public Member getMemberByUserName(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isEmpty()){
            return null;
        }
        return member.get();
    }

    public String getRamdomPassword(int size) {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
                'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$',
                '%', '^', '&' };
        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
        int idx = 0; int len = charSet.length; for (int i=0; i<size; i++)
        {

            idx = sr.nextInt(len);
            // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]); }
        return sb.toString(); }





}
