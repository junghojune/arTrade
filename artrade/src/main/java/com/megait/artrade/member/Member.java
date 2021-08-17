package com.megait.artrade.member;


import com.megait.artrade.work.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Member {


    @Id
    @GeneratedValue
    private Long id;

    private String username; // id

    private String nickname;

    private String email; // 본인 확인용 메일

    private String password;

    private LocalDateTime registerDateTime;

    private boolean emailVerified;

    private String profileImageUrl;

    private LocalDateTime lastLoginDatetime;

    private boolean isSellerAuthority;

    @OneToMany
    private List<Work> works;

    private String walletId;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    private String provider;

    private String emailCheckToken;

    public void generateEmailCheckToken(){
       emailCheckToken = UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token){
        if(emailCheckToken == null){
            return false;
        }
        System.out.println("!(emailCheckToken == null)"+emailCheckToken);
        return emailCheckToken.equals(token);
    }

    public void completeSingUp(){
        emailVerified=true;
    }


}