package com.megait.artrade.comment;


import com.megait.artrade.authentication.AuthenticationMember;
import com.megait.artrade.comment.Comment;
import com.megait.artrade.comment.CommentRepository;
import com.megait.artrade.member.Member;
import com.megait.artrade.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    // 댓글 레포
    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;


    //Comment를 가져오기
    public Comment getComment(Long id){

        // 댓글만 꺼내기
        Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.get();
    }



    // 댓글 저장
    public Comment saveComment(String contents, Member member){
        Comment comment = Comment.builder()
                .contents(contents)
                .member(member)
                .commentClass(CommentType.댓글)
                .createAt(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }



    // 대댓글 저장
    public Comment saveReplyComment(String contents, @AuthenticationMember Member member){
        Comment comment = Comment.builder()
                .contents(contents)
                .member(member)
                .commentClass(CommentType.대댓글)
                .createAt(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }





    // 댓글 수정








    // 대댓글 수정

}