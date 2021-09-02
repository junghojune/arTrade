package com.megait.artrade.comment;


import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.comment.Comment;
import com.megait.artrade.comment.CommentRepository;
import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import com.megait.artrade.work.Work;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    // 댓글 레포
    private final CommentRepository commentRepository;


    public List<Comment> getList(Work work){

        List<Comment> comment = commentRepository.findByWork(work).orElseThrow();
        return comment;
    }
    //Comment를 가져오기
    public Comment getComment(Member member, String contents, Work work){

        // 댓글만 꺼내기
        Comment comment = commentRepository.findByMemberAndContentsAndWork(member, contents,work).orElseThrow(()->{
            return new IllegalArgumentException("달린 댓글이 없습니다.");
        });
        return comment;
    }



    // 댓글 저장
    public Comment saveComment(String contents, Member member, Work work, int count){
        Comment comment = Comment.builder()
                .contents(contents)
                .member(member)
                .commentClass(CommentType.댓글)
                .work(work)
                .comment_cnt(count)
                .oder(1)
                .createAt(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }

}