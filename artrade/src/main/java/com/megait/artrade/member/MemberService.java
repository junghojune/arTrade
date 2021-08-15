package com.megait.artrade.member;



import com.megait.artrade.authentication.MemberUser;
import com.megait.artrade.authentication.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    @Profile("local")
    public void createNewMember(){

        Member member = Member.builder()
                .username("admin")
                .email("admin@test.com")
                .password(passwordEncoder.encode("Password!"))
                .nickname("테스트 관리자")
                .type(MemberType.일반회원)
                .registerDateTime(LocalDateTime.now())
                .build();

        Member newMember = memberRepository.save(member);

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


}
