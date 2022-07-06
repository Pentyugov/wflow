package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "security$UserSettings")
@Table(name = "SYSTEM_USER_SETTINGS")
@Where(clause="DELETE_DATE is null")
public class UserSettings extends BaseEntity {

    public static final Integer THEME_BLUE = 10;
    public static final Integer THEME_RED = 20;
    public static final Integer THEME_GREEN = 30;


    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "LOCALE", length = 2)
    private String locale;

    @Column(name = "THEME_COLOR")
    private Integer themeColor;

    @Column(name = "DARK_THEME")
    private Boolean darkTheme;

    @Column(name = "MINI_SIDEBAR")
    private Boolean miniSidebar;

    @Column(name = "ENABLE_CHAT_NOTIFICATION_SOUND")
    private Boolean enableChatNotificationSound;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Boolean getMiniSidebar() {
        return miniSidebar;
    }

    public void setMiniSidebar(Boolean miniSidebar) {
        this.miniSidebar = miniSidebar;
    }

    public Boolean getDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(Boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public Integer getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(Integer themeColor) {
        this.themeColor = themeColor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getEnableChatNotificationSound() {
        return enableChatNotificationSound;
    }

    public void setEnableChatNotificationSound(Boolean enableChatNotificationSound) {
        this.enableChatNotificationSound = enableChatNotificationSound;
    }
}
