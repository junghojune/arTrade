package com.megait.artrade.authentication;

import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm form = (SignUpForm) target ;
        Optional<Member> optional = memberRepository.findByUsername(form.getUsername());
        if(optional.isPresent()){
            errors.rejectValue("username" ,"duplicate.username", "이미 사용중이거나 탈퇴한 아이디입니다.");
        }
       if(!form.getPassword().equals(form.getPasswordCheck())){
           errors.rejectValue("passwordCheck" ,"Passwords do not match", "비밀번호가 일치하지 않습니다.");
       }

    }
}
