package com.pentyugov.wflow.core.dto;

import java.util.ArrayList;
import java.util.List;

public class WidgetSettingsDto {

    public WidgetSettingsDto() {

    }

    public WidgetSettingsDto(Integer type, Boolean visible, Integer bucket, Integer index) {
        this.type = type;
        this.visible = visible;
        this.bucket = bucket;
        this.index = index;
    }

    public final static Integer WIDGET_CALENDAR_TYPE = 10;
    public final static Integer WIDGET_ACTIVE_TASKS_TYPE = 20;
    public final static Integer WIDGET_PRODUCTIVITY_TYPE = 30;

    private Integer type = 0;
    private Boolean visible = Boolean.TRUE;
    private Integer bucket = 0;
    private Integer index = 0;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getBucket() {
        return bucket;
    }

    public void setBucket(Integer bucket) {
        this.bucket = bucket;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public static List<WidgetSettingsDto> createDefaultWidgetSettings() {
        List<WidgetSettingsDto> result = new ArrayList<>();
        result.add(new WidgetSettingsDto(WIDGET_CALENDAR_TYPE, true, 0, 0));
        result.add(new WidgetSettingsDto(WIDGET_PRODUCTIVITY_TYPE, true, 1, 0));
        result.add(new WidgetSettingsDto(WIDGET_ACTIVE_TASKS_TYPE, true, 1, 1));
        return result;
    }


}
