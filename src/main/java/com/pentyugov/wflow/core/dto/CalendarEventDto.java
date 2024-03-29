package com.pentyugov.wflow.core.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDto extends BaseDto {

    private int type;
    private String title;
    private String description;
    private Date start;
    private Date end;
    private Color color;
    private Boolean allDay = Boolean.FALSE;
    private Boolean draggable = Boolean.FALSE;
    private Resizable resizable;
    private String[] actions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Color implements Serializable {
        private String primary;
        private String secondary;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resizable implements Serializable {
        private Boolean beforeStart = Boolean.FALSE;
        private Boolean afterEnd = Boolean.FALSE;
    }


}
