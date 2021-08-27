package com.megait.artrade.offerprice;

import com.megait.artrade.action.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferpriceRepository extends JpaRepository<OfferPrice, Long> {
    Optional<OfferPrice> findByAuction(Auction auction);
}
