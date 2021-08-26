
package com.megait.artrade.work;

import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;


    public Work processNewWork(Member member, String title, String contents,
                               String filePath) {

        Work work = Work.builder()
                .title(title)
                .contents(contents)
                .filePath(filePath)
                .copyrightHolder(member)
                .uploadAt(LocalDateTime.now())
                .seller(member)
                .build();

        Work newWork = workRepository.save(work);

        return newWork;
    }
}