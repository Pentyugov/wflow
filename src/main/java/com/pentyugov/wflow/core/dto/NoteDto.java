package com.pentyugov.wflow.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NoteDto extends BaseDto {

    private String title;
    private String description;
    private String category;
    private Date date;
    private String color;

}
