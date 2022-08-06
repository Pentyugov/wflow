package com.pentyugov.wflow.web.payload.response.telbot;

import com.pentyugov.wflow.core.dto.TelegramUserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TelbotLoggedUsersResponse {
    private HttpStatus httpStatus;
    private List<TelegramUserDto> users;
}
