package com.pentyugov.wflow.web.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KanbanRequest {

    private String taskId;
    private String state;
    private Integer order;

}
