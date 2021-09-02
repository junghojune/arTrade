package com.megait.artrade.comment;

import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findByWork(Work work);
    Optional<Comment> findByMemberAndContentsAndWork(Member member, String contents, Work work);


}
