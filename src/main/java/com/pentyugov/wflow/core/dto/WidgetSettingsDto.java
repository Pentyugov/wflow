package com.pentyugov.wflow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WidgetSettingsDto implements Serializable {

    public static final Integer WIDGET_CALENDAR_TYPE = 10;
    public static final Integer WIDGET_ACTIVE_TASKS_TYPE = 20;
    public static final Integer WIDGET_PRODUCTIVITY_TYPE = 30;

    private Integer type = 0;
    private Boolean visible = Boolean.TRUE;
    private Integer bucket = 0;
    private Integer index = 0;

    public static List<WidgetSettingsDto> createDefaultWidgetSettings() {
        List<WidgetSettingsDto> result = new ArrayList<>();
        result.add(new WidgetSettingsDto(WIDGET_CALENDAR_TYPE, true, 0, 0));
        result.add(new WidgetSettingsDto(WIDGET_PRODUCTIVITY_TYPE, true, 1, 0));
        result.add(new WidgetSettingsDto(WIDGET_ACTIVE_TASKS_TYPE, true, 1, 1));
        return result;
    }


}
