package com.pentyugov.wflow.core.dto;

import com.pentyugov.wflow.core.domain.entity.UserSettings;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TelegramUserDto {

    private UUID userId;
    private Long telUserId;
    private Long telChatId;
    private Boolean telLogged = Boolean.FALSE;
    private TelUserSettings userSettings;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class TelUserSettings {

        public TelUserSettings(UserSettings userSettings) {
            this.subscribeOnTasks = userSettings.getTelbotTaskNotification();
            this.subscribeOnCalendar = userSettings.getTelbotCalendarNotification();
        }

        private Boolean subscribeOnTasks = Boolean.FALSE;
        private Boolean subscribeOnCalendar = Boolean.FALSE;

    }
}
