package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CardHistoryDto extends BaseDto {

    private String comment;
    private UserDto user;
    private Date createDate;
    private String result;

}
