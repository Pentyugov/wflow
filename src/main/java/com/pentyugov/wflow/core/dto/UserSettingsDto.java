package com.pentyugov.wflow.core.dto;

public class UserSettingsDto extends BaseDto {

    private String locale;

    private Boolean miniSidebar;

    private Boolean enableChatNotificationSound;

    private Integer themeColor;

    private Boolean darkTheme;


    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getThemeColor() {
        return themeColor;
    }

    public Boolean getDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(Boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public void setThemeColor(Integer themeColor) {
        this.themeColor = themeColor;
    }

    public Boolean getEnableChatNotificationSound() {
        return enableChatNotificationSound;
    }

    public void setEnableChatNotificationSound(Boolean enableChatNotificationSound) {
        this.enableChatNotificationSound = enableChatNotificationSound;
    }

    public Boolean getMiniSidebar() {
        return miniSidebar;
    }

    public void setMiniSidebar(Boolean miniSidebar) {
        this.miniSidebar = miniSidebar;
    }
}
