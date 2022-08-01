package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelegramGetTaskPageRequest {
    Long telUserId;
    Integer page;
    String sortBy;

}
