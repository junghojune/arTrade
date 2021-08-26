package com.megait.artrade.action;



import com.megait.artrade.offerprice.OfferPrice;
import com.megait.artrade.offerprice.OfferpriceRepository;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final OfferpriceRepository offerpriceRepository;
    private final WorkService workService;


    public double findMaxPrice(long workId){

        double maxPrice = 0;
        Work work = workService.getWork(workId);

        Auction auction = work.getAuction();
        if(auction != null){
            List<OfferPrice> offerPriceList = auction.getOfferPrice();
            if(offerPriceList == null){
                return 0;
            }else {
                for(int i=0; i< offerPriceList.size(); i++){
                   Double offerPrices =offerPriceList.get(i).getOfferPrice();
                    if(offerPrices > maxPrice){
                        maxPrice = offerPrices;
                    }
                }
            }
        }
         return maxPrice;
    }



    public OfferPrice saveOfferPrice(OfferPrice offerPrice ){
        offerPrice.setOfferAt(LocalDateTime.now());
        OfferPrice price = offerpriceRepository.save(offerPrice);
        return price;
    }

    public Auction saveAuction(Auction auction){
       return auctionRepository.save(auction);
    }




}
