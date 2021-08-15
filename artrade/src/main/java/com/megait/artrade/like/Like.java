package com.megait.artrade.like;


import com.megait.artrade.member.Member;
import com.megait.artrade.work.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Work work;

    private LocalDateTime likedAt;

    @ManyToOne
    private Member member;

    private boolean status;

    private LocalDateTime modifyAt;


}
