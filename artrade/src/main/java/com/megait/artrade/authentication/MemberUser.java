package com.megait.artrade.authentication;


import com.megait.artrade.member.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;


@Getter
public class MemberUser extends User implements OAuth2User {

    private Member member;
    private  Map<String, Object> attributes;


    public MemberUser(Member member) {
        super(
               member.getUsername(),
               member.getPassword(),
        List.of(new SimpleGrantedAuthority(member.getType().name()))
        );

        this.member = member;

    }

    public MemberUser(Member member , Map<String, Object> attributes) {
        super(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getType().name()))
        );

        this.member = member;
        this.attributes = attributes;

    }



    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
