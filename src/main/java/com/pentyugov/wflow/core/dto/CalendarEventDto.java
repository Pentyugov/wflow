package com.pentyugov.wflow.core.dto;

import java.util.Date;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public Resizable getResizable() {
        return resizable;
    }

    public void setResizable(Resizable resizable) {
        this.resizable = resizable;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    public static class Color {
        private String primary;
        private String secondary;

        public Color() {

        }

        public Color(String primary, String secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getSecondary() {
            return secondary;
        }

        public void setSecondary(String secondary) {
            this.secondary = secondary;
        }
    }

    public static class Resizable {
        private Boolean beforeStart = Boolean.FALSE;
        private Boolean afterEnd = Boolean.FALSE;

        public Resizable() {

        }

        public Resizable(Boolean beforeStart, Boolean afterEnd) {
            this.beforeStart = beforeStart;
            this.afterEnd = afterEnd;
        }

        public Boolean getBeforeStart() {
            return beforeStart;
        }

        public void setBeforeStart(Boolean beforeStart) {
            this.beforeStart = beforeStart;
        }

        public Boolean getAfterEnd() {
            return afterEnd;
        }

        public void setAfterEnd(Boolean afterEnd) {
            this.afterEnd = afterEnd;
        }
    }


}
