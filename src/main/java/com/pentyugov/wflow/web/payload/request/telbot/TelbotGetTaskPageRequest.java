package com.pentyugov.wflow.web.payload.request.telbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelbotGetTaskPageRequest {
    Long telUserId;
    Integer page;
    String sortBy;

}
