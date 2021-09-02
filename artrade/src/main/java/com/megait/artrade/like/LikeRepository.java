package com.megait.artrade.like;

import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findLikeByMemberAndWork(Member member, Work work);

    Like findAllByMemberAndWork(Member member, Work work);
}
