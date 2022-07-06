package com.pentyugov.wflow.core.dto;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class NotificationDto extends BaseDto {

    private Date createDate;

    private String title;

    private String message;

    private int type;

    private int accessoryType;

    private Boolean read;

    private UUID receiverId;

    public int getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(int accessoryType) {
        this.accessoryType = accessoryType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateDate(LocalDateTime localDateTime) {
        this.createDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }
}
