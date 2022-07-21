package com.pentyugov.wflow.web.payload.request;

public class KanbanRequest {

    private String taskId;
    private String state;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
