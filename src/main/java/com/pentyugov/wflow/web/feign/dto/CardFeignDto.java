package com.pentyugov.wflow.web.feign.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CardFeignDto extends FeignDto {

    private String number;
    private String description;
    private String comment;
    private String state;
    private CardFeignDto parentCard;
    private UUID creatorId;
    private IssueFeignDto issue;

}
