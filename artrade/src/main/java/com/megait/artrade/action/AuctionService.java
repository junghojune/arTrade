package com.megait.artrade.action;



import com.megait.artrade.member.Member;
import com.megait.artrade.offerprice.OfferPrice;
import com.megait.artrade.offerprice.OfferpriceRepository;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkRepository;
import com.megait.artrade.work.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final OfferpriceRepository offerpriceRepository;
    private final WorkRepository workRepository;
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


    public void createAuction(List<Work> work , Member member){

        LocalDateTime localDateTime = LocalDateTime.now().plusDays(2);

        for(int i=0 ; i < work.size(); i++){
            Work work_ = work.get(i);
            OfferPrice offerPrice_ = createOfferPrice(member);
            Auction auction = Auction.builder()
                    .auctionClosingTime(localDateTime)
                    .offerPrice(List.of(offerPrice_))
                    .winingBid(5)
                    .status(AuctionStatusType.경매중)
                    .auctionProduct(work_)
                    .build();

            Auction auction_ = auctionRepository.save(auction);
            work_.setAuction(auction_);
            workRepository.save(work_);
            offerPrice_.setAuction(auction_);
            offerpriceRepository.save(offerPrice_);

        }
    }

    private OfferPrice createOfferPrice(Member member) {
        OfferPrice offerPrice = OfferPrice.builder()
                .offerPrice(5)
                .member(member)
                .offerAt(LocalDateTime.now())
                .build();
        OfferPrice offerPrice_ = offerpriceRepository.save(offerPrice);
        return offerPrice_;
    }


    @Transactional
    public boolean checkTimeoutAuction(Work work)  {
        LocalDateTime auctionClosingTime = work.getAuction().getAuctionClosingTime();
        LocalDateTime duedate = LocalDateTime.parse(auctionClosingTime.toString());
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().toString());
        if(now.isAfter(duedate)){
           work.getAuction().setStatus(AuctionStatusType.미체결됨);
           work.setTimeoutAuction(true);
        }
        System.out.println("경매가 마감되면 true , 마감 되지 않으면 false");
        return now.isAfter(duedate);
    }

}
