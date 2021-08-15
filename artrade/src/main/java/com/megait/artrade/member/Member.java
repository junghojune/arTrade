package com.megait.artrade.member;


import com.megait.artrade.work.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


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



}