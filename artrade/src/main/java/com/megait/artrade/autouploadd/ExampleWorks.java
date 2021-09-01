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
        Member newMember = memberService.createNewMember("user01" , "춘식이" , "0x0260E71A86AD681F629D5ad5F43c7580E8aC24F3");
        memberService.createNewMember("user02" , "니니즈" , "0x7aFE95B205D9Abccc7437C0FD8fc8E537a4e2D68");
        List<Work> work = workService.createWork(newMember);
        auctionService.createAuction(work , newMember);

    }
}
