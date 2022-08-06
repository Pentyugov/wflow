package com.pentyugov.wflow.web.payload.response.telbot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TelbotLoginUserResponse {

    private String userId;
    private String hashCode;
    private String status;
}
