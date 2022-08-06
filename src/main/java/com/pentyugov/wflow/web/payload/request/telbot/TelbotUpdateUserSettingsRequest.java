package com.pentyugov.wflow.web.payload.request.telbot;

import com.pentyugov.wflow.core.dto.TelegramUserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TelbotUpdateUserSettingsRequest {
    Long telUserId;
    TelegramUserDto.TelUserSettings userSettings;
}