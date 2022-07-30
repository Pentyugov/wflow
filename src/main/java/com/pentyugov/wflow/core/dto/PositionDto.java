package com.pentyugov.wflow.core.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PositionDto extends BaseDto {

    private String name;
    private String code;

}
