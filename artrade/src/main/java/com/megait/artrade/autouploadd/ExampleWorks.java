package com.megait.artrade.autouploadd;


import com.megait.artrade.action.AuctionService;
import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberService;
import com.megait.artrade.work.Work;
import com.megait.artrade.work.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExampleWorks {
    private final MemberService memberService;

    private final WorkService workService;

    private final AuctionService auctionService;


    @PostConstruct
    @Profile("local")
    public void createExampleWorks(){
        Member newMember = memberService.createNewMember("user01" , "춘식이" , "0x9F049eA69B8C68E0841D9BDdd4832f00Db14A3F9");
        memberService.createNewMember("user02" , "니니즈" , "0xcfAc79Ea16ED1A1Cb6B0fC670808Ae437C595fFA");
        List<Work> work = workService.createWork(newMember);
        auctionService.createAuction(work , newMember);

    }
}
