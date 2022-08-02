package com.pentyugov.wflow.web.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTTokenSuccessResponse {
    private boolean success;
    private String token;

}
