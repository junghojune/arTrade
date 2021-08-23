package com.megait.artrade.action;


import com.megait.artrade.offerprice.OfferPrice;
import com.megait.artrade.work.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Auction {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'경매중'")
    private AuctionStatusType status;

    @OneToOne
    private Work auctionProduct;

    @OneToMany
    private List<OfferPrice> offerPrice = new ArrayList<>();

    private LocalDateTime auctionClosingTime;

    @Column(nullable = false)
    @ColumnDefault("0")
    private double winingBid;

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", status=" + status +
                ", auctionProduct=" + auctionProduct +
                ", auctionClosingTime=" + auctionClosingTime +
                ", winingBid=" + winingBid +
                '}';
    }
}