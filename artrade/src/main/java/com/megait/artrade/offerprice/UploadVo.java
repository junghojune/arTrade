package com.megait.artrade.offerprice;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UploadVo {

       @NotNull(message = "필수 항목입니다.")
       private long id;

       @NotNull(message = "초기 경매가를 입력해주세요.")
       private Double defaultValue;


       private LocalDate date;

       private LocalTime time;

}
