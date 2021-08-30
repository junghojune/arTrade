
package com.megait.artrade.work;

import com.megait.artrade.action.Auction;
import com.megait.artrade.action.AuctionRepository;
import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;

    private final AuctionRepository auctionRepository;

    private final MemberRepository memberRepository;


    public Work processNewWork(Member member, String title, String contents,
                               String filePath) {

        Work work = Work.builder()
                .title(title)
                .contents(contents)
                .filePath(filePath)
                .copyrightHolder(member)
                .uploadAt(LocalDateTime.now())
                .seller(member)
                .checkToken(true)
                .build();

        Work newWork = workRepository.save(work);

        Member member_ = memberRepository.getById(member.getId());
        List<Work> works = member_.getWorks();
        works.add(newWork);

        memberRepository.save(member_);


        return newWork;
    }


    public Work getWork(Long id){
        Work work = workRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 작품번호로 작품을 조회할 수 없습니다");
        });
        return work;
    }



    public Work saveWork(Work work){
        return workRepository.save(work);
    }

    public Auction saveAuction(Auction auction){
        return auctionRepository.save(auction);
    }

    public List<Work> getAllWorkList() {

        return workRepository.findAllByAuctionIsNotNull();

    }

    public Work findByTitle(String title){
        return   workRepository.findByTitle(title).orElseThrow(()->{
            return  new IllegalArgumentException("작품을 찾을 수 없습니다 ByTitle");
        });


    }

    public List<Work> createWork(Member member){

        String[] title = new String[]{"알파벳A" , "알파벳B", "알파벳C","알파벳D","알파벳E","알파벳F"};
        Optional<Member> memberOptional = memberRepository.findById(member.getId());
        Member member_ = memberOptional.get();
        if(memberOptional.isEmpty()){
            return null;
        }
        List<Work> workList = new ArrayList<>();
        int number = 0;
        for(String i : title) {
            int a = number++;
            System.out.println( "number++"+a);
            String s = Integer.toString(a+1);
            String filename = "/images/work_list/img" + s + ".jpg";
            Work work = Work.builder()
                    .title(title[a])
                    .contents( title[a]+"은 알파벳시리즈 중 하나로 파스텔 톤을 베이스로 하여 만들어진 작품이다.")
                    .filePath(filename)
                    .copyrightHolder(member_)
                    .uploadAt(LocalDateTime.now())
                    .checkToken(true)
                    .seller(member_)
                    .build();
            workList.add(work);
        }
        List<Work> workList_ = workRepository.saveAll(workList);
        memberOptional.get().setWorks(workList_);
        memberRepository.save(member_);
        return workList_;
    }

}