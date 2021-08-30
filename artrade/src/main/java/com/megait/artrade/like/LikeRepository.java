package com.megait.artrade.like;

import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findAllByMemberAndWork(Member member, Work work);
}
