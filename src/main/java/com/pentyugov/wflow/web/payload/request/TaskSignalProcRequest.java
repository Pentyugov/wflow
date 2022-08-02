package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSignalProcRequest {

    private String taskId;
    private String action;
    private String comment;

}
