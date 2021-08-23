package com.megait.artrade.olineexhibit;

import com.megait.artrade.work.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineExhibit {

    @Id
    @GeneratedValue
    private Long id;

    private String topic;

    private int topicId;

    private String Theme;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToMany
    private List<Work> works = new ArrayList<>();

    private int displayOrder;


}
