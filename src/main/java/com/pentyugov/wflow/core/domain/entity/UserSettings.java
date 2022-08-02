package com.pentyugov.wflow.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "WIDGET_SETTINGS", length = 1000)
    private String widgetSettings;

}
