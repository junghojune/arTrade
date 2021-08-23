package com.megait.artrade.offerprice;



import com.megait.artrade.action.Auction;
import com.megait.artrade.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class OfferPrice {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Member member;

    private LocalDateTime offerAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private double offerPrice;

    @ManyToOne
    private Auction auction;



}

