package com.pentyugov.wflow.core.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto extends BaseDto {

    private String title;
    private String description;
    private String category;
    private Date date;
    private String color;

}
