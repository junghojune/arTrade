package com.megait.artrade.authentication;


import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import com.megait.artrade.member.MemberType;
import com.megait.artrade.provider.FacebookUserInfo;
import com.megait.artrade.provider.GoogleUserInfo;
import com.megait.artrade.provider.NaverUserInfo;
import com.megait.artrade.provider.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserService extends DefaultOAuth2UserService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(userRequest.getClientRegistration());
        System.out.println(super.loadUser(userRequest).getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            log.info("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>)oAuth2User.getAttributes().get("response"));
        }else {
           log.info("지원하지 않는 형식의 로그인 접근방식입니다");
        }

        String email = oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId()+"@"+"test.com";
        String password = passwordEncoder.encode("OauthPassword!");

        Member principal = memberRepository.findByEmail(email).orElse(null);
        if(principal == null ) {
             principal = Member.builder()
                     .email(email)
                     .username(oAuth2UserInfo.getName())
                     .nickname(oAuth2UserInfo.getName())
                     .password(password)
                     .provider(oAuth2UserInfo.getProvider())
                     .registerDateTime(LocalDateTime.now())
                     .lastLoginDatetime(LocalDateTime.now())
                     .type(MemberType.일반회원).build();


        }else{
            principal.setLastLoginDatetime(LocalDateTime.now());
        }
        memberRepository.save(principal);


        return new MemberUser(principal, oAuth2User.getAttributes());


    }
}
