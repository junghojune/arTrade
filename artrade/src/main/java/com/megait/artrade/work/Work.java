package com.megait.artrade.work;


import com.megait.artrade.action.Auction;
import com.megait.artrade.action.AuctionStatusType;
import com.megait.artrade.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Work {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String contents;

    private String imageUrl;

    @ManyToOne
    private Member copyrightHolder;

    @ManyToOne
    private Member seller;

    private LocalDateTime uploadAt;

    private boolean checkToken;

    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @OneToOne
    private Auction auction;

    private AuctionStatusType status;

    private int popularity;
}
