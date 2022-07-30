package com.pentyugov.wflow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDto extends BaseDto {

    private String locale;
    private Boolean miniSidebar;
    private Boolean enableChatNotificationSound;
    private Integer themeColor;
    private Boolean darkTheme;
    private List<WidgetSettingsDto> widgetSettings;

}
