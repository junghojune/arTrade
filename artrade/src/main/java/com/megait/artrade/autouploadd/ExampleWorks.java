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
        Member newMember = memberService.createNewMember();
        List<Work> work = workService.createWork(newMember);
        auctionService.createAuction(work , newMember);

    }
}
