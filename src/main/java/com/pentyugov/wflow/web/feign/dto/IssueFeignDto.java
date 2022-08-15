package com.pentyugov.wflow.web.feign.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class IssueFeignDto extends FeignDto {

    private String name;
    private String result;
    private String comment;
    private String userId;
    private String initiatorId;
    private String executorId;
    private CardFeignDto card;

}
