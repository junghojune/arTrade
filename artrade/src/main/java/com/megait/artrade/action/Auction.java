package com.megait.artrade.action;


import com.megait.artrade.offerprice.OfferPrice;
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
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue
    private Long id;

    private AuctionStatusType status;

    @OneToOne
    private Work auctionProduct;

    @OneToMany
    private List<OfferPrice> offerPrice;

    private LocalDateTime auctionClosingTime;

    private double winingBid;

}