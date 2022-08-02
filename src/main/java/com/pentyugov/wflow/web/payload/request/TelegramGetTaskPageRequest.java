package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class TelegramGetTaskPageRequest {
    Long telUserId;
    Optional<Integer> page;
    Optional<String> sortBy;

}
