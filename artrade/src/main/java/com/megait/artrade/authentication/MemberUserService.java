package com.megait.artrade.authentication;


import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberUserService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username : " + username);
         Member principal = memberRepository.findByUsername(username)
                .orElseThrow(()->{
                    log.info("없는 이메일로 로그인 시도");
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다 :" + username);
                });

         principal.setLastLoginDatetime(LocalDateTime.now());
         memberRepository.save(principal);
         return new MemberUser(principal);
    }
}
