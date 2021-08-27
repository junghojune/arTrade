package com.megait.artrade.offerprice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferPriceService {
    private final OfferpriceRepository offerpriceRepository;


    public OfferPrice saveOfferPrice(OfferPrice offerPrice){
        return offerpriceRepository.save(offerPrice);

    }
}
