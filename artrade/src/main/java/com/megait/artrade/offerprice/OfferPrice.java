package com.megait.artrade.offerprice;



import com.megait.artrade.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferPrice {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Member member;

    private LocalDateTime offerAt;

    private double offerPrice;


}
